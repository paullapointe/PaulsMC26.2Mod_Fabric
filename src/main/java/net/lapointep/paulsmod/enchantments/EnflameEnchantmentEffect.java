package net.lapointep.paulsmod.enchantments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record EnflameEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<EnflameEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnflameEnchantmentEffect::amount)
    ).apply(instance, EnflameEnchantmentEffect::new));

    public static final int RANGE = 5;
    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof Player) {

            EntityType victimType =  victim.getType();
            if (victim instanceof Player) {
                PaulsMod.LOGGER.info("Player cannot enflame players!");
                return;
            }

            BlockPos position = victim.blockPosition();
            victim.setRemainingFireTicks(level * 3 * 20);
            if (level > 2) {
                List<Entity> otherEnemies = PaulsCommon.getEntitiesInRange(serverLevel, position, RANGE);
                for (Entity otherEntity: otherEnemies) {
                    if (otherEntity instanceof Player) {
                        PaulsMod.LOGGER.info("Player cannot enflame players!");
                    }
                    else {
                        if (otherEntity.getType() == victimType) {
                            otherEntity.setRemainingFireTicks(level * 2 * 20);
                        }
                    }
                }
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}