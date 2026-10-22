package net.lapointep.paulsmod.effect;

import net.lapointep.paulsmod.access.ProtectionDataHolder;
import net.lapointep.paulsmod.util.Destination;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ProtectionEffect extends MobEffect implements RemovableEffect {

    public ProtectionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity.getHealth() <= 1.5f) {
            if (entity instanceof ProtectionDataHolder dataHolder) {
                Destination dest = dataHolder.paulsmod$getProtectionDestination();
                if (dest != null) {
                    dest.teleport(entity);
                    Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(this);
                    entity.removeEffect(holder);
                }
            }
        }
        return true;
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, int amplifier) {
        if (entity instanceof ProtectionDataHolder dataHolder) {
            dataHolder.paulsmod$setProtectionDestination(null);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int k = 80 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }
}
