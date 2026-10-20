package net.lapointep.paulsmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BandageEffect extends MobEffect {
    public BandageEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    public static final float HEALING_POINTS = 2.5f;
    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
        float missingHealth = entity.getMaxHealth() - entity.getHealth();
        if (missingHealth > 0) {
            float healingAmount = Math.min(missingHealth, HEALING_POINTS);
            entity.setHealth(entity.getHealth() + healingAmount);
        }
    }
}
