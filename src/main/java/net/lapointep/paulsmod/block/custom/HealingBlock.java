package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.block.entity.HealingBlockEntity;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

public class HealingBlock extends BaseEntityBlock {
    public static final int MAX_STATE = 2;
    public static final MapCodec<HealingBlock> CODEC = simpleCodec(HealingBlock::new);
    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, MAX_STATE);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public HealingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, 0));
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
        return createTickerHelper(type, ModBlockEntities.HEALING, HealingBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HealingBlockEntity(pos, state);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        // Always check if running on the server side to avoid duplicate execution
        if (!level.isClientSide()) {
            if (level instanceof ServerLevel serverLevel) {
                if (entity instanceof LivingEntity livingEntity) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof HealingBlockEntity healingBlockEntity) {
                        if (livingEntity.isOnFire()) {
                            livingEntity.clearFire();
                        }

                        if (livingEntity.hasEffect(MobEffects.POISON)) {
                            livingEntity.removeEffect(MobEffects.POISON);
                        }

                        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
                            healingBlockEntity.heal(livingEntity);
                            double x = pos.getX() + 0.5;
                            double y = pos.getY() + 1.2; // Offset above the block
                            double z = pos.getZ() + 0.5;

                            // sendParticles(particle, x, y, z, count, deltaX, deltaY, deltaZ, speed)
                            serverLevel.sendParticles(
                                    ParticleTypes.HAPPY_VILLAGER, // Or ParticleTypes.SOUL_FIRE_FLAME
                                    x, y, z,             // Target position
                                    4,                  // Number of particles
                                    0.2, 0.2, 0.2,       // Spread offset box radius (X, Y, Z)
                                    0.05                 // Particle velocity/speed multiplier
                            );
                        }
                    }
                }
            }
        }

        super.stepOn(level, pos, state, entity);
    }
}
