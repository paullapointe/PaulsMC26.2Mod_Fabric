package net.lapointep.paulsmod.enchantments;

import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

import java.util.function.Consumer;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> LIGHTENING = createKey("lightening");
    public static final ResourceKey<Enchantment> ENFLAME = createKey("enflame");
    public static final ResourceKey<Enchantment> BURY = createKey("bury");
    public static final ResourceKey<Enchantment> DAWN = createKey("dawn");
    public static final ResourceKey<Enchantment> ARISE = createKey("arise");
    public static final ResourceKey<Enchantment> FREEZE = createKey("freeze");
    public static final ResourceKey<Enchantment> HEAL = createKey("heal");
    public static final ResourceKey<Enchantment> BREAD = createKey("bread");
    public static final ResourceKey<Enchantment> TORCH = createKey("torch");
    public static final ResourceKey<Enchantment> NOFLAME = createKey("noflame");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        context.register(LIGHTENING, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new LighteningEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(LIGHTENING.identifier()));

        context.register(ENFLAME, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new EnflameEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(ENFLAME.identifier()));

        context.register(BURY, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new BuryEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(BURY.identifier()));

        context.register(DAWN, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 1,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new DawnEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(DAWN.identifier()));

        context.register(ARISE, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE), // primaryItems - what shows in enchanting table
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new AriseEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(ARISE.identifier()));

        context.register(FREEZE, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new FreezeEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(FREEZE.identifier()));

        context.register(HEAL, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new HealEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(HEAL.identifier()));

        context.register(BREAD, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new BreadEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(BREAD.identifier()));

        context.register(TORCH, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new TorchEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(TORCH.identifier()));

        context.register(NOFLAME, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                5, 3,
                Enchantment.dynamicCost(1, 11),  // minCost: 1 + 11 * (level - 1)
                Enchantment.dynamicCost(12, 11), // maxCost: 12 + 11 * (level - 1)
                2,
                EquipmentSlotGroup.MAINHAND
        )).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new NoFlameEnchantmentEffect(LevelBasedValue.perLevel(1.0f))
        ).build(NOFLAME.identifier()));
    }

    private static ResourceKey<Enchantment> createKey(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, name));
    }
}
