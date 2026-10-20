package net.lapointep.paulsmod.effect;

import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class IncreasedSpeedEffect extends MobEffect {
    public IncreasedSpeedEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "increased_speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
