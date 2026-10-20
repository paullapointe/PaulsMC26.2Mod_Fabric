package net.lapointep.paulsmod.block.custom;

import net.lapointep.paulsmod.block.entity.BrickMakerBlockEntity;
import net.lapointep.paulsmod.block.entity.MixerBlockEntity;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BrickMakerBlock extends ProcessorBlock {
    public BrickMakerBlock(Properties properties) {
        super(properties, ProcessorType.BRICK_MAKER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BrickMakerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BRICKMAKER, BrickMakerBlockEntity::tick);
    }
}
