package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.block.entity.TeleporterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class TeleporterBlock extends BaseEntityBlock {
    public static final MapCodec<TeleporterBlock> CODEC = simpleCodec(TeleporterBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public TeleporterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeleporterBlockEntity(pos, state);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide() && state.getValue(POWERED) && entity instanceof Player player) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TeleporterBlockEntity teleporterBlockEntity) {
                BlockPos targetPos = teleporterBlockEntity.getTargetPos();
                String targetDim = teleporterBlockEntity.getTargetDimension();
                
                if (targetPos != null && !targetPos.equals(BlockPos.ZERO)) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    ResourceKey<Level> targetDimKey = ResourceKey.create(Registries.DIMENSION, Identifier.parse(targetDim));
                    ServerLevel targetLevel = serverLevel.getServer().getLevel(targetDimKey);
                    
                    if (targetLevel != null) {
                        player.teleportTo(targetLevel, targetPos.getX() + 0.5, targetPos.getY() + 1.0, targetPos.getZ() + 0.5, java.util.Set.of(), player.getYRot(), player.getXRot(), false);
                    }
                }
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TeleporterBlockEntity teleporterBlockEntity) {
                CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                if (customData != null) {
                    CompoundTag nbt = customData.copyTag();
                    if (nbt.contains("x") && nbt.contains("y") && nbt.contains("z") && nbt.contains("dim")) {
                        BlockPos targetPos = new BlockPos(nbt.getIntOr("x", 0), nbt.getIntOr("y", 0), nbt.getIntOr("z", 0));
                        String targetDim = nbt.getStringOr("dim", "minecraft:overworld");
                        teleporterBlockEntity.setTarget(targetPos, targetDim);
                    }
                }
            }
        }
    }
}
