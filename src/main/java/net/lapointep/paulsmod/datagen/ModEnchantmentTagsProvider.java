package net.lapointep.paulsmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.lapointep.paulsmod.enchantments.ModEnchantmentEffects;
import net.lapointep.paulsmod.enchantments.ModEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagsProvider extends EnchantmentTagsProvider {

    public ModEnchantmentTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(EnchantmentTags.NON_TREASURE)
                .add(ModEnchantments.LIGHTENING)
                .add(ModEnchantments.ENFLAME)
                .add(ModEnchantments.BURY)
                .add(ModEnchantments.DAWN)
                .add(ModEnchantments.ARISE)
                .add(ModEnchantments.FREEZE)
                .add(ModEnchantments.HEAL)
                .add(ModEnchantments.BREAD)
                .add(ModEnchantments.TORCH)
                .add(ModEnchantments.NOFLAME);
    }
}
