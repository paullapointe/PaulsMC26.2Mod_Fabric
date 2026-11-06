package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.block.entity.ProcessorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import static net.lapointep.paulsmod.block.entity.ModBlockEntities.MIXER;

public class ProcessorBlock extends BaseEntityBlock {
    public static final MapCodec<ProcessorBlock> CODEC = simpleCodec(ProcessorBlock::new);

    public enum ProcessorType {
        LAVA_CORE,
        MIXER,
        BRICK_MAKER,
        DRYER,
        ATM
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public ProcessorBlock(Properties properties) {
        super(properties);
    }

    public ProcessorBlock(Properties properties, ProcessorType type) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ProcessorBlockEntity) {
                player.openMenu((ProcessorBlockEntity) blockEntity);
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ProcessorBlockEntity) {
                net.minecraft.world.Containers.dropContents(level, pos, (ProcessorBlockEntity)blockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ProcessorBlockEntity(MIXER, pos, state, 1);
    }
}
