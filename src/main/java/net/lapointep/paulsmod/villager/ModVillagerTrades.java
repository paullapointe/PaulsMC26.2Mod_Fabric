package net.lapointep.paulsmod.villager;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.enchantments.ModEnchantments;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;

import java.util.List;
import java.util.Optional;

public class ModVillagerTrades {

    public static final ResourceKey<VillagerTrade> FLORIN_FOR_MELON_SEEDS = createKey("farmer/1/florin_melon_seeds");
    public static final ResourceKey<VillagerTrade> FLORIN_FOR_PUMPKIN_SEEDS = createKey("farmer/1/florin_pumpkin_seeds");

    public static final ResourceKey<VillagerTrade> FLORIN_FOR_IRON = createKey("blacksmith/1/florin_iron_ingots");
    public static final ResourceKey<VillagerTrade> FLORIN_FOR_GOLD = createKey("blacksmith/1/florin_gold_ingots");
    public static final ResourceKey<VillagerTrade> FLORIN_FOR_COAL = createKey("blacksmith/1/florin_coal");
    public static final ResourceKey<VillagerTrade> FLORIN_FOR_DWARVEN_RING = createKey("blacksmith/1/florin_ring");

    public static final ResourceKey<VillagerTrade> FLORIN_FOR_PAPER = createKey("librarian/1/florin_paper");
    public static final ResourceKey<VillagerTrade> FLORIN_FOR_BOOK = createKey("librarian/1/book");

    public static final ResourceKey<VillagerTrade> ENCHANTED_BOOK_FOR_FLORIN = createKey("librarian/1/book");

    public static final ResourceKey<VillagerTrade> FLORIN_FOR_FISHING_ROOT = createKey("fisherman/1/fishing_rod");
    public static final ResourceKey<VillagerTrade> FLORIN_FOR_FISHING_BOAT = createKey("fisherman/1/boat");

    public static final ResourceKey<VillagerTrade> FLORIN_FOR_WOOL = createKey("shepherd/1/wool");

    public static final ResourceKey<VillagerTrade> FLORIN_FOR_BOTTLES = createKey("cleric/1/bottles");
    public static final ResourceKey<VillagerTrade> FLORIN_FOR_NETHERWART = createKey("cleric/1/netherwart");

    public static final ResourceKey<VillagerTrade> FLORIN_FOR_ENCHANTED_BOOK = createKey("librarian/1/fluorite_enchanted_book");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        var items = context.lookup(Registries.ITEM);
        var enchantments = context.lookup(Registries.ENCHANTMENT);

        context.register(FLORIN_FOR_BOTTLES,
                new VillagerTrade(new TradeCost(Items.GLASS_BOTTLE, 16),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_NETHERWART,
                new VillagerTrade(new TradeCost(Items.NETHER_WART, 8),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_MELON_SEEDS,
                new VillagerTrade(new TradeCost(Items.MELON_SEEDS, 8),
                new ItemStackTemplate(ModItems.FLORIN, 1),
                99, 1, 0.05f,
                Optional.empty(), List.of()));

        context.register(FLORIN_FOR_PUMPKIN_SEEDS,
                new VillagerTrade(new TradeCost(Items.PUMPKIN_SEEDS, 12),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_IRON,
                new VillagerTrade(new TradeCost(Items.IRON_INGOT, 6),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_GOLD,
                new VillagerTrade(new TradeCost(Items.GOLD_INGOT, 4),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_COAL,
                new VillagerTrade(new TradeCost(Items.COAL, 32),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_DWARVEN_RING,
                new VillagerTrade(new TradeCost(ModItems.DWARVEN_RING, 1),
                        new ItemStackTemplate(ModItems.FLORIN, 64),
                        5, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_PAPER,
                new VillagerTrade(new TradeCost(Items.PAPER, 32),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_BOOK,
                new VillagerTrade(new TradeCost(Items.BOOK, 16),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_FISHING_ROOT,
                new VillagerTrade(new TradeCost(Items.FISHING_ROD, 1),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        8, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_FISHING_BOAT,
                new VillagerTrade(new TradeCost(Items.ACACIA_BOAT, 2),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        8, 1, 0.05f,
                        Optional.empty(), List.of()));

        context.register(FLORIN_FOR_WOOL,
                new VillagerTrade(new TradeCost(Items.WOOL.white(), 12),
                        new ItemStackTemplate(ModItems.FLORIN, 1),
                        99, 1, 0.05f,
                        Optional.empty(), List.of()));


        context.register(FLORIN_FOR_ENCHANTED_BOOK, new VillagerTrade(
                new TradeCost(ModItems.FLORIN, 32),
                new ItemStackTemplate(Items.ENCHANTED_BOOK),
                12, 6, 0.05f,
                Optional.empty(),
                VillagerTrades.enchantedBook(items,
                        HolderSet.direct(enchantments.getOrThrow(ModEnchantments.LIGHTENING),
                                enchantments.getOrThrow(ModEnchantments.ARISE),
                                enchantments.getOrThrow(ModEnchantments.BREAD),
                                enchantments.getOrThrow(ModEnchantments.BURY),
                                enchantments.getOrThrow(ModEnchantments.DAWN),
                                enchantments.getOrThrow(ModEnchantments.FREEZE),
                                enchantments.getOrThrow(ModEnchantments.HEAL),
                                enchantments.getOrThrow(ModEnchantments.NOFLAME),
                                enchantments.getOrThrow(ModEnchantments.TORCH),
                                enchantments.getOrThrow(ModEnchantments.ENFLAME)
                        )
                )));



    }

    private static ResourceKey<VillagerTrade> createKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, name));
    }
}
