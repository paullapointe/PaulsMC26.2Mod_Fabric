package net.lapointep.paulsmod.effect;

import net.minecraft.world.entity.LivingEntity;

public interface RemovableEffect {
    void onEffectRemoved(LivingEntity entity, int amplifier);
}
