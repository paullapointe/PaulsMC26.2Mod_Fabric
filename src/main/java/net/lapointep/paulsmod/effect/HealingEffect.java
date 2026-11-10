package net.lapointep.paulsmod.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class HealingEffect extends MobEffect {
    public HealingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (!entity.isInvertedHealAndHarm()) {
            entity.heal(1.0f);
        }

        // 2. If the entity is a player, highlight hostile mobs around them
        if (entity instanceof Player) {
            double range = 16.0D; // Radius in blocks to search for hostile mobs
            AABB searchBox = entity.getBoundingBox().inflate(range);

            // Find all instances of hostile mobs (Monster) within the bounding box
            List<Monster> hostileMobs = level.getEntitiesOfClass(Monster.class, searchBox);

            for (Monster mob : hostileMobs) {
                // Apply glowing for slightly longer than the tick interval
                // so it doesn't flicker between ticks (e.g., 100 ticks / 5 seconds)
                mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0, false, false, true));
            }
        }

        return true;
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
