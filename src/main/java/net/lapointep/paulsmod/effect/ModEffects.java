package net.lapointep.paulsmod.effect;

import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class ModEffects {

    public static final MobEffect LAPIS_COLA_EFFECT = register("lapis_cola_effect", new LapisColaEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));
    public static final MobEffect TORCH_EFFECT = register("torch_effect", new TorchEffect(MobEffectCategory.BENEFICIAL, 0xFFFF00));

    public static final MobEffect FREEZE = register("freeze", new FreezeEffect(MobEffectCategory.HARMFUL, 0xDDFFFF));
    public static final MobEffect FLYING = register("flying", new FlyingEffect(MobEffectCategory.BENEFICIAL, 0x88CCFF));
    public static final MobEffect VERY_TALL = register("very_tall", new VeryTallEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
    public static final MobEffect HEALING = register("healing", new HealingEffect(MobEffectCategory.BENEFICIAL, 0xFF0000));
    public static final MobEffect WAFFLES = register("waffles", new WafflesEffect(MobEffectCategory.BENEFICIAL, 0xFFFF00));
  //  public static final MobEffect POTENT_HEAL = register("potent_heal", new PotentHealingEffect(MobEffectCategory.BENEFICIAL, 0xFF0000));
    public static final MobEffect BANDAGE = register("bandage", new BandageEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
    public static final MobEffect INCREASED_HEALTH = register("increased_health", new IncreasedHealthEffect(MobEffectCategory.BENEFICIAL, 0xFF0000));
    public static final MobEffect INCREASED_SPEED = register("increased_speed", new IncreasedSpeedEffect(MobEffectCategory.BENEFICIAL, 0x00FF00));
  //  public static final MobEffect FABULOUS_HAIR = register("fabulous_hair", new FabulousHairEffect(MobEffectCategory.BENEFICIAL, 0xFF69B4));
  //  public static final MobEffect INVISIBILITY = register("invisibility", new InvisibilityEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));

    public static final MobEffect SHERA_ARMOUR = register("shera_armour", new ArmorEffect(MobEffectCategory.BENEFICIAL, 0xFFD700, 
            () -> new ItemStack(Items.GOLDEN_HELMET), () -> new ItemStack(Items.GOLDEN_CHESTPLATE), () -> new ItemStack(Items.GOLDEN_LEGGINGS), () -> new ItemStack(Items.GOLDEN_BOOTS)));
    public static final MobEffect HEMAN_ARMOUR = register("heman_armour", new ArmorEffect(MobEffectCategory.BENEFICIAL, 0x8B4513,
            () -> new ItemStack(Items.IRON_HELMET), () -> new ItemStack(Items.IRON_CHESTPLATE), () -> new ItemStack(Items.IRON_LEGGINGS), () -> new ItemStack(Items.IRON_BOOTS)));
    public static final MobEffect IRONMAN_ARMOUR = register("ironman_armour", new ArmorEffect(MobEffectCategory.BENEFICIAL, 0xFF0000,
            () -> new ItemStack(Items.NETHERITE_HELMET), () -> new ItemStack(Items.NETHERITE_CHESTPLATE), () -> new ItemStack(Items.NETHERITE_LEGGINGS), () -> new ItemStack(Items.NETHERITE_BOOTS)));
    public static final MobEffect LOKI_ARMOUR = register("loki_armour", new ArmorEffect(MobEffectCategory.BENEFICIAL, 0x008000,
            () -> new ItemStack(Items.CHAINMAIL_HELMET), () -> new ItemStack(Items.CHAINMAIL_CHESTPLATE), () -> new ItemStack(Items.CHAINMAIL_LEGGINGS), () -> new ItemStack(Items.CHAINMAIL_BOOTS)));
    public static final MobEffect MOONKNIGHT_ARMOUR = register("moonknight_armour", new ArmorEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF,
            () -> new ItemStack(Items.DIAMOND_HELMET), () -> new ItemStack(Items.DIAMOND_CHESTPLATE), () -> new ItemStack(Items.DIAMOND_LEGGINGS), () -> new ItemStack(Items.DIAMOND_BOOTS)));

    private static MobEffect register(String name, MobEffect effect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, name), effect);
    }

    public static void registerModEffects() {
        PaulsMod.LOGGER.info("Registering Mod Effects for " + PaulsMod.MOD_ID);
    }
}
