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

public record LighteningEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {

    public static final int RANGE = 5;
    public static final MapCodec<LighteningEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(LighteningEnchantmentEffect::amount)
    ).apply(instance, LighteningEnchantmentEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof Player) {

            float numStrikes = this.amount.calculate(level);
            EntityType victimType =  victim.getType();
            BlockPos position = victim.blockPosition();
            for (float i = 0; i < numStrikes; i++) {
                PaulsCommon.spawnLightningAt(serverLevel, position, true);
            }

            if (level > 2) {
                List<Entity> otherEnemies = PaulsCommon.getEntitiesInRange(serverLevel, position, RANGE);
                for (Entity otherEntity: otherEnemies) {
                    if (otherEntity.getType() == victimType ) {
                        BlockPos position2 = victim.blockPosition();
                        PaulsCommon.spawnLightningAt(serverLevel, position2, true);
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