package net.lapointep.paulsmod.block.entity;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.lapointep.paulsmod.PaulsMod;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.lapointep.paulsmod.menu.ProcessorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class ProcessorBlockEntity extends BlockEntity implements WorldlyContainer, ExtendedMenuProvider, Nameable {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    protected final ContainerData data;
    private int craftingProgress = 0;
    public static final int MAX_CRAFTING_PROGRESS = 20;

    int requiredInputItemSize = 1;
    int counter = 0;
    public static final int MAX_COUNTER = 2;
    private ItemStack outputItemStack = ItemStack.EMPTY;

    public ProcessorBlockEntity(BlockEntityType T, BlockPos pos, BlockState state, int requiredInputItemSize) {
        super(T, pos, state);
        this.requiredInputItemSize = requiredInputItemSize;
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ProcessorBlockEntity.this.craftingProgress;
                    case 1 -> ProcessorBlockEntity.MAX_CRAFTING_PROGRESS;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ProcessorBlockEntity.this.craftingProgress = value;
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
        if (side == Direction.DOWN) {
            return new int[]{OUTPUT_SLOT};
        } else {
            return new int[]{INPUT_SLOT};
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return slot == INPUT_SLOT;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot == OUTPUT_SLOT;
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


    public ItemStack displayItem() {
        if (!this.items.get(OUTPUT_SLOT).isEmpty()) {
            return this.items.get(OUTPUT_SLOT);
        }

        if (!this.items.get(INPUT_SLOT).isEmpty()) {
            return this.items.get(INPUT_SLOT);
        }

        return null;
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
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.craftingProgress = input.getIntOr("craftingProgress", 0);
        this.items.clear();
        ContainerHelper.loadAllItems(input, this.items);
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.lava_core");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ProcessorMenu(containerId, playerInventory, this, this.data);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ProcessorBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        if (blockEntity.counter <= MAX_COUNTER) {
            blockEntity.counter++;
            return;
        }
        blockEntity.counter = 0;

        if (blockEntity.isCrafting()) {
            //PaulsMod.LOGGER.info("Generic Crafting! {} {}/{}", pos, blockEntity.craftingProgress, MAX_CRAFTING_PROGRESS);
            blockEntity.increaseCraftingProgress();
            setChanged(level, pos, state);
        }
    }

    public int getCraftingProgress() {
        return this.craftingProgress;
    }

    public void increaseCraftingProgress() {
        this.craftingProgress++;
        if (this.craftingProgress >= MAX_CRAFTING_PROGRESS) {
            this.craftingProgress = 0;
            craft(this);
        }
    }

    @Override
    public Object getScreenOpeningData(ServerPlayer serverPlayer) {
        return this.worldPosition;
    }

    public boolean craft(Container container) {
        ItemStack inputStack = container.getItem(INPUT_SLOT);
        ItemStack outputStack = container.getItem(OUTPUT_SLOT);
        ItemStack resultItem = this.getOutputItem();
        if (resultItem == null) { return false; }

        // 1. Check if input slot has an item
        if (inputStack.isEmpty()) {
            return false;
        }

        if (inputStack.getCount() < this.requiredInputItemSize) {
            return false;
        }

        // 2. Validate if output slot can accept the result
        if (!canAcceptOutput(outputStack, resultItem)) {
            return false;
        }

        // 3. Decrement input stack safely
        inputStack.shrink(this.requiredInputItemSize);
        if (inputStack.isEmpty()) {
            container.setItem(INPUT_SLOT, ItemStack.EMPTY);
        }

        // 4. Increment/Insert into output stack
        if (outputStack.isEmpty()) {
            container.setItem(OUTPUT_SLOT, resultItem.copy());
        } else {
            outputStack.grow(resultItem.getCount());
        }

        // 5. Mark container changed so chunk saves state
        container.setChanged();
        return true;
    }

    /**
     * Checks if the output slot can accept the crafting result.
     */
    private static boolean canAcceptOutput(ItemStack currentOutput, ItemStack resultItem) {
        if (currentOutput.isEmpty()) {
            return true;
        }

        // Checks item identity AND Data Components (ComponentMap)
        if (!ItemStack.isSameItemSameComponents(currentOutput, resultItem)) {
            return false;
        }

        // Respect both slot max stack limits and item max count limits
        int maxAllowed = Math.min(currentOutput.getMaxStackSize(), resultItem.getMaxStackSize());
        return (currentOutput.getCount() + resultItem.getCount()) <= maxAllowed;
    }

    public boolean isCrafting() {
        /*
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        if (inputStack.isEmpty()) {
            return false;
        }

        if (inputStack.getItem() == Items.BUCKET) {
            this.increaseCraftingProgress();
        }
*/
        // TODO: Add more checks here
        //PaulsMod.LOGGER.info("Generic isCrafting");
        return false;
    }

    public ItemStack getOutputItem() {
        return this.outputItemStack;
    }
    public void setOutputItem(Item outputItem) {
        this.outputItemStack = new ItemStack(outputItem);
    }
    public void setOutputItem(ItemStack outputItemStack) {
        this.outputItemStack = outputItemStack;
    }

    public void setRequiredInputItemSize(int size) {
        this.requiredInputItemSize = size;
    }
}
