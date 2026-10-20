package net.lapointep.paulsmod.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class GuideEntity extends Entity {
    private int lifeTime = 0;
    private static final int MAX_LIFETIME = 40; // Despawn after 2 seconds

    public GuideEntity(EntityType<? extends GuideEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true; // Pass through blocks
    }

    public void setGuideDirection(Vec3 direction, double speed) {
        this.setDeltaMovement(direction.scale(speed));
    }

    @Override
    public void tick() {
        super.tick();

        // Move entity forward
        this.move(MoverType.SELF, this.getDeltaMovement());

        // Spawn visual particle trail on client
        if (this.level().isClientSide()) {
            this.level().addParticle(
                    ParticleTypes.END_ROD,
                    this.getX(), this.getY(), this.getZ(),
                    0.0, 0.0, 0.0
            );
        }

        // Handle temporary lifetime
        this.lifeTime++;
        if (this.lifeTime >= MAX_LIFETIME) {
            this.discard(); // Safely despawn entity
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

}