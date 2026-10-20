package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.BlockWithCyclableState;
import net.lapointep.paulsmod.block.custom.ClockBlock;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


import static net.lapointep.paulsmod.block.custom.ClockBlock.MAX_STATE;

public class ClockBlockEntity extends BlockEntity implements BlockWithCyclableState {
    final int MAX_COUNTER = 20*5;
    final BETicker ticker = new BETicker(MAX_COUNTER);

    public ClockBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CLOCK, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ClockBlockEntity blockEntity) {
           if (level.isClientSide()) {
            return;
        }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }
        int hour12Time = PaulsCommon.get12HourTime(level);
        //PaulsMod.LOGGER.info("Clock Ticking {} {}", pos, hour12Time);
        level.setBlock(pos, state.setValue(ClockBlock.STATE, hour12Time-1), 3);
    }

    public void cycleState(Level level, BlockPos pos, BlockState state) {
        int currentState = state.getValue(ClockBlock.STATE);
        int newState = (currentState + 1) % (MAX_STATE);
        level.setBlock(pos, state.setValue(ClockBlock.STATE, newState), 3);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
    }
}
