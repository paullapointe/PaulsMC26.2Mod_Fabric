package net.lapointep.paulsmod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TorchEntity extends Entity {
    private int lifeTime = 0;
    private int maxLifeTime = 600; // Default 30 seconds (30 * 20 ticks)
    private BlockPos lastLightPos = null;

    public TorchEntity(EntityType<? extends TorchEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setGlowingTag(true);
    }

    public void setMaxLifeTime(int maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            if (this.random.nextInt(3) == 0) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            this.updateLight();
            this.lifeTime++;
            if (this.lifeTime >= maxLifeTime) {
                this.discard();
            }
        }
    }

    private void updateLight() {
        BlockPos currentPos = this.blockPosition();
        if (!currentPos.equals(this.lastLightPos)) {
            this.removeLight();
            if (this.level().getBlockState(currentPos).isAir()) {
                BlockState lightState = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15);
                this.level().setBlock(currentPos, lightState, 3);
                this.lastLightPos = currentPos.immutable();
            } else {
                this.lastLightPos = null;
            }
        }
    }

    private void removeLight() {
        if (this.lastLightPos != null) {
            if (this.level().getBlockState(this.lastLightPos).is(Blocks.LIGHT)) {
                this.level().setBlock(this.lastLightPos, Blocks.AIR.defaultBlockState(), 3);
            }
            this.lastLightPos = null;
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide()) {
            this.removeLight();
        }
        super.remove(reason);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }
}
