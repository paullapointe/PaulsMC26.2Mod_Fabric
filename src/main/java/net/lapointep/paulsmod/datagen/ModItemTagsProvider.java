package net.lapointep.paulsmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(ItemTags.ARROWS).add(ModItems.TELEPORTER_ARROW.builtInRegistryHolder().key());

        tag(TagKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("paulsmod", "staff")))
                .add(ModItems.STAFF.builtInRegistryHolder().key());

        tag(TagKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("paulsmod", "am_staff")))
                .add(ModItems.AM_STAFF.builtInRegistryHolder().key());

        tag(TagKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("paulsmod", "di_staff")))
                .add(ModItems.DI_STAFF.builtInRegistryHolder().key());

        tag(TagKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("paulsmod", "em_staff")))
                .add(ModItems.EM_STAFF.builtInRegistryHolder().key());

        tag(TagKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("paulsmod", "end_staff")))
                .add(ModItems.END_STAFF.builtInRegistryHolder().key());

    }
}
