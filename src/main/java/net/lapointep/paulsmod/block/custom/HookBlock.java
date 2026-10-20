package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.block.entity.HookBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HookBlock extends BaseEntityBlock {

    public static final int MAX_STATE = 2;
    public static final MapCodec<HookBlock> CODEC = simpleCodec(HookBlock::new);
    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, MAX_STATE);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final Property<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public HookBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, 0).setValue(FACING, Direction.NORTH).setValue(POWERED, false));
    }

    // Define pixel bounding boxes (x1, y1, z1, x2, y2, z2) on a 0-16 grid
    private static final VoxelShape SHAPE_NORTH = Block.box(3.0, 1.0, 15.0, 13.0, 13.0, 16.0);
    private static final VoxelShape SHAPE_SOUTH = Block.box(3.0, 1.0, 0.0, 13.0, 13.0, 1.0);
    private static final VoxelShape SHAPE_WEST  = Block.box(15.0, 1.0, 3.0, 16.0, 13.0, 13.0);
    private static final VoxelShape SHAPE_EAST  = Block.box(0.0, 1.0, 3.0, 1.0, 13.0, 13.0);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case SOUTH -> SHAPE_SOUTH;
            case WEST  -> SHAPE_WEST;
            case EAST  -> SHAPE_EAST;
            default    -> SHAPE_NORTH;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Return the same shape as the outline, or return Shapes.empty() if entities can walk through it
        return getShape(state, level, pos, context);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE, FACING, POWERED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof HookBlockEntity hookBlockEntity) {
                ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack stackInHook = hookBlockEntity.getItem(0);

                if (stackInHook.isEmpty() && !stackInHand.isEmpty()) {
                    // Place item
                    PaulsMod.LOGGER.info("Placing item in hook");
                    hookBlockEntity.setItem(0, stackInHand.split(1));
                    hookBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                } else if (!stackInHook.isEmpty()) {
                    // Take item
                    if (stackInHand.isEmpty()) {
                        PaulsMod.LOGGER.info("Taking item from hook");
                        player.setItemInHand(InteractionHand.MAIN_HAND, hookBlockEntity.removeItem(0, 1));
                    } else {
                        ItemStack removedItem = hookBlockEntity.removeItem(0, 1);
                        if (!player.getInventory().add(removedItem)) {
                            player.drop(removedItem, false);
                        }
                    }
                    hookBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof HookBlockEntity hookBlockEntity) {
                Containers.dropContents(level, pos, hookBlockEntity);
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
        return new HookBlockEntity(pos, state);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.HOOK, HookBlockEntity::tick);
    }

}
