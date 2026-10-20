package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.block.entity.StickyNoteBlockEntity;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.item.custom.StickyNoteBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StickyNoteBlock extends BaseEntityBlock {
    public static final MapCodec<StickyNoteBlock> CODEC = simpleCodec(StickyNoteBlock::new);
    public static final Property<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public StickyNoteBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
        builder.add(FACING);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StickyNoteBlockEntity(pos, state);
    }


    @Override
    public ItemStack getCloneItemStack(net.minecraft.world.level.LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, includeData);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof StickyNoteBlockEntity stickyNoteBlockEntity) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("note_text", stickyNoteBlockEntity.getNoteText());
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        }
        return stack;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof StickyNoteBlockEntity stickynoteBlockEntity) {
                CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                if (customData != null) {
                    CompoundTag nbt = customData.copyTag();
                    if (nbt.contains("note_text")) {
                        stickynoteBlockEntity.setNoteText(nbt.getStringOr("note_text", ""));
                    }
                }
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof StickyNoteBlockEntity noteEntity) {
                String noteText = noteEntity.getNoteText();
                ItemStack stack = new ItemStack(ModItems.STICKYNOTE);

                // Access the instance method setNoteText on StickyNoteBlockItem
                if (stack.getItem() instanceof StickyNoteBlockItem noteItem) {
                    noteItem.setNoteText(stack, noteText);
                }

                // Give item to player or drop at feet if inventory is full
                if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }

                // Remove block without dropping normal loot table items
                level.destroyBlock(pos, false);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
