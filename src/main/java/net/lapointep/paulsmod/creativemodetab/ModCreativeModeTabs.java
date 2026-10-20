package net.lapointep.paulsmod.creativemodetab;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    // ... existing code ...

    public static final CreativeModeTab PAULSMOD_TAB5 = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(net.lapointep.paulsmod.PaulsMod.MOD_ID, "pauls_mod_magic"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STAFF))
                    .title(Component.translatable("creativemodetab.paulsmod.pauls_mods_magic"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.ONE_RING);
                        output.accept(ModItems.DWARVEN_RING);
                        output.accept(ModItems.STAFF);
                        output.accept(ModItems.AM_STAFF);
                        output.accept(ModItems.DI_STAFF);
                        output.accept(ModItems.EM_STAFF);
                        output.accept(ModItems.END_STAFF);
                        output.accept(ModBlocks.SEEING_POOL);
                    }).build());

    public static final CreativeModeTab PAULSMOD_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(net.lapointep.paulsmod.PaulsMod.MOD_ID, "pauls_mod_items"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TRAVELING_STONE))
                    .title(Component.translatable("creativemodetab.paulsmod.pauls_mods_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.WRENCH);
                        output.accept(ModItems.BANDAGE);
                        output.accept(ModItems.TELEPORTER_ARROW);
                        output.accept(ModItems.TRAVELING_STONE);
                        output.accept(ModItems.GUIDE_STONE);
                        output.accept(ModItems.FLORIN);
                        output.accept(ModItems.WRAPPING_PAPER);
                        output.accept(ModItems.WAFFLES);
                        output.accept(ModItems.STICKYNOTE);
                    }).build());

    public static final CreativeModeTab PAULSMOD_TAB2 = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(net.lapointep.paulsmod.PaulsMod.MOD_ID, "pauls_mod_tools"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.TELEPORTER, 1))
                    .title(Component.translatable("creativemodetab.paulsmod.pauls_mods_tools"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.TELEPORTER);
                        output.accept(ModBlocks.FUELER);
                        output.accept(ModBlocks.RECIEVER);
                        output.accept(ModBlocks.HELPER);
                        output.accept(ModBlocks.OSC);
                        output.accept(ModBlocks.STAIN_GLASS);
                        output.accept(ModBlocks.CENSUS);
                        output.accept(ModBlocks.HEARTH);
                        output.accept(ModBlocks.CLOCK);

                        output.accept(ModBlocks.ENDERLAMP);
                        output.accept(ModBlocks.HEALING);

                        output.accept(ModBlocks.PEDESTAL);
                        output.accept(ModBlocks.PLATE);
                        output.accept(ModItems.CLAY_PLATE);
                        output.accept(ModBlocks.HOOK);
                        output.accept(ModBlocks.ALTER);
                        output.accept(ModBlocks.SPAWNER);
                        output.accept(ModBlocks.LOCKING_DOOR);

                        output.accept(ModBlocks.LAVA_CORE);
                        output.accept(ModBlocks.MIXER);
                        output.accept(ModBlocks.DRYER);
                        output.accept(ModBlocks.ATM);
                        output.accept(ModBlocks.BRICKMAKER);
                        output.accept(ModBlocks.SORTER);
                        output.accept(ModBlocks.BREWER);
                        output.accept(ModBlocks.STORESHELF);
                        output.accept(ModBlocks.BARRIER);
                    }).build());

    public static final CreativeModeTab PAULSMOD_TAB3 = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(net.lapointep.paulsmod.PaulsMod.MOD_ID, "pauls_mod_potions"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.UNSTABLE_POWDER))
                    .title(Component.translatable("creativemodetab.paulsmod.pauls_mods_potions"))
                    .displayItems((parameters, output) -> {

                        output.accept(ModItems.BANDAGE);
                        output.accept(ModItems.COPPER_POWDER);
                        output.accept(ModItems.IRON_POWDER);
                        output.accept(ModItems.UNSTABLE_POWDER);
                        output.accept(ModItems.LAVA_POWDER);
                        output.accept(ModItems.NETHER_POWDER);
                        output.accept(ModItems.SCULK_POWDER);
                        output.accept(ModItems.FLOUR);
                        output.accept(ModItems.WAFFLE_MIX);

                    }).build());

    public static final CreativeModeTab PAULSMOD_TAB4 = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(net.lapointep.paulsmod.PaulsMod.MOD_ID, "pauls_mod_blocks"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.PILLAR1))
                    .title(Component.translatable("creativemodetab.paulsmod.pauls_mods_blocks"))
                    .displayItems((parameters, output) -> {

                        output.accept(ModBlocks.STATUE);
                        output.accept(ModItems.CAMO);

                        output.accept(ModBlocks.ROUND_FRAMED_WINDOW);
                        output.accept(ModBlocks.FRAMED_WINDOW);

                        output.accept(ModBlocks.LATTICE_ARCH);

                        output.accept(ModBlocks.BIG_DI_PILLAR);
                        output.accept(ModBlocks.BIG_AN_PILLAR);
                        output.accept(ModBlocks.BIG_GR_PILLAR);
                        output.accept(ModBlocks.BIG_DS_PILLAR);

                        output.accept(ModBlocks.PILLAR1);
                        output.accept(ModBlocks.PILLAR2);
                        output.accept(ModBlocks.PILLAR3);

                        output.accept(ModBlocks.ANDESITE_PILLAR1);
                        output.accept(ModBlocks.ANDESITE_PILLAR2);
                        output.accept(ModBlocks.ANDESITE_PILLAR3);

                        output.accept(ModBlocks.GRANITE_PILLAR1);
                        output.accept(ModBlocks.GRANITE_PILLAR2);
                        output.accept(ModBlocks.GRANITE_PILLAR3);

                        output.accept(ModBlocks.DEEPSLATE_PILLAR1);
                        output.accept(ModBlocks.DEEPSLATE_PILLAR2);
                        output.accept(ModBlocks.DEEPSLATE_PILLAR3);

                        output.accept(ModBlocks.MOSAIC_1_1);
                        output.accept(ModBlocks.MOSAIC_1_2);
                        output.accept(ModBlocks.MOSAIC_1_3);
                        output.accept(ModBlocks.MOSAIC_1_4);

                        output.accept(ModBlocks.MOSAIC_2_1);
                        output.accept(ModBlocks.MOSAIC_2_2);
                        output.accept(ModBlocks.MOSAIC_2_3);
                        output.accept(ModBlocks.MOSAIC_2_4);

                        output.accept(ModBlocks.MOSAIC_3_1);
                        output.accept(ModBlocks.MOSAIC_3_2);
                        output.accept(ModBlocks.MOSAIC_3_3);
                        output.accept(ModBlocks.MOSAIC_3_4);

                        output.accept(ModBlocks.MOSAIC_4_1);
                        output.accept(ModBlocks.MOSAIC_4_2);
                        output.accept(ModBlocks.MOSAIC_4_3);
                        output.accept(ModBlocks.MOSAIC_4_4);

                        output.accept(ModBlocks.MOSAIC_5_1);
                        output.accept(ModBlocks.MOSAIC_5_2);
                        output.accept(ModBlocks.MOSAIC_5_3);
                        output.accept(ModBlocks.MOSAIC_5_4);

                        output.accept(ModBlocks.RIVER_ROCK);
                        output.accept(ModItems.RIVER_ROCKS);
                        output.accept(ModBlocks.RIVER_ROCK_MORTAR);



                    }).build());


    public static void registerModCreativeModeTabs() {
        net.lapointep.paulsmod.PaulsMod.LOGGER.info("Registering Creative Mode Tabs for " + net.lapointep.paulsmod.PaulsMod.MOD_ID);
    }
}
