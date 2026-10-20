package net.lapointep.paulsmod.datagen;

import net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModMobLootTableProvider  {

    public static void modifyLootTables(ResourceKey<LootTable> lootTableKey, FabricLootTableBuilder builder, LootTableSource source, HolderLookup.Provider provider) {

            if (lootTableKey.identifier().equals(Identifier.fromNamespaceAndPath("minecraft", "entities/skeleton"))) {
                LootPool.Builder lootPoolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(0.005f))
                        .when(LootItemRandomChanceCondition.randomChance(1f))
                        .add(LootItem.lootTableItem(ModItems.DWARVEN_RING))
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)).build());
                builder.pool(lootPoolBuilder.build());
            }
    }

}