package net.lapointep.paulsmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TeleporterBlockEntity extends BlockEntity {
    private BlockPos targetPos = BlockPos.ZERO;
    private String targetDimension = "minecraft:overworld";

    public TeleporterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TELEPORTER, pos, state);
    }

    public void setTarget(BlockPos pos, String dimension) {
        this.targetPos = pos;
        this.targetDimension = dimension;
        this.setChanged();
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    public String getTargetDimension() {
        return targetDimension;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("targetX", targetPos.getX());
        output.putInt("targetY", targetPos.getY());
        output.putInt("targetZ", targetPos.getZ());
        output.putString("targetDim", targetDimension);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        int x = input.getIntOr("targetX", 0);
        int y = input.getIntOr("targetY", 0);
        int z = input.getIntOr("targetZ", 0);
        this.targetPos = new BlockPos(x, y, z);
        this.targetDimension = input.getStringOr("targetDim", "minecraft:overworld");
    }
}
