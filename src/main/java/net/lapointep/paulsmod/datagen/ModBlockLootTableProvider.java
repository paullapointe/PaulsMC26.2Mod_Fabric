package net.lapointep.paulsmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootSubProvider {
    public ModBlockLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.TELEPORTER);
        dropSelf(ModBlocks.FUELER);
        dropSelf(ModBlocks.RECIEVER);
        dropSelf(ModBlocks.HELPER);
        dropSelf(ModBlocks.OSC);
        dropSelf(ModBlocks.STICKYNOTE);
        dropSelf(ModBlocks.HEARTH);
        dropSelf(ModBlocks.CLOCK);
        dropSelf(ModBlocks.ENDERLAMP);
        dropSelf(ModBlocks.HEALING);
        dropSelf(ModBlocks.BARRIER);
        dropSelf(ModBlocks.CENSUS);
        dropSelf(ModBlocks.ENDERLAMP);
        dropSelf(ModBlocks.HEALING);
        dropSelf(ModBlocks.STAIN_GLASS);
        dropSelf(ModBlocks.STATUE);
        dropSelf(ModBlocks.PEDESTAL);
        dropSelf(ModBlocks.PLATE);
        dropSelf(ModBlocks.HOOK);
        dropSelf(ModBlocks.SPAWNER);
        dropSelf(ModBlocks.ALTER);
        dropSelf(ModBlocks.LAVA_CORE);
        dropSelf(ModBlocks.BRICKMAKER);
        dropSelf(ModBlocks.MIXER);
        dropSelf(ModBlocks.DRYER);
        dropSelf(ModBlocks.ATM);
        dropSelf(ModBlocks.SORTER);
        dropSelf(ModBlocks.BREWER);
        dropSelf(ModBlocks.STORESHELF);
        dropSelf(ModBlocks.CAMO);

        add(ModBlocks.RIVER_ROCK, createOreDrop(ModBlocks.RIVER_ROCK, ModItems.RIVER_ROCKS));
        dropSelf(ModBlocks.RIVER_ROCK_MORTAR);

        dropSelf(ModBlocks.FRAMED_WINDOW);
        dropSelf(ModBlocks.ROUND_FRAMED_WINDOW);

        dropSelf(ModBlocks.LATTICE_ARCH);

        dropSelf(ModBlocks.BIG_DI_PILLAR);
        dropSelf(ModBlocks.BIG_AN_PILLAR);
        dropSelf(ModBlocks.BIG_GR_PILLAR);
        dropSelf(ModBlocks.BIG_DS_PILLAR);

        dropSelf(ModBlocks.PILLAR1);
        dropSelf(ModBlocks.PILLAR2);
        dropSelf(ModBlocks.PILLAR3);

        dropSelf(ModBlocks.ANDESITE_PILLAR1);
        dropSelf(ModBlocks.ANDESITE_PILLAR2);
        dropSelf(ModBlocks.ANDESITE_PILLAR3);

        dropSelf(ModBlocks.GRANITE_PILLAR1);
        dropSelf(ModBlocks.GRANITE_PILLAR2);
        dropSelf(ModBlocks.GRANITE_PILLAR3);

        dropSelf(ModBlocks.DEEPSLATE_PILLAR1);
        dropSelf(ModBlocks.DEEPSLATE_PILLAR2);
        dropSelf(ModBlocks.DEEPSLATE_PILLAR3);

        dropSelf(ModBlocks.MOSAIC_1_1);
        dropSelf(ModBlocks.MOSAIC_1_2);
        dropSelf(ModBlocks.MOSAIC_1_3);
        dropSelf(ModBlocks.MOSAIC_1_4);

        dropSelf(ModBlocks.MOSAIC_2_1);
        dropSelf(ModBlocks.MOSAIC_2_2);
        dropSelf(ModBlocks.MOSAIC_2_3);
        dropSelf(ModBlocks.MOSAIC_2_4);

        dropSelf(ModBlocks.MOSAIC_3_1);
        dropSelf(ModBlocks.MOSAIC_3_2);
        dropSelf(ModBlocks.MOSAIC_3_3);
        dropSelf(ModBlocks.MOSAIC_3_4);

        dropSelf(ModBlocks.MOSAIC_4_1);
        dropSelf(ModBlocks.MOSAIC_4_2);
        dropSelf(ModBlocks.MOSAIC_4_3);
        dropSelf(ModBlocks.MOSAIC_4_4);

        dropSelf(ModBlocks.MOSAIC_5_1);
        dropSelf(ModBlocks.MOSAIC_5_2);
        dropSelf(ModBlocks.MOSAIC_5_3);
        dropSelf(ModBlocks.MOSAIC_5_4);

        add(ModBlocks.LOCKING_DOOR, this::createDoorTable);

        LootPool.Builder staffPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .when(LootItemRandomChanceCondition.randomChance(0.005f))
                .add(LootItem.lootTableItem(ModItems.STAFF));

        // 2. Generate the Loot Table for Oak Leaves
        LootTable.Builder acaciaLeavesBuilder = createLeavesDrops(
                Blocks.ACACIA_LEAVES,
                Blocks.ACACIA_SAPLING,
                NORMAL_LEAVES_SAPLING_CHANCES
        ).withPool(staffPool);

        this.add(Blocks.ACACIA_LEAVES, acaciaLeavesBuilder);
    }
}
