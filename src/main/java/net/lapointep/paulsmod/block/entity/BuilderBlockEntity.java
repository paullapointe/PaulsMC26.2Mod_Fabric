package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.lapointep.paulsmod.block.custom.BuilderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.lapointep.paulsmod.menu.BuilderMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class BuilderBlockEntity extends BlockEntity implements WorldlyContainer, ExtendedMenuProvider, Nameable {
    public static final int INPUT_SLOT = 0;

    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    BETicker beTicker = new BETicker(20 * 2);
    int requiredInputItemSize = 1;
    public static final int MAX_COUNTER = 2;

    public BuilderBlockEntity(BlockEntityType T, BlockPos pos, BlockState state, int requiredInputItemSize) {
        super(T, pos, state);
        this.requiredInputItemSize = requiredInputItemSize;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return slot == 0;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot == 1;
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
    public ItemStack removeItem(int slot, int count) {
        return ContainerHelper.takeItem(this.items, slot);
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
        return Component.translatable("block.paulsmod.builder");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BuilderMenu(containerId, playerInventory, this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BuilderBlockEntity blockEntity) {
        if (level.isClientSide()) { return; }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (beTicker.notReadyToRun()) { return; }
        PaulsMod.LOGGER.info("Builder Ticking {}", pos);
        if (!level.hasNeighborSignal(pos)) {
            return;
        }
        PaulsMod.LOGGER.info("Builder Ticking {}: Redstone on", pos);
        ItemStack stack = this.items.get(INPUT_SLOT);
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) { return; }
        PaulsMod.LOGGER.info("Builder Ticking {} blocks: ", stack.getCount());
        Direction direction = state.getValue(BuilderBlock.FACING);
        BlockPos targetPos = pos.relative(direction);

        while (!stack.isEmpty()) {
            BlockState targetState = level.getBlockState(targetPos);
            if (targetState.isAir() || targetState.is(net.minecraft.world.level.block.Blocks.WATER) || targetState.is(net.minecraft.world.level.block.Blocks.LAVA)) {
                level.setBlock(targetPos, blockItem.getBlock().defaultBlockState(), 3);
                stack.shrink(1);
                targetPos = targetPos.relative(direction);
                PaulsMod.LOGGER.info("Builder BlockEntity: Placing block");
            } else {
                PaulsMod.LOGGER.info("Builder BlockEntity: Not placing block");
                break;
            }
        }
        this.setChanged();
    }

    @Override
    public Object getScreenOpeningData(ServerPlayer serverPlayer) {
        return this.worldPosition;
    }
}
