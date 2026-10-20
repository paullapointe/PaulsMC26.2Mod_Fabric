package net.lapointep.paulsmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.UUID;

public class LockingDoorBlockEntity extends BlockEntity {
    private UUID ownerUuid;

    public LockingDoorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LOCKING_DOOR, pos, state);
    }

    public void setOwner(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
        this.setChanged();
    }

    public UUID getOwner() {
        return ownerUuid;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (ownerUuid != null) {
            output.putString("owner_uuid", ownerUuid.toString());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        String uuidStr = input.getStringOr("owner_uuid", "");
        if (!uuidStr.isEmpty()) {
            try {
                this.ownerUuid = UUID.fromString(uuidStr);
            } catch (IllegalArgumentException e) {
                this.ownerUuid = null;
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if (ownerUuid != null) {
            tag.putString("owner_uuid", ownerUuid.toString());
        }
        return tag;
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }
}
