package net.lapointep.paulsmod.block.entity;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.mixin.BrewingStandAccessor;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.lapointep.paulsmod.item.custom.SortingWandItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.lapointep.paulsmod.menu.BrewerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static net.lapointep.paulsmod.block.custom.BrewerBlock.*;
import static net.lapointep.paulsmod.block.entity.ModBlockEntities.BREWER;
import static net.lapointep.paulsmod.util.PaulsCommon.*;

public class BrewerBlockEntity extends BlockEntity implements WorldlyContainer, ExtendedMenuProvider, Nameable {
    private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    protected final ContainerData data;

    public static final int MAX_COUNTER = 20*1;
    BETicker ticker = new BETicker(MAX_COUNTER);

    public BrewerBlockEntity(BlockPos pos, BlockState state) {
        super(BREWER, pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{OUTPUT_SLOT_1, OUTPUT_SLOT_2, OUTPUT_SLOT_3};
        }
        return new int[]{INPUT_SLOT_1, INPUT_SLOT_2, INPUT_SLOT_3};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return side != Direction.DOWN && (slot == INPUT_SLOT_1 || slot == INPUT_SLOT_2 || slot == INPUT_SLOT_3);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN && (slot == OUTPUT_SLOT_1 || slot == OUTPUT_SLOT_2 || slot == OUTPUT_SLOT_3);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, slot, amount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items, true);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items.clear();
        ContainerHelper.loadAllItems(input, this.items);
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.brewer");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BrewerMenu(containerId, playerInventory, this, this.data);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BrewerBlockEntity blockEntity) {
        if (level == null || level.isClientSide()) return;
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }
        //PaulsMod.LOGGER.info("Brewer Ticking {}", pos);

        // Check if the block above is a brewing stand
        // pos.up() returns a new BlockPos shifted 1 block positive on the Y axis
        BlockPos posAbove = pos.above();
        BlockEntity aboveBlock = level.getBlockEntity(posAbove);
        if (aboveBlock instanceof BrewingStandBlockEntity brewingStand) {
           // PaulsMod.LOGGER.info("Brewing stand instance");
            int currentBrewTime = ((BrewingStandAccessor) brewingStand).getBrewTime();
            if (currentBrewTime == 0) {
                //PaulsMod.LOGGER.info("Brewer is not brewing");
                // Remove bottles from the brewing stand
                if (!PaulsCommon.hasThreeEmptyBottleSlots(brewingStand)) {
                    transferBottlesFromBrewingStand(brewingStand);
                }
            }

            // Move bottles from the input to the brewing stand
            if (PaulsCommon.hasThreeEmptyBottleSlots(brewingStand)) {
                if (this.hasThreeFullInputBottleSlots()) {
                    transferBottlesToBrewingStand(brewingStand);
                }
            }
        }


        if (this.getItem(OUTPUT_SLOT_1).isEmpty() && this.getItem(OUTPUT_SLOT_2).isEmpty() && this.getItem(OUTPUT_SLOT_3).isEmpty()) {
            //PaulsMod.LOGGER.info("No bottles in output slots");
            return;
        }

        Direction direction = level.getBlockState(pos).getValue(FACING);
        BlockPos posBeside = pos.relative(direction);
        BlockEntity blockEntityBeside = level.getBlockEntity(posBeside);
        if (blockEntityBeside == null) {
            //PaulsMod.LOGGER.info("No block entity beside");
            return;
        }
        //PaulsMod.LOGGER.info("Block entity beside {}", posBeside);
        if (blockEntityBeside instanceof BrewerBlockEntity nextBrewer) {
            moveBottlesToNextBrewer(nextBrewer);
        }
        else if (blockEntityBeside instanceof Container container) {
            moveBottlesToContainer(container);
        }

        // Move any items from the output slots to the inventory beside
    }

    public boolean hasThreeFullInputBottleSlots() {
        return !this.getItem(INPUT_SLOT_1).isEmpty() && !this.getItem(INPUT_SLOT_2).isEmpty() && !this.getItem(INPUT_SLOT_3).isEmpty();
    }

    public void transferBottlesFromBrewingStand(BrewingStandBlockEntity brewingStand) {
        this.setItem(OUTPUT_SLOT_1, brewingStand.getItem(0).copy());
        brewingStand.setItem(0, ItemStack.EMPTY);

        this.setItem(OUTPUT_SLOT_2, brewingStand.getItem(1).copy());
        brewingStand.setItem(1, ItemStack.EMPTY);

        this.setItem(OUTPUT_SLOT_3, brewingStand.getItem(2).copy());
        brewingStand.setItem(2, ItemStack.EMPTY);
        brewingStand.setChanged();
        this.setChanged();
    }

    public void transferBottlesToBrewingStand(BrewingStandBlockEntity brewingStand) {
        //PaulsMod.LOGGER.info("Transfering bottles to brewing stand");
        brewingStand.setItem(0, getItem(INPUT_SLOT_1).copy());
        this.setItem(INPUT_SLOT_1, ItemStack.EMPTY);

        brewingStand.setItem(1, getItem(INPUT_SLOT_2).copy());
        this.setItem(INPUT_SLOT_2, ItemStack.EMPTY);

        brewingStand.setItem(2, getItem(INPUT_SLOT_3).copy());
        this.setItem(INPUT_SLOT_3, ItemStack.EMPTY);
        
        brewingStand.setChanged();
        this.setChanged();
    }

    public void moveBottlesToNextBrewer(BrewerBlockEntity nextBrewer) {
        PaulsMod.LOGGER.info("Moving bottles to next brewer");
        nextBrewer.setItem(INPUT_SLOT_1, getItem(OUTPUT_SLOT_1).copy());
        this.setItem(OUTPUT_SLOT_1, ItemStack.EMPTY);

        nextBrewer.setItem(INPUT_SLOT_2, getItem(OUTPUT_SLOT_2).copy());
        this.setItem(OUTPUT_SLOT_2, ItemStack.EMPTY);

        nextBrewer.setItem(INPUT_SLOT_3, getItem(OUTPUT_SLOT_3).copy());
        this.setItem(OUTPUT_SLOT_3, ItemStack.EMPTY);
        nextBrewer.setChanged();
        this.setChanged();
    }

    public void moveBottlesToContainer(Container container) {
        //PaulsMod.LOGGER.info("Moving bottles to container");
        ItemStack itemStack = this.getItem(OUTPUT_SLOT_1);
        if (addItemStackToContainer(itemStack, container) ) {
            //PaulsMod.LOGGER.info("Clearing output slot 1");
            this.setItem(OUTPUT_SLOT_1, ItemStack.EMPTY);
        }

        itemStack = this.getItem(OUTPUT_SLOT_2);
        if (addItemStackToContainer(itemStack, container) ) {
            //PaulsMod.LOGGER.info("Clearing output slot 2");
            this.setItem(OUTPUT_SLOT_2, ItemStack.EMPTY);
        }

        itemStack = this.getItem(OUTPUT_SLOT_3);
        if (addItemStackToContainer(itemStack, container) ) {
            //PaulsMod.LOGGER.info("Clearing output slot 3");
            this.setItem(OUTPUT_SLOT_3, ItemStack.EMPTY);
        }
        container.setChanged();
        this.setChanged();
    }

    @Override
    public Object getScreenOpeningData(ServerPlayer serverPlayer) {
        return this.worldPosition;
    }

    //---------------------------------------------------------------------------------



}
