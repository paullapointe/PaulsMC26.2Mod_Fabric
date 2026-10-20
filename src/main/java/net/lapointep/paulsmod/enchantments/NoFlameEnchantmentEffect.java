package net.lapointep.paulsmod.enchantments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lapointep.paulsmod.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public record NoFlameEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<NoFlameEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(NoFlameEnchantmentEffect::amount)
    ).apply(instance, NoFlameEnchantmentEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        target.setRemainingFireTicks(0);
        if (level > 1) {
            int radius = 10;
            BlockPos blockPos = BlockPos.containing(pos);
            for (BlockPos currentPos : BlockPos.betweenClosed(blockPos.offset(-radius, -radius, -radius), blockPos.offset(radius, radius, radius))) {
                if (serverLevel.getBlockState(currentPos).getBlock() instanceof BaseFireBlock) {
                    serverLevel.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        if (level > 2) {
            int radius = 5;
            BlockPos blockPos = BlockPos.containing(pos);
            for (BlockPos currentPos : BlockPos.betweenClosed(blockPos.offset(-radius, -radius, -radius), blockPos.offset(radius, radius, radius))) {
                if (serverLevel.getFluidState(currentPos).is(net.minecraft.tags.FluidTags.LAVA)) {
                    serverLevel.setBlock(currentPos, Blocks.BASALT.defaultBlockState(), 3);
                }
            }
        }

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}