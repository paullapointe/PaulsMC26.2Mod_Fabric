package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.block.entity.SpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SpawnerBlock extends BaseEntityBlock {

    public static final int MAX_STATE = 2;
    public static final MapCodec<SpawnerBlock> CODEC = simpleCodec(SpawnerBlock::new);
    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, MAX_STATE);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public SpawnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, 0).setValue(POWERED, Boolean.FALSE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE, POWERED);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable net.minecraft.world.level.redstone.Orientation orientation, boolean notify) {
        if (!level.isClientSide()) {
            boolean isPowered = level.hasNeighborSignal(pos);
            if (state.getValue(POWERED) != isPowered) {
                level.setBlock(pos, state.setValue(POWERED, isPowered), 3);
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack stackInSpawner = spawnerBlockEntity.getItem(0);

                if (stackInSpawner.isEmpty() && !stackInHand.isEmpty()) {
                    // Place item
                    PaulsMod.LOGGER.info("Placing item in spawner");
                    spawnerBlockEntity.setItem(0, stackInHand.split(1));
                    spawnerBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                } else if (!stackInSpawner.isEmpty()) {
                    // Take item
                    if (stackInHand.isEmpty()) {
                        PaulsMod.LOGGER.info("Taking item from spawner");
                        player.setItemInHand(InteractionHand.MAIN_HAND, spawnerBlockEntity.removeItem(0, 1));
                    } else {
                        ItemStack removedItem = spawnerBlockEntity.removeItem(0, 1);
                        if (!player.getInventory().add(removedItem)) {
                            player.drop(removedItem, false);
                        }
                    }
                    spawnerBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                Containers.dropContents(level, pos, spawnerBlockEntity);
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
        return new SpawnerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.SPAWNER, SpawnerBlockEntity::tick);
    }
}
