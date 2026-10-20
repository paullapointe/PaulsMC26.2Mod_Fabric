package net.lapointep.paulsmod.enchantments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record BuryEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<BuryEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(BuryEnchantmentEffect::amount)
    ).apply(instance, BuryEnchantmentEffect::new));

    public static final int RANGE = 5;
    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {

        if (target instanceof LivingEntity victim && context.owner() instanceof Player) {

            if (victim == context.owner()) {
                PaulsMod.LOGGER.info("Player cannot bury themselves!");
                return;
            }

            EntityType victimType =  victim.getType();
            boolean undead = victim.isInvertedHealAndHarm();
            if (!undead) {
                PaulsMod.LOGGER.info("Player cannot bury the living!");
                return;
            }

            BlockPos position = victim.blockPosition();
            if (level == 1) {
                victim.teleportTo(position.getX(), position.getY()-level, position.getZ());
            }

            if (level > 1) {
                List<Entity> otherEnemies = PaulsCommon.getEntitiesInRange(serverLevel, position, RANGE);
                for (Entity otherEntity: otherEnemies) {
                    if (otherEntity.getType() == victimType ) {
                        BlockPos position2 = victim.blockPosition();
                        otherEntity.teleportTo(position2.getX(), position2.getY() - level, position2.getZ());
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