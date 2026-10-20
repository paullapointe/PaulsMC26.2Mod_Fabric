package net.lapointep.paulsmod.enchantments;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.effect.HealingEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class ModEnchantmentEffects {
    public static final MapCodec<LighteningEnchantmentEffect> LIGHTENING_EFFECT = register("lightening_effect", LighteningEnchantmentEffect.CODEC);
    public static final MapCodec<EnflameEnchantmentEffect> ENFLAME_EFFECT = register("enflame_effect", EnflameEnchantmentEffect.CODEC);
    public static final MapCodec<BuryEnchantmentEffect> BURY_EFFECT = register("bury_effect", BuryEnchantmentEffect.CODEC);
    public static final MapCodec<DawnEnchantmentEffect> DAWN_EFFECT = register("dawn_effect", DawnEnchantmentEffect.CODEC);
    public static final MapCodec<AriseEnchantmentEffect> ARISE_EFFECT = register("arise_effect", AriseEnchantmentEffect.CODEC);
    public static final MapCodec<FreezeEnchantmentEffect> FREEZE_EFFECT = register("freeze_effect", FreezeEnchantmentEffect.CODEC);

    public static final MapCodec<HealEnchantmentEffect> HEALING_EFFECT = register("heal_effect",HealEnchantmentEffect.CODEC);
    public static final MapCodec<BreadEnchantmentEffect> BREAD_EFFECT = register("bread_effect",BreadEnchantmentEffect.CODEC);
    public static final MapCodec<TorchEnchantmentEffect> TORCH_EFFECT = register("torch_effect",TorchEnchantmentEffect.CODEC);
    public static final MapCodec<NoFlameEnchantmentEffect> NOFLAME_EFFECT = register("noflame_effect",NoFlameEnchantmentEffect.CODEC);

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(
                BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
                Identifier.fromNamespaceAndPath("paulsmod", id),
                codec
        );
    }

    public static void registerModEnchantmentEffects() {
        // Call ModEnchantmentEffects.registerModEnchantmentEffects() inside your main ModInitializer's onInitialize() method.
    }
}