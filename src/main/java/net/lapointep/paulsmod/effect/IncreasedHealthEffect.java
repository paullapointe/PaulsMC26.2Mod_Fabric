package net.lapointep.paulsmod.effect;

import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class IncreasedHealthEffect extends MobEffect {
    public IncreasedHealthEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MAX_HEALTH, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "increased_health"), 16.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
