package net.lapointep.paulsmod.block.entity;


import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.custom.HookBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jspecify.annotations.Nullable;



public class HookBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem {
    public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    public static final int LOW_MAX_COUNT = 20*1;
    public static final int HIGH_MAX_COUNT = 5;

    BETicker lowTicker = new BETicker(LOW_MAX_COUNT);
    BETicker highTicker = new BETicker(HIGH_MAX_COUNT);

    boolean hookIsEmpty = false;
    public HookBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.HOOK, worldPosition, blockState);
    }

    @Override
    public BlockEntity getContainerBlockEntity() {
        return this;
    }

    @Override
    public ItemStack getTheItem() {
        return inventory.getFirst();
    }

    @Override
    public void setTheItem(ItemStack itemStack) {
        inventory.set(0, itemStack.copyWithCount(1));
        setChanged();
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerSingleItem.BlockContainerSingleItem.super.removeItem(slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }


    public static void tick(Level level, BlockPos pos, BlockState state, HookBlockEntity blockEntity) {
        if (level.isClientSide()) { return; }
        if (blockEntity.lowTicker.notReadyToRun()) return;

        boolean powered = state.getValue(HookBlock.POWERED);
        if (powered) {
            if (blockEntity.highTicker.notReadyToRun()) {
                return;
            }
            PaulsMod.LOGGER.info("Hook turning off!");
            level.setBlock(pos, state.setValue(HookBlock.POWERED, false), 3);
        }
        else {
            if (blockEntity.hookIsEmpty == blockEntity.inventory.get(0).isEmpty()) {
                // Only toggle when the state changes
                return;
            }
            PaulsMod.LOGGER.info("Hook Toggling!");
            blockEntity.hookIsEmpty = blockEntity.inventory.get(0).isEmpty();
            level.setBlock(pos, state.setValue(HookBlock.POWERED, true), 3);
        }
    }

    @Override
    public void clearContent() {
        inventory.set(0, ItemStack.EMPTY);
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, this.inventory);
    }

    /* SAVING DATA */
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.inventory);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.inventory.clear();
        ContainerHelper.loadAllItems(input, this.inventory);
    }

    /* MENU METHODS */


    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

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
