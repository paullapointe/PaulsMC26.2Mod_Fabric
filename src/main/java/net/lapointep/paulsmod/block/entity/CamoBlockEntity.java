package net.lapointep.paulsmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public class CamoBlockEntity extends BlockEntity {
    private BlockState camoState = Blocks.AIR.defaultBlockState();
    private Direction camoDirection = null;

    public CamoBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAMO, pos, state);
    }

    public BlockState getCamoState() {
        if (this.level != null && (camoState == null || camoState.isAir())) {
            updateCamoState();
        }
        return camoState != null ? camoState : Blocks.AIR.defaultBlockState();
    }

    public Direction getCamoDirection() {
        if (this.level != null && camoDirection == null) {
            updateCamoState();
        }
        return camoDirection;
    }

    private void updateCamoState() {
        if (this.level == null) return;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = this.worldPosition.relative(direction);
            BlockState neighborState = this.level.getBlockState(neighborPos);
            if (neighborState.isSolid() && !neighborState.is(this.getBlockState().getBlock())) {
                this.camoState = neighborState;
                this.camoDirection = direction;
                this.setChanged();
                if (!this.level.isClientSide()) {
                    this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
                }
                return;
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (camoState != null && !camoState.isAir()) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(camoState.getBlock());
            output.putString("camo_block", id.toString());
        }
        if (camoDirection != null) {
            output.putString("camo_direction", camoDirection.getName());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        String idStr = input.getStringOr("camo_block", "minecraft:air");
        Identifier id = Identifier.tryParse(idStr);
        if (id != null) {
            Optional<Block> block = BuiltInRegistries.BLOCK.getOptional(id);
            block.ifPresent(value -> this.camoState = value.defaultBlockState());
        }
        String dirStr = input.getStringOr("camo_direction", "");
        if (!dirStr.isEmpty()) {
            this.camoDirection = Direction.byName(dirStr);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if (camoState != null) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(camoState.getBlock());
            tag.putString("camo_block", id.toString());
        }
        if (camoDirection != null) {
            tag.putString("camo_direction", camoDirection.getName());
        }
        return tag;
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }
}
