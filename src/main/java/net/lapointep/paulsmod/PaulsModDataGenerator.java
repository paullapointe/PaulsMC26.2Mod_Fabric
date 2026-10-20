package net.lapointep.paulsmod;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.lapointep.paulsmod.datagen.*;
import net.lapointep.paulsmod.enchantments.ModEnchantments;
import net.lapointep.paulsmod.villager.ModTradeSets;
import net.lapointep.paulsmod.villager.ModVillagerTradeTags;
import net.lapointep.paulsmod.villager.ModVillagerTrades;
import net.lapointep.paulsmod.worldgen.ModConfiguredFeatures;
import net.lapointep.paulsmod.worldgen.ModPlacedFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class PaulsModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModRegistryDataProvider::new);
        pack.addProvider(ModVillagerTradeTags::new);
	}

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.VILLAGER_TRADE, ModVillagerTrades::bootstrap);
        registryBuilder.add(Registries.TRADE_SET, ModTradeSets::bootstrap);

        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
        registryBuilder.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);
    }
}
