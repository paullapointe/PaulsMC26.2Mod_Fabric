package net.lapointep.paulsmod.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class LapisColaEffect extends MobEffect {
    public LapisColaEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static final int MAX_EXPERIENCE_POINTS = 8;
    public static final float MAX_DAMAGE_PER_TICK = 0.25f;

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            RandomSource random = level.getRandom();

            int experiencePoints = random.nextInt(MAX_EXPERIENCE_POINTS);
            float damage = random.nextFloat() * MAX_DAMAGE_PER_TICK;

            player.giveExperiencePoints(experiencePoints);
            player.heal(-damage);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        if (duration % 25 == 0) {
            return true;
        }
        return false;
    }
}
