package net.lapointep.paulsmod.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.LAVA_CORE);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.MIXER);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.SORTER);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.PEDESTAL);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.PLATE);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.ALTER);
        // BREWER blockstate is manually defined in resources/assets/paulsmod/blockstates/brewer.json
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.STORESHELF);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.SEEING_POOL);
        //blockModelGenerators.createNonTemplateModelBlock(ModBlocks.HOOK);
        blockModelGenerators.createTrivialCube(ModBlocks.SPAWNER);

        //blockModelGenerators.createNonTemplateModelBlock(ModBlocks.BIG_DI_PILLAR);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.PILLAR1);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.PILLAR2);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.PILLAR3);

        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.ANDESITE_PILLAR1);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.ANDESITE_PILLAR2);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.ANDESITE_PILLAR3);

        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.GRANITE_PILLAR1);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.GRANITE_PILLAR2);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.GRANITE_PILLAR3);

        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.DEEPSLATE_PILLAR1);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.DEEPSLATE_PILLAR2);
        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.DEEPSLATE_PILLAR3);

        blockModelGenerators.createNonTemplateModelBlock(ModBlocks.ATM);

        blockModelGenerators.createTrivialCube(ModBlocks.RIVER_ROCK);
        blockModelGenerators.createTrivialCube(ModBlocks.RIVER_ROCK_MORTAR);

        blockModelGenerators.createTrivialCube(ModBlocks.DRYER);
        blockModelGenerators.createTrivialCube(ModBlocks.BRICKMAKER);

        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_1_1);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_1_2);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_1_3);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_1_4);

        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_2_1);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_2_2);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_2_3);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_2_4);

        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_3_1);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_3_2);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_3_3);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_3_4);

        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_4_1);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_4_2);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_4_3);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_4_4);

        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_5_1);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_5_2);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_5_3);
        blockModelGenerators.createTrivialCube(ModBlocks.MOSAIC_5_4);
        blockModelGenerators.createTrivialCube(ModBlocks.PRESENT);

        blockModelGenerators.createDoor(ModBlocks.LOCKING_DOOR);

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

        // Blocks
        itemModelGenerators.generateFlatItem(ModItems.RIVER_ROCKS, ModelTemplates.FLAT_ITEM);

        // Items
        itemModelGenerators.generateFlatItem(ModItems.ONE_RING, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DWARVEN_RING, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.STAFF, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.AM_STAFF, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DI_STAFF, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.EM_STAFF, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.END_STAFF, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.WRENCH, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.BANDAGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WAFFLES, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.SCAMBLED_EGGS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.GLUE, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.COPPER_POWDER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.IRON_POWDER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.UNSTABLE_POWDER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.LAVA_POWDER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.NETHER_POWDER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.SCULK_POWDER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FLOUR, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WAFFLE_MIX, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.TELEPORTER_ARROW, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.GUIDE_STONE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.TRAVELING_STONE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.SORTING_WAND, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FLORIN, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WRAPPING_PAPER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CLAY_PLATE, ModelTemplates.FLAT_ITEM);
    }
}
