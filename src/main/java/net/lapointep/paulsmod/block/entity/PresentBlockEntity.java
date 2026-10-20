package net.lapointep.paulsmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class PresentBlockEntity extends BlockEntity {
    private CompoundTag wrappedItemTag = new CompoundTag();
    private boolean removedByPlayer = false;

    public PresentBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PRESENT, pos, state);
    }

    public void setRemovedByPlayer(boolean removedByPlayer) {
        this.removedByPlayer = removedByPlayer;
    }

    public boolean isRemovedByPlayer() {
        return removedByPlayer;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        if (wrappedItemTag != null && !wrappedItemTag.isEmpty()) {
            tag.put("WrappedItem", wrappedItemTag);
        }
        return tag;
    }

    public void setWrappedItemTag(CompoundTag tag) {
        this.wrappedItemTag = tag.copy();
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public CompoundTag getWrappedItemTag() {
        return this.wrappedItemTag;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (wrappedItemTag != null && !wrappedItemTag.isEmpty()) {
            output.putString("WrappedItem", wrappedItemTag.toString());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        String tagStr = input.getStringOr("WrappedItem", "");
        if (!tagStr.isEmpty()) {
            this.wrappedItemTag = net.minecraft.nbt.CompoundTag.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, 
                    net.minecraft.nbt.StringTag.valueOf(tagStr)).result().orElse(new CompoundTag());
        }
    }
}
