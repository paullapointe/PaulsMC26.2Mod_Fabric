package net.lapointep.paulsmod.enchantments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

public record AriseEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<AriseEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(AriseEnchantmentEffect::amount)
    ).apply(instance, AriseEnchantmentEffect::new));

    public static final int RANGE = 5;
    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof Player) {

            float numStrikes = this.amount.calculate(level);
            EntityType victimType =  victim.getType();
            BlockPos position = victim.blockPosition();
            for (float i = 0; i < numStrikes; i++) {
                victim.teleportTo(position.getX(), position.getY()+(level*3), position.getZ());
            }

            if (level > 2) {
                List<Entity> otherEnemies = PaulsCommon.getEntitiesInRange(serverLevel, position, RANGE);
                for (Entity otherEntity: otherEnemies) {
                    if (otherEntity.getType() == victimType ) {
                        BlockPos position2 = victim.blockPosition();
                        otherEntity.teleportTo(position2.getX(), position2.getY()+(level*2), position2.getZ());
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