package net.lapointep.paulsmod.block;

import net.lapointep.paulsmod.block.custom.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.Function;

import static net.lapointep.paulsmod.PaulsMod.MOD_ID;

public class ModBlocks {

    public static final Block BARRIER = registerBlock("barrier", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)), true);
    public static final Block FRAMED_WINDOW = registerBlock("framed_window",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.GLASS)));
    public static final Block ROUND_FRAMED_WINDOW = registerBlock("round_framed_window",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.GLASS)));

    public static final Block LATTICE_ARCH = registerBlock("lattice_arch",properties -> new LatticeArchBlock(properties.strength(2f).noOcclusion().sound(SoundType.WOOD)));

    public static final Block BIG_DI_PILLAR = registerBlock("big_pillar",properties -> new BigDiPillarBlock(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block BIG_GR_PILLAR = registerBlock("granite_big_pillar",properties -> new BigDiPillarBlock(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block BIG_AN_PILLAR = registerBlock("andesite_big_pillar",properties -> new BigDiPillarBlock(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block BIG_DS_PILLAR = registerBlock("big_ds_pillar",properties -> new BigDiPillarBlock(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));

    public static final Block PILLAR1 = registerBlock("pillar1",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block PILLAR2 = registerBlock("pillar2",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block PILLAR3 = registerBlock("pillar3",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));

    public static final Block ANDESITE_PILLAR1 = registerBlock("andesite_pillar1",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block ANDESITE_PILLAR2 = registerBlock("andesite_pillar2",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block ANDESITE_PILLAR3 = registerBlock("andesite_pillar3",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));

    public static final Block GRANITE_PILLAR1 = registerBlock("granite_pillar1",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block GRANITE_PILLAR2 = registerBlock("granite_pillar2",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block GRANITE_PILLAR3 = registerBlock("granite_pillar3",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));

    public static final Block DEEPSLATE_PILLAR1 = registerBlock("deepslate_pillar1",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block DEEPSLATE_PILLAR2 = registerBlock("deepslate_pillar2",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));
    public static final Block DEEPSLATE_PILLAR3 = registerBlock("deepslate_pillar3",properties -> new Block(properties.strength(3f).noOcclusion().sound(SoundType.STONE)));

    public static final Block MOSAIC_1_1 = registerBlock("mosaic1_1", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_1_2 = registerBlock("mosaic1_2", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_1_3 = registerBlock("mosaic1_3", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_1_4 = registerBlock("mosaic1_4", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));

    public static final Block MOSAIC_2_1 = registerBlock("mosaic2_1", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_2_2 = registerBlock("mosaic2_2", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_2_3 = registerBlock("mosaic2_3", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_2_4 = registerBlock("mosaic2_4", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));

    public static final Block MOSAIC_3_1 = registerBlock("mosaic3_1", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_3_2 = registerBlock("mosaic3_2", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_3_3 = registerBlock("mosaic3_3", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_3_4 = registerBlock("mosaic3_4", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));

    public static final Block MOSAIC_4_1 = registerBlock("mosaic4_1", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_4_2 = registerBlock("mosaic4_2", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_4_3 = registerBlock("mosaic4_3", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_4_4 = registerBlock("mosaic4_4", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));

    public static final Block MOSAIC_5_1 = registerBlock("mosaic5_1", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_5_2 = registerBlock("mosaic5_2", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_5_3 = registerBlock("mosaic5_3", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));
    public static final Block MOSAIC_5_4 = registerBlock("mosaic5_4", properties -> new Block(properties.strength(3f).sound(SoundType.STONE)));

    public static final Block RIVER_ROCK = registerBlock("river_rock",properties -> new DropExperienceBlock(UniformInt.of(1, 2), properties.strength(3f).sound(SoundType.STONE)));
    public static final Block RIVER_ROCK_MORTAR = registerBlock("river_rock_mortar",properties -> new DropExperienceBlock(UniformInt.of(1, 2), properties.strength(3f).sound(SoundType.STONE)));

    public static final Block HEARTH = registerBlock("hearth",properties -> new HearthBlock(properties.strength(4f).noOcclusion().lightLevel(state -> 15)) );
    public static final Block CLOCK = registerBlock("clock",properties -> new ClockBlock(properties.strength(4f).noOcclusion().lightLevel(state -> 15)) );
    public static final Block ENDERLAMP = registerBlock("enderlamp",properties -> new EnderLampBlock(properties.strength(4f).noOcclusion().lightLevel(state -> 15)) );
    public static final Block HEALING = registerBlock("healing",properties -> new HealingBlock(properties.strength(4f).noOcclusion().lightLevel(state -> 15)) );
    public static final Block CENSUS = registerBlock("census",properties -> new CensusBlock(properties.strength(4f).noOcclusion().lightLevel(state -> 15)));

    public static final Block TELEPORTER = registerBlock("teleporter",properties -> new TeleporterBlock(properties.strength(4f).noOcclusion()), false);
    public static final Block FUELER = registerBlock("fueler",properties -> new FuelerBlock(properties.strength(4f).noOcclusion()), false);
    public static final Block RECIEVER = registerBlock("reciever",properties -> new RecieverBlock(properties.strength(4f).noOcclusion()), false);
    public static final Block HELPER = registerBlock("helper",properties -> new HelperBlock(properties.strength(4f).noOcclusion()), false);
    public static final Block OSC = registerBlock("osc",properties -> new OscBlock(properties.strength(4f).noOcclusion()), false);
    public static final Block STICKYNOTE = registerBlock("stickynote",properties -> new StickyNoteBlock(properties.strength(1f).noOcclusion()), false);

    public static final Block STATUE = registerBlock("statue",properties -> new StatueBlock(properties.strength(4f).noOcclusion()));
    public static final Block PEDESTAL = registerBlock("pedestal",properties -> new PedestalBlock(properties.strength(4f).noOcclusion()));
    public static final Block PLATE = registerBlock("plate",properties -> new PlateBlock(properties.strength(4f).noOcclusion()));
    public static final Block SPAWNER = registerBlock("spawner",properties -> new SpawnerBlock(properties.strength(4f).noOcclusion()));
    public static final Block HOOK = registerBlock("hook",properties -> new HookBlock(properties.strength(4f).noOcclusion()));
    public static final Block ALTER = registerBlock("alter",properties -> new AlterBlock(properties.strength(4f).noOcclusion()));
    public static final Block SEEING_POOL = registerBlock("seeing_pool",properties -> new SeeingPoolBlock(properties.strength(8f).noOcclusion()));

    public static final Block STAIN_GLASS = registerBlock("stain_glass", properties -> new StainGlassBlock(properties.strength(4f).noOcclusion()));

    public static final Block LAVA_CORE = registerBlock("lava_core", properties -> new LavaCoreBlock(properties.strength(4f).noOcclusion()));
    public static final Block MIXER = registerBlock("mixer", properties -> new MixerBlock(properties.strength(4f).noOcclusion()));
    public static final Block DRYER = registerBlock("dryer", properties -> new DryerBlock(properties.strength(4f).noOcclusion()));
    public static final Block ATM = registerBlock("atm", properties -> new ATMBlock(properties.strength(4f).noOcclusion()));
    public static final Block BRICKMAKER = registerBlock("brickmaker", properties -> new BrickMakerBlock(properties.strength(4f).noOcclusion()));
    public static final Block SORTER = registerBlock("sorter", properties -> new SorterBlock(properties.strength(4f).noOcclusion()));
    public static final Block BREWER = registerBlock("brewer", properties -> new BrewerBlock(properties.strength(4f).noOcclusion()));
    public static final Block STORESHELF = registerBlock("storeshelf", properties -> new StoreShelfBlock(properties.strength(4f).noOcclusion()));
    public static final Block CAMO = registerBlock("camo", properties -> new CamoBlock(properties.strength(4f).noOcclusion()));
    public static final Block PRESENT = registerBlock("present", properties -> new PresentBlock(properties.strength(1f).noOcclusion()), false);

    public static final Block LOCKING_DOOR = registerBlock("locking_door",properties -> new LockingDoorBlock(BlockSetType.OAK, properties.strength(3f).requiresCorrectToolForDrops().noOcclusion()));

    public static final Block UNSTABLENETHERRACK = registerBlock("unstablenetherrack",properties -> new UnstableBlock(UnstableBlock.UnstableType.Netherrack, properties));
    public static final Block UNSTABLELAVA = registerBlock("unstablelava",properties -> new UnstableBlock(UnstableBlock.UnstableType.Lava, properties));
    public static final Block UNSTABLEWATER = registerBlock("unstablewater",properties -> new UnstableBlock(UnstableBlock.UnstableType.Water, properties));
    public static final Block UNSTABLESTONE = registerBlock("unstablestone",properties -> new UnstableBlock(UnstableBlock.UnstableType.Stone, properties));
    public static final Block UNSTABLEDIRT = registerBlock("unstabledirt",properties -> new UnstableBlock(UnstableBlock.UnstableType.Dirt, properties));

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        return registerBlock(name, function, true);
    }

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, boolean shouldRegisterItem) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name))));
        if (shouldRegisterItem) {
            registerBlockItem(name, toRegister);
        }
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name), toRegister);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name),
                new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name)))));
    }

    public static void registerModBlocks() {
        net.lapointep.paulsmod.PaulsMod.LOGGER.info("Registering Mod Blocks for " + MOD_ID);
    }
}
