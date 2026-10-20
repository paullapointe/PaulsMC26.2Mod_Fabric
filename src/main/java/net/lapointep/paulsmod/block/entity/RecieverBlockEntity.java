package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.custom.HealingBlock;
import net.lapointep.paulsmod.block.custom.RecieverBlock;
import net.lapointep.paulsmod.block.custom.LavaCoreBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

import static net.minecraft.world.inventory.AbstractFurnaceMenu.FUEL_SLOT;

public class RecieverBlockEntity extends BlockEntity {
    private BlockPos targetPos = BlockPos.ZERO;
    private String targetDimension = "minecraft:overworld";
    public static final int MAX_COUNT = 20*3;
    BETicker ticker = new BETicker(MAX_COUNT);
    public RecieverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RECIEVER, pos, state);
    }

    public void setTarget(BlockPos pos, String dimension) {
        this.targetPos = pos;
        this.targetDimension = dimension;
        this.setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RecieverBlockEntity blockEntity) {
        if (level.isClientSide()) { return; }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }

        setState(redstonePowerAt(targetPos));
    }

    public void setState(boolean state) {
        BlockState blockState = this.getBlockState();
        level.setBlock(this.worldPosition, blockState.setValue(RecieverBlock.POWERED, state), 3);
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

    public boolean redstonePowerAt(BlockPos pos) {
        if ( level.hasNeighborSignal(pos)) { return true; }

        // Check if emitting redstone power
        for (Direction direction : Direction.values()) {
            if (level.getSignal(pos, direction) > 0) {
                return true;
            }
        }
        return false;
    }
}
