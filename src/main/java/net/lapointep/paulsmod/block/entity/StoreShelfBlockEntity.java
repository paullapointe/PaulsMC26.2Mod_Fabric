package net.lapointep.paulsmod.block.entity;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.lapointep.paulsmod.block.custom.StoreShelfBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.lapointep.paulsmod.menu.StoreShelfMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import static net.lapointep.paulsmod.block.custom.StoreShelfBlock.*;
import static net.lapointep.paulsmod.block.entity.ModBlockEntities.STORESHELF;


public class StoreShelfBlockEntity extends BlockEntity implements WorldlyContainer, ExtendedMenuProvider, Nameable {

    private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    protected final ContainerData data;
    private String placerName = "";

    public StoreShelfBlockEntity(BlockPos pos, BlockState state) {
        super(STORESHELF, pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> StoreShelfBlockEntity.this.getCost();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> StoreShelfBlockEntity.this.setCost(value);
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2, 3, 4, 5};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return false;
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

    public void setPlacerName(String placerName) {
        this.placerName = placerName;
        this.setChanged();
    }

    public String getPlacerName() {
        return placerName;
    }

    public void setCost(int cost) {
        if (this.level != null) {
            BlockState state = this.getBlockState();
            if (state.hasProperty(StoreShelfBlock.COST)) {
                this.level.setBlock(this.worldPosition, state.setValue(StoreShelfBlock.COST, cost), 3);
            }
        }
        this.setChanged();
    }

    public int getCost() {
        BlockState state = this.getBlockState();
        if (state.hasProperty(StoreShelfBlock.COST)) {
            return state.getValue(StoreShelfBlock.COST);
        }
        return 0;
    }

    public void cycleCost(Level level, BlockPos pos, BlockState state) {
        int currentCost = state.getValue(StoreShelfBlock.COST);
        int newState = (currentCost + 1) % (MAX_COST + 1);
        level.setBlock(pos, state.setValue(StoreShelfBlock.COST, newState), 3);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items, true);
        output.putString("placer_name", placerName);
        output.putInt("cost", getCost());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items.clear();
        ContainerHelper.loadAllItems(input, this.items);
        this.placerName = input.getStringOr("placer_name", "");
        setCost(input.getIntOr("cost", 0));
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.storeshelf");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new StoreShelfMenu(containerId, playerInventory, this, this.data);
    }

    @Override
    public Object getScreenOpeningData(ServerPlayer serverPlayer) {
        return this.worldPosition;
    }

    //---------------------------------------------------------------------------------

    /* BLOCK ENTITY SYNC METHODS */
    @Override
    public void setChanged() {
        super.setChanged();
        if(level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

}
