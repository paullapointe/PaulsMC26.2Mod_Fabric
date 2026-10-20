package net.lapointep.paulsmod.effect;

import net.lapointep.paulsmod.entity.ModEntities;
import net.lapointep.paulsmod.entity.TorchEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class TorchEffect extends MobEffect {
    public TorchEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (!entity.hasEffect(MobEffects.GLOWING)) {
            TorchEntity torch = new TorchEntity(ModEntities.TORCH_ENTITY, level);
            torch.setPos(entity.getX(), entity.getY() + 0.5, entity.getZ());
            torch.setMaxLifeTime(30 * 20 * (amplifier + 1));
            level.addFreshEntity(torch);
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 30 * 20 * (amplifier + 1)));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
