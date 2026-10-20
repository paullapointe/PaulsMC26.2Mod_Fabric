package net.lapointep.paulsmod.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantaneousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class PotentHealingEffect extends InstantaneousMobEffect {
    public PotentHealingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyInstantaneousEffect(ServerLevel level, @Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double health) {
        target.heal(24.0f);
    }
}
