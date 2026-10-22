package net.lapointep.paulsmod.enchantments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record HealEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<HealEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(HealEnchantmentEffect::amount)
    ).apply(instance, HealEnchantmentEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof Player) {
            PaulsMod.LOGGER.info("Healing " + victim.getName().getString() + " for " + this.amount.calculate(level*level));
            victim.heal((float) this.amount.calculate(level*level));
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}