package net.lapointep.paulsmod.villager;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.concurrent.CompletableFuture;

public class ModVillagerTradeTags extends FabricTagsProvider<VillagerTrade> {
    public ModVillagerTradeTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.VILLAGER_TRADE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {

        getOrCreateRawBuilder(VillagerTradeTags.CLERIC_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOTTLES.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_NETHERWART.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.CLERIC_LEVEL_2)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOTTLES.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_NETHERWART.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.CLERIC_LEVEL_3)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOTTLES.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_NETHERWART.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.CLERIC_LEVEL_4)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOTTLES.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_NETHERWART.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.CLERIC_LEVEL_5)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOTTLES.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_NETHERWART.identifier()));

        getOrCreateRawBuilder(VillagerTradeTags.FARMER_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PUMPKIN_SEEDS.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_MELON_SEEDS.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FARMER_LEVEL_2)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PUMPKIN_SEEDS.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_MELON_SEEDS.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FARMER_LEVEL_3)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PUMPKIN_SEEDS.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_MELON_SEEDS.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FARMER_LEVEL_4)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PUMPKIN_SEEDS.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_MELON_SEEDS.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FARMER_LEVEL_5)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PUMPKIN_SEEDS.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_MELON_SEEDS.identifier()));

        getOrCreateRawBuilder(VillagerTradeTags.COMMON_SMITH_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_COAL.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_DWARVEN_RING.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_IRON.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_GOLD.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.COMMON_SMITH_LEVEL_2)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_COAL.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_DWARVEN_RING.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_IRON.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_GOLD.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.COMMON_SMITH_LEVEL_3)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_COAL.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_DWARVEN_RING.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_IRON.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_GOLD.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.COMMON_SMITH_LEVEL_4)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_COAL.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_DWARVEN_RING.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_IRON.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_GOLD.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.COMMON_SMITH_LEVEL_5)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_COAL.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_DWARVEN_RING.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_IRON.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_GOLD.identifier()));

        getOrCreateRawBuilder(VillagerTradeTags.SHEPHERD_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_WOOL.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.SHEPHERD_LEVEL_2)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_WOOL.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.SHEPHERD_LEVEL_3)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_WOOL.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.SHEPHERD_LEVEL_4)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_WOOL.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.SHEPHERD_LEVEL_5)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_WOOL.identifier()));

        getOrCreateRawBuilder(VillagerTradeTags.LIBRARIAN_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_ENCHANTED_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PAPER.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.LIBRARIAN_LEVEL_2)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_ENCHANTED_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PAPER.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.LIBRARIAN_LEVEL_3)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_ENCHANTED_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PAPER.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.LIBRARIAN_LEVEL_4)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_ENCHANTED_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PAPER.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.LIBRARIAN_LEVEL_5)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_ENCHANTED_BOOK.identifier()))
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_PAPER.identifier()));

        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_ROOT.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_2)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_ROOT.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_3)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_ROOT.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_4)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_ROOT.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_5)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_ROOT.identifier()));

        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_1)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_BOAT.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_2)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_BOAT.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_3)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_BOAT.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_4)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_BOAT.identifier()));
        getOrCreateRawBuilder(VillagerTradeTags.FISHERMAN_LEVEL_5)
                .add(TagEntry.element(ModVillagerTrades.FLORIN_FOR_FISHING_BOAT.identifier()));

    }
}
