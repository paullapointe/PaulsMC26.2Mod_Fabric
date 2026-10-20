package net.lapointep.paulsmod.potions;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotions {

    public static final Holder<Potion> SCULKICIDE_POTION = registerPotion("sculkicide_potion", new Potion("sculkicide_potion"));
    public static final Holder<Potion> NETHERRACK_POTION = registerPotion("netherrack_potion", new Potion("netherrack_potion"));
    public static final Holder<Potion> LAVA_POTION = registerPotion("lava_potion", new Potion("lava_potion"));
    public static final Holder<Potion> WATER_POTION = registerPotion("water_potion", new Potion("water_potion"));
    public static final Holder<Potion> DIRT_POTION = registerPotion("dirt_potion", new Potion("dirt_potion"));
    public static final Holder<Potion> STONE_POTION = registerPotion("stone_potion", new Potion("stone_potion"));

    public static final Holder<Potion> SUGAR_WATER = registerPotion("sugar_water", new Potion("sugar_water"));
    public static final Holder<Potion> LAPIS_COLA = registerPotion("lapis_cola", new Potion("lapis_cola", new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.LAPIS_COLA_EFFECT), 200)));


    private static Holder<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, name), potion);
    }

    public static void registerPotions() {
        PaulsMod.LOGGER.info("Registering potions for " + PaulsMod.MOD_ID);
    }
}
