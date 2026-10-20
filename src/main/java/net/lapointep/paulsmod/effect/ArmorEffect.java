package net.lapointep.paulsmod.effect;

import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class ArmorEffect extends MobEffect implements RemovableEffect {
    private final Supplier<ItemStack> helmet;
    private final Supplier<ItemStack> chest;
    private final Supplier<ItemStack> legs;
    private final Supplier<ItemStack> boots;

    public ArmorEffect(MobEffectCategory category, int color, Supplier<ItemStack> helmet, Supplier<ItemStack> chest, Supplier<ItemStack> legs, Supplier<ItemStack> boots) {
        super(category, color);
        this.helmet = helmet;
        this.chest = chest;
        this.legs = legs;
        this.boots = boots;
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayer player) {
          //  PaulsCommon.toggleTemporaryArmor(player, true, helmet.get(), chest.get(), legs.get(), boots.get());
        }
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayer player) {
           // PaulsCommon.toggleTemporaryArmor(player, false, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        }
    }
}
