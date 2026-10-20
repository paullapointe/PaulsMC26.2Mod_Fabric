package net.lapointep.paulsmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.lapointep.paulsmod.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.BlockItemTagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }


    @Override
    protected void addTags(HolderLookup.Provider registries) {

        tag(BlockTags.FIRE).add(ModBlocks.BARRIER.builtInRegistryHolder().key());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.LAVA_CORE.builtInRegistryHolder().key())
                .add(ModBlocks.TELEPORTER.builtInRegistryHolder().key())
                .add(ModBlocks.FUELER.builtInRegistryHolder().key())
                .add(ModBlocks.RECIEVER.builtInRegistryHolder().key())
                .add(ModBlocks.HELPER.builtInRegistryHolder().key())
                .add(ModBlocks.OSC.builtInRegistryHolder().key())
                .add(ModBlocks.STICKYNOTE.builtInRegistryHolder().key())
                .add(ModBlocks.HEARTH.builtInRegistryHolder().key())
                .add(ModBlocks.CLOCK.builtInRegistryHolder().key())
                .add(ModBlocks.ENDERLAMP.builtInRegistryHolder().key())
                .add(ModBlocks.HEALING.builtInRegistryHolder().key())
                .add(ModBlocks.CENSUS.builtInRegistryHolder().key())
                .add(ModBlocks.ENDERLAMP.builtInRegistryHolder().key())
                .add(ModBlocks.HEALING.builtInRegistryHolder().key())
                .add(ModBlocks.STAIN_GLASS.builtInRegistryHolder().key())

               // .add(ModBlocks.PILLAR)
                .add(ModBlocks.STATUE.builtInRegistryHolder().key())
                .add(ModBlocks.PEDESTAL.builtInRegistryHolder().key())
                .add(ModBlocks.PLATE.builtInRegistryHolder().key())
                .add(ModBlocks.HOOK.builtInRegistryHolder().key())
                .add(ModBlocks.ALTER.builtInRegistryHolder().key())
                .add(ModBlocks.RIVER_ROCK.builtInRegistryHolder().key())
                .add(ModBlocks.RIVER_ROCK_MORTAR.builtInRegistryHolder().key())
                .add(ModBlocks.LOCKING_DOOR.builtInRegistryHolder().key())

                .add(ModBlocks.ROUND_FRAMED_WINDOW.builtInRegistryHolder().key())
                .add(ModBlocks.FRAMED_WINDOW.builtInRegistryHolder().key())

                .add(ModBlocks.LATTICE_ARCH.builtInRegistryHolder().key())

                .add(ModBlocks.BIG_DS_PILLAR.builtInRegistryHolder().key())
                .add(ModBlocks.BIG_DI_PILLAR.builtInRegistryHolder().key())
                .add(ModBlocks.BIG_AN_PILLAR.builtInRegistryHolder().key())
                .add(ModBlocks.BIG_GR_PILLAR.builtInRegistryHolder().key())

                .add(ModBlocks.PILLAR1.builtInRegistryHolder().key())
                .add(ModBlocks.PILLAR2.builtInRegistryHolder().key())
                .add(ModBlocks.PILLAR3.builtInRegistryHolder().key())

                .add(ModBlocks.ANDESITE_PILLAR1.builtInRegistryHolder().key())
                .add(ModBlocks.ANDESITE_PILLAR2.builtInRegistryHolder().key())
                .add(ModBlocks.ANDESITE_PILLAR3.builtInRegistryHolder().key())

                .add(ModBlocks.GRANITE_PILLAR1.builtInRegistryHolder().key())
                .add(ModBlocks.GRANITE_PILLAR2.builtInRegistryHolder().key())
                .add(ModBlocks.GRANITE_PILLAR3.builtInRegistryHolder().key())

                .add(ModBlocks.DEEPSLATE_PILLAR1.builtInRegistryHolder().key())
                .add(ModBlocks.DEEPSLATE_PILLAR2.builtInRegistryHolder().key())
                .add(ModBlocks.DEEPSLATE_PILLAR3.builtInRegistryHolder().key())

                .add(ModBlocks.MOSAIC_1_1.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_1_2.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_1_3.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_1_4.builtInRegistryHolder().key())

                .add(ModBlocks.MOSAIC_2_1.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_2_2.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_2_3.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_2_4.builtInRegistryHolder().key())

                .add(ModBlocks.MOSAIC_3_1.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_3_2.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_3_3.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_3_4.builtInRegistryHolder().key())

                .add(ModBlocks.MOSAIC_4_1.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_4_2.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_4_3.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_4_4.builtInRegistryHolder().key())

                .add(ModBlocks.MOSAIC_5_1.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_5_2.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_5_3.builtInRegistryHolder().key())
                .add(ModBlocks.MOSAIC_5_4.builtInRegistryHolder().key())
                .add(ModBlocks.PRESENT.builtInRegistryHolder().key())
        ;

        tag(BlockTags.DOORS).add((ModBlocks.LOCKING_DOOR.builtInRegistryHolder().key()));

    }
}
