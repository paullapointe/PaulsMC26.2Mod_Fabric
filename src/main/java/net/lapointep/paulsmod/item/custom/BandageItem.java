package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.effect.ModEffects;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BandageItem extends Item {
    public BandageItem(Properties properties) {
        super(properties);
    }
    public static final int DURATION = 120*20;

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.BANDAGE);
        ItemStack stack = player.getItemInHand(hand);
        // Check if the player already has the Bandage effect
        if (player.hasEffect(holder)) {
            return InteractionResult.FAIL; // Prevents item usage if effect is active
        }

        player.startUsingItem(hand);
        PaulsMod.LOGGER.info("BandageItem used by player");

        if (!level.isClientSide()) {
            player.addEffect(new MobEffectInstance(holder, DURATION, 0));
            stack.consume(1, player);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand hand) {
        Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.BANDAGE);
        // Check if the player already has the Bandage effect
        if (interactionTarget.hasEffect(holder)) {
            return InteractionResult.FAIL; // Prevents item usage if effect is active
        }

        player.startUsingItem(hand);
        Level level = player.level();
        PaulsMod.LOGGER.info("BandageItem used on entity %s", interactionTarget.getName().getString());
        if (!level.isClientSide()) {
            if (interactionTarget instanceof LivingEntity entity) {
                entity.addEffect(new MobEffectInstance(holder, DURATION, 0));
                stack.consume(1, player);
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 32;
    }
}
