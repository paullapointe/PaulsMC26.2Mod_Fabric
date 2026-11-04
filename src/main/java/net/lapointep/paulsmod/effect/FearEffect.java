package net.lapointep.paulsmod.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FearEffect extends MobEffect {
    public FearEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        Player nearestPlayer = level.getNearestPlayer(entity, 12.0);
        if (nearestPlayer != null) {
            if (entity instanceof PathfinderMob pathfinderMob) {
                Vec3 awayPos = DefaultRandomPos.getPosAway(pathfinderMob, 16, 7, nearestPlayer.position());
                if (awayPos != null) {
                    pathfinderMob.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, 1.5D);
                }
            } else {
                // For non-pathfinder mobs, just push them away
                Vec3 awayVec = entity.position().subtract(nearestPlayer.position()).normalize().scale(0.2 * (amplifier + 1));
                entity.setDeltaMovement(entity.getDeltaMovement().add(awayVec));
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }
}
