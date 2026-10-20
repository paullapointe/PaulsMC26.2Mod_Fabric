package net.lapointep.paulsmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockWithCyclableState {
    void cycleState(Level level, BlockPos pos, BlockState state);
}
