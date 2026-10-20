package net.lapointep.paulsmod.mixin;

import net.lapointep.paulsmod.effect.RemovableEffect;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.potions.ModPotions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "onEffectsRemoved", at = @At("HEAD"))
    private void paulsmod$onEffectsRemoved(java.util.Collection<MobEffectInstance> effects, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        for (MobEffectInstance instance : effects) {
            if (instance.getEffect().value() instanceof RemovableEffect removable) {
                removable.onEffectRemoved((LivingEntity) (Object) this, instance.getAmplifier());
            }
        }
    }

    @Inject(method = "completeUsingItem", at = @At("HEAD"))
    private void paulsmod$completeUsingItem(CallbackInfo ci) {
        /*
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide()) return;
        
        ItemStack stack = entity.getUseItem();
        if (stack.is(Items.POTION)) {
            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

        }

         */
    }
}
