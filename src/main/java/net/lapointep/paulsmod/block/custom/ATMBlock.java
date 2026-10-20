package net.lapointep.paulsmod.block.custom;

import net.lapointep.paulsmod.block.entity.ATMBlockEntity;
import net.lapointep.paulsmod.block.entity.MixerBlockEntity;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ATMBlock extends ProcessorBlock {
    public ATMBlock(Properties properties) {
        super(properties, ProcessorType.ATM);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ATMBlockEntity(pos, state);
    }

}
