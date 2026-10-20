package net.lapointep.paulsmod.block.custom;

import net.lapointep.paulsmod.block.entity.LavaCoreBlockEntity;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LavaCoreBlock extends ProcessorBlock {
    public LavaCoreBlock(Properties properties) {
        super(properties, ProcessorType.LAVA_CORE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LavaCoreBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.LAVA_CORE, LavaCoreBlockEntity::tick);
    }
}
