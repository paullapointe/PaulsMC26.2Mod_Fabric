package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.block.entity.UnstableBlockEntity;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class UnstableBlock extends BaseEntityBlock {
    public static final int MAX_STATE = 5;
    public static final MapCodec<UnstableBlock> CODEC = simpleCodec(UnstableBlock::new);
    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, MAX_STATE);

    public enum UnstableType {
        Netherrack, Lava, Water, Stone, Dirt;
    }

    UnstableType blockType;
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public UnstableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, 0));
        blockType = UnstableType.Netherrack;
    }

    public UnstableBlock(UnstableType type, Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, 0));
        blockType = type;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (blockType == UnstableType.Netherrack) {
            return createTickerHelper(type, ModBlockEntities.UNSTABLENETHERRACK, UnstableBlockEntity::tick);
        }
        else if (blockType == UnstableType.Lava) {
            return createTickerHelper(type, ModBlockEntities.UNSTABLELAVA, UnstableBlockEntity::tick);
        }
        else if (blockType == UnstableType.Water) {
            return createTickerHelper(type, ModBlockEntities.UNSTABLEWATER, UnstableBlockEntity::tick);
        }
        else if (blockType == UnstableType.Dirt) {
            return createTickerHelper(type, ModBlockEntities.UNSTABLEDIRT, UnstableBlockEntity::tick);
        }
        else if (blockType == UnstableType.Stone) {
            return createTickerHelper(type, ModBlockEntities.UNSTABLESTONE, UnstableBlockEntity::tick);
        }

        return createTickerHelper(type, ModBlockEntities.UNSTABLENETHERRACK, UnstableBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (blockType == UnstableType.Netherrack) {
            return new UnstableBlockEntity(ModBlockEntities.UNSTABLENETHERRACK, pos, state);
        }
        else if (blockType == UnstableType.Lava) {
            return new UnstableBlockEntity(ModBlockEntities.UNSTABLELAVA, pos, state);
        }
        else if (blockType == UnstableType.Water) {
            return new UnstableBlockEntity(ModBlockEntities.UNSTABLEWATER, pos, state);
        }
        else if (blockType == UnstableType.Stone) {
            return new UnstableBlockEntity(ModBlockEntities.UNSTABLESTONE, pos, state);
        }
        else if (blockType == UnstableType.Dirt) {
            return new UnstableBlockEntity(ModBlockEntities.UNSTABLEDIRT, pos, state);
        }
        return new UnstableBlockEntity(ModBlockEntities.UNSTABLENETHERRACK, pos, state);
    }
}
