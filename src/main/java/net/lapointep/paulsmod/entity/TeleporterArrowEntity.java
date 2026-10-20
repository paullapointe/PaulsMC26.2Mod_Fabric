package net.lapointep.paulsmod.entity;

import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TeleporterArrowEntity extends AbstractArrow {
    public TeleporterArrowEntity(EntityType<? extends TeleporterArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public TeleporterArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, @Nullable ItemStack weapon) {
        super(ModEntities.TELEPORTER_ARROW, shooter, level, pickupItemStack, weapon);
    }

    public TeleporterArrowEntity(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack weapon) {
        super(ModEntities.TELEPORTER_ARROW, x, y, z, level, pickupItemStack, weapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        teleportEntity(entityHitResult.getEntity(), entityHitResult.getLocation());
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        teleportEntity(this.getOwner(), blockHitResult.getLocation());
    }

    private void teleportEntity(Entity target, Vec3 impactPos) {
        if (target instanceof LivingEntity livingTarget && !this.level().isClientSide()) {
            if (livingTarget.level() == this.level() && (!(livingTarget instanceof Player player) || !player.isSpectator())) {
                Vec3 targetPos = impactPos;

                ItemStack stack = this.getPickupItemStackOrigin();
                if (stack != null) {
                    CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                    if (customData != null) {
                        CompoundTag nbt = customData.copyTag();
                        if (nbt.contains("x") && nbt.contains("y") && nbt.contains("z")) {
                            targetPos = new Vec3(nbt.getIntOr("x", 0) + 0.5, nbt.getIntOr("y", 0) + 1.0, nbt.getIntOr("z", 0) + 0.5);
                        }
                    }
                }

                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL, livingTarget.getX(), livingTarget.getY() + livingTarget.getBbHeight() / 2.0, livingTarget.getZ(), 32, 0.5, 0.5, 0.5, 0.1);
                    serverLevel.sendParticles(ParticleTypes.PORTAL, targetPos.x, targetPos.y, targetPos.z, 32, 0.5, 0.5, 0.5, 0.1);
                }

                if (livingTarget instanceof ServerPlayer serverPlayer) {
                    serverPlayer.teleportTo(targetPos.x, targetPos.y, targetPos.z);
                } else {
                    livingTarget.teleportTo(targetPos.x, targetPos.y, targetPos.z);
                }
                livingTarget.resetFallDistance();
                this.discard();
            }
        } else if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.TELEPORTER_ARROW);
    }
}
