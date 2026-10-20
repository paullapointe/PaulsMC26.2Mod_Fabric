package net.lapointep.paulsmod.block.entity;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.lapointep.paulsmod.item.custom.SortingWandItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.lapointep.paulsmod.menu.SorterMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static net.lapointep.paulsmod.block.entity.ModBlockEntities.SORTER;
import static net.lapointep.paulsmod.util.PaulsCommon.getItemType;
import static net.lapointep.paulsmod.util.PaulsCommon.tryInsertItem;

public class SorterBlockEntity extends BlockEntity implements WorldlyContainer, ExtendedMenuProvider, Nameable {
    private final NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    public static final int RANGE = 4;
    protected final ContainerData data;
    private int craftingProgress = 0;
    public static final int MAX_CRAFTING_PROGRESS = 20;

    private final Map<PaulsCommon.ItemTypes, List<BlockPos>> itemTypePositions = new EnumMap<>(PaulsCommon.ItemTypes.class);

    int counter = 0;
    public static final int MAX_COUNTER = 20*10;
    BETicker ticker = new BETicker(MAX_COUNTER);

    public SorterBlockEntity(BlockPos pos, BlockState state) {
        super(SORTER, pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SorterBlockEntity.this.craftingProgress;
                    case 1 -> SorterBlockEntity.MAX_CRAFTING_PROGRESS;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SorterBlockEntity.this.craftingProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] slots = new int[this.items.size()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return true;
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
        output.putInt("craftingProgress", craftingProgress);
        ContainerHelper.saveAllItems(output, this.items, true);

        for (PaulsCommon.ItemTypes type : PaulsCommon.ItemTypes.values()) {
            List<BlockPos> positions = itemTypePositions.getOrDefault(type, List.of());
            output.putInt("itemType_" + type.name() + "_size", positions.size());
            for (int i = 0; i < positions.size(); i++) {
                BlockPos pos = positions.get(i);
                output.putInt("itemType_" + type.name() + "_" + i + "_x", pos.getX());
                output.putInt("itemType_" + type.name() + "_" + i + "_y", pos.getY());
                output.putInt("itemType_" + type.name() + "_" + i + "_z", pos.getZ());
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.craftingProgress = input.getIntOr("craftingProgress", 0);
        this.items.clear();
        ContainerHelper.loadAllItems(input, this.items);

        itemTypePositions.clear();
        for (PaulsCommon.ItemTypes type : PaulsCommon.ItemTypes.values()) {
            int size = input.getIntOr("itemType_" + type.name() + "_size", 0);
            if (size > 0) {
                List<BlockPos> positions = new java.util.ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    int x = input.getIntOr("itemType_" + type.name() + "_" + i + "_x", 0);
                    int y = input.getIntOr("itemType_" + type.name() + "_" + i + "_y", 0);
                    int z = input.getIntOr("itemType_" + type.name() + "_" + i + "_z", 0);
                    positions.add(new BlockPos(x, y, z));
                }
                itemTypePositions.put(type, positions);
            }
        }
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.sorter");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SorterMenu(containerId, playerInventory, this, this.data);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SorterBlockEntity blockEntity) {
        if (level == null || level.isClientSide()) return;

        if (blockEntity.ticker.notReadyToRun()) { return; }

        //PaulsMod.LOGGER.info("Sorter Ticking {} {}/{}", pos, blockEntity.craftingProgress, MAX_CRAFTING_PROGRESS);

        blockEntity.pickupItems();
        blockEntity.sortItems();

           // setChanged(level, pos, state);

    }

    @Override
    public Object getScreenOpeningData(ServerPlayer serverPlayer) {
        return this.worldPosition;
    }

    //---------------------------------------------------------------------------------

    private void pickupItems() {
        if (this.level == null || this.level.isClientSide()) return;

        List<ItemEntity> itemEntities = PaulsCommon.getItemsInArea(this.level, this.worldPosition, RANGE);

        for (ItemEntity itemEntity : itemEntities) {
            if (!itemEntity.isAlive()) continue;

            ItemStack stack = itemEntity.getItem();
            ItemStack remainder = stack.copy();

            // Pass 1: Try to merge into existing slots with matching items
            for (int i = 0; i < this.getContainerSize(); i++) {
                if (remainder.isEmpty()) break;

                ItemStack slotStack = this.getItem(i);
                if (!slotStack.isEmpty() && ItemStack.isSameItemSameComponents(remainder, slotStack)) {
                    // Calculate how much space is left in the slot
                    int space = slotStack.getMaxStackSize() - slotStack.getCount();
                    int toInsert = Math.min(remainder.getCount(), space);

                    if (toInsert > 0) {
                        slotStack.grow(toInsert);
                        remainder.shrink(toInsert);
                        this.setChanged();
                    }
                }
            }

            // Pass 2: If there are still items left, try to put them in empty slots
            for (int i = 0; i < this.getContainerSize(); i++) {
                if (remainder.isEmpty()) break;

                ItemStack slotStack = this.getItem(i);
                if (slotStack.isEmpty()) {
                    // Respect the item's max stack size (usually 64 or 99 depending on the item/version)
                    int maxInsert = Math.min(remainder.getCount(), remainder.getMaxStackSize());
                    ItemStack toInsert = remainder.split(maxInsert);

                    // this.setItem handles placing it in the NonNullList and calls setChanged()
                    this.setItem(i, toInsert);
                }
            }

            // Step 3: Update or discard the ItemEntity in the world
            if (remainder.getCount() != stack.getCount()) {
                if (remainder.isEmpty()) {
                    itemEntity.discard(); // Completely absorbed
                } else {
                    itemEntity.setItem(remainder); // Partially absorbed, update entity
                }
            }
        }
    }

    public void addBlockPosForItemType(PaulsCommon.ItemTypes type, BlockPos pos) {
        if (type == PaulsCommon.ItemTypes.All) {
            itemTypePositions.clear();
        }
        else {
            itemTypePositions.remove(PaulsCommon.ItemTypes.All);
        }

        List<BlockPos> positions = itemTypePositions.computeIfAbsent(type, k -> new java.util.ArrayList<>());
        if (positions.contains(pos)) {
            positions.remove(pos);
        } else {
            positions.add(pos);
        }

        PaulsMod.LOGGER.info("Sorter positions updated. Current mapping:");
        itemTypePositions.forEach((itemType, posList) -> {
            if (!posList.isEmpty()) {
                PaulsMod.LOGGER.info("  {}: {}", itemType, posList.stream().map(BlockPos::toShortString).toList());
            }
        });
    }

    private void sortItems() {
        if (this.items.isEmpty()) return;

        for (int i = 0; i < this.getContainerSize(); i++) {

            ItemStack stack = this.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof SortingWandItem) continue;
            
            //PaulsMod.LOGGER.info("Sorter checking {} {}", stack, this.items);
            if (stack.isEmpty() || stack.getItem() instanceof SortingWandItem) continue;

            // First check to see if the "ALL" position is set.
            PaulsCommon.ItemTypes itemType = null;
            List<BlockPos> positions = itemTypePositions.get(PaulsCommon.ItemTypes.All);
            if (positions == null || positions.isEmpty()) {
                //PaulsMod.LOGGER.info("Sorter has no positions for ALL");
                // If not, check for the position of the specific type
                itemType = getItemType(stack);
                positions = itemTypePositions.get(itemType);
                if (positions == null || positions.isEmpty()) {
                    PaulsMod.LOGGER.warn("Sorter has no positions for {} {}", stack, itemType);
                    // if no position, just leave this item and move to the next one
                    continue;
                }
            }

            if (positions == null || positions.isEmpty()) {
                PaulsMod.LOGGER.warn("Sorter has no positions for {} {}/{}", stack, this.items, positions);
                continue;
            }

            // Try to place the item in one of the available chests
            for (int j = 0; j < positions.size(); j++) {
                BlockPos targetPos = positions.get(j);
                //PaulsMod.LOGGER.info("Sorter checking position {} {}/{}", targetPos, stack, this.items);
                BlockEntity targetEntity = level.getBlockEntity(targetPos);
                if (targetEntity != null) {
                   // PaulsMod.LOGGER.info("Sorter attempting to insert to {} {}/{}", targetPos, stack, this.items);

                    if (targetEntity instanceof Container container ) {
                        if (tryInsertItem(level, targetPos, null, stack)) {
                            //PaulsMod.LOGGER.info("Sorter inserted to {} {}/{}", targetPos, stack, this.items);
                        }
                        else {
                            PaulsMod.LOGGER.warn("Sorter failed to insert to {} {}/{}", targetPos, stack, this.items);
                        }
                    }
                    else {
                        PaulsMod.LOGGER.warn("Sorter target is not a container");
                        // Remove the position from the list if it's not a container'
                        if (itemType != null) {
                            addBlockPosForItemType(itemType, targetPos);
                        }
                    }

                }
            }
        }
    }
}
