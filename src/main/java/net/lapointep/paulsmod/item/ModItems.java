package net.lapointep.paulsmod.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.item.custom.*;
// TODO: Verify registration for FuelerBlockItem with texture fueler_item.png
// TODO: Verify registration for RecieverBlockItem with texture reciever_item.png
    // TODO: Verify registration for HiddenTeleporterBlockItem with texture hidden_teleporter.png
    // TODO: Verify registration for TeleporterBlockItem with texture teleporter.png
    // TODO: Verify registration for TeleporterArrowItem with texture teleporter_arrow.png
    // TODO: Verify registration for GuideStoneItem with texture guide_stone.png
    // TODO: Verify registration for SorterWand with texture sorting_wand.png
    // TODO: Verify registration for StickyNoteItem with texture sticky_note.png
    // TODO: Verify registration for Necronomicon with texture necronomicon.png
    // TODO: Verify registration for SortingWandItem with texture sorting_wand.png
    // TODO: Verify registration for FlorinItem with texture florin.png
    // TODO: Verify registration for TravelingStoneItem with texture travelling_stone.png
    // TODO: Verify registration for TempPadItem with texture temp_pad.png
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Function;

import static net.lapointep.paulsmod.PaulsMod.MOD_ID;


public class ModItems {

    // Blocks
    public static final Item RIVER_ROCKS = registerItem("river_rocks", Item::new);
    public static final Item TELEPORTER = registerItem("teleporter", properties -> new TeleporterBlockItem(ModBlocks.TELEPORTER, properties.stacksTo(1)));
    public static final Item FUELER = registerItem("fueler", properties -> new FuelerBlockItem(ModBlocks.FUELER, properties.stacksTo(1)));
    public static final Item RECIEVER = registerItem("reciever", properties -> new RecieverBlockItem(ModBlocks.RECIEVER, properties.stacksTo(1)));
    public static final Item HELPER = registerItem("helper", properties -> new HelperBlockItem(ModBlocks.HELPER, properties.stacksTo(1)));
    public static final Item OSC = registerItem("osc", properties -> new OscBlockItem(ModBlocks.OSC, properties.stacksTo(1)));
    public static final Item STICKYNOTE = registerItem("stickynote", properties -> new StickyNoteBlockItem(ModBlocks.STICKYNOTE, properties.durability(8).stacksTo(1)));
    public static final Item CAMO = registerItem("camo_block", properties -> new net.minecraft.world.item.BlockItem(ModBlocks.CAMO, properties));


    // Items
    public static final Item STAFF = registerItem("staff", properties->new StaffItem(StaffItem.StaffType.staff, properties.durability(8).stacksTo(1)));
    public static final Item DI_STAFF = registerItem("diamond_staff", properties->new StaffItem(StaffItem.StaffType.diamond, properties.durability(16).stacksTo(1)));
    public static final Item EM_STAFF = registerItem("emerald_staff", properties->new StaffItem(StaffItem.StaffType.emerald, properties.durability(32).stacksTo(1)));
    public static final Item AM_STAFF = registerItem("am_staff", properties->new StaffItem(StaffItem.StaffType.am, properties.durability(64).stacksTo(1)));
    public static final Item END_STAFF = registerItem("end_staff", properties->new StaffItem(StaffItem.StaffType.end, properties.durability(256).stacksTo(1)));

    public static final Item WRENCH = registerItem("wrench", WrenchItem::new);
    public static final Item ONE_RING = registerItem("one_ring", OneRingItem::new);
    public static final Item DWARVEN_RING = registerItem("dwarven_ring", DwarvenRingItem::new);

    public static final Item COPPER_POWDER = registerItem("copper_powder",Item::new);
    public static final Item IRON_POWDER = registerItem("iron_powder", Item::new);
    public static final Item UNSTABLE_POWDER = registerItem("unstable_powder",Item::new);

    public static final Item LAVA_POWDER = registerItem("lava_powder", Item::new);
    public static final Item NETHER_POWDER = registerItem("nether_powder",Item::new);
    public static final Item WATER_POWDER = registerItem("water_powder",Item::new);
    public static final Item STONE_POWDER = registerItem("stone_powder",Item::new);
    public static final Item DIRT_POWDER = registerItem("dirt_powder",Item::new);

    public static final Item SCULK_POWDER = registerItem("sculk_powder",Item::new);

    public static final Item FLOUR = registerItem("flour", Item::new);
    public static final Item WAFFLE_MIX = registerItem("waffle_mix", Item::new);

    public static final Item TELEPORTER_ARROW = registerItem("teleporter_arrow",properties -> new TeleporterArrowItem(properties));
    public static final Item TRAVELING_STONE = registerItem("traveling_stone",properties -> new TravelingStoneItem(properties.durability(80)));

    public static final Item GUIDE_STONE = registerItem("guide_stone",properties -> new GuideStoneItem(properties.durability(80)));
    public static final Item SORTING_WAND = registerItem("sorting_wand",SortingWandItem::new);

    public static final Item FLORIN = registerItem("florin", properties -> new FlorinItem(properties));
    public static final Item WRAPPING_PAPER = registerItem("wrapping_paper", properties -> new WrappingPaperItem(properties));
    public static final Item PRESENT = registerItem("present", properties -> new PresentItem(ModBlocks.PRESENT, properties.stacksTo(1)));
    public static final Item WAFFLES = registerItem("waffles",Item::new);
    public static final Item CLAY_PLATE = registerItem("clay_plate",Item::new);
    public static final Item BANDAGE = registerItem("bandage", BandageItem::new);
    public static final Item GLUE = registerItem("glue", Item::new);

    public static final Item SCAMBLED_EGGS = registerItem("scrambled_eggs", properties -> new Item(properties.food(new FoodProperties(2, 1, false))));

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name)))));
    }

    public static void registerModItems() {
        net.lapointep.paulsmod.PaulsMod.LOGGER.info("Registering Mod Items for " + MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {

            output.accept(STAFF);
            output.accept(AM_STAFF);
            output.accept(DI_STAFF);
            output.accept(EM_STAFF);
            output.accept(END_STAFF);
            output.accept(ONE_RING);
            output.accept(DWARVEN_RING);

            output.accept(WRENCH);
            output.accept(BANDAGE);

            output.accept(COPPER_POWDER);
            output.accept(IRON_POWDER);
            output.accept(UNSTABLE_POWDER);
            output.accept(LAVA_POWDER);
            output.accept(NETHER_POWDER);
            output.accept(WATER_POWDER);
            output.accept(DIRT_POWDER);
            output.accept(STONE_POWDER);
            output.accept(SCULK_POWDER);

            output.accept(FLOUR);
            output.accept(WAFFLE_MIX);
            output.accept(TELEPORTER_ARROW);
            output.accept(TRAVELING_STONE);

            //output.accept(PILLAR);
            output.accept(TELEPORTER);
            output.accept(FUELER);
            output.accept(RECIEVER);
            output.accept(HELPER);
            output.accept(OSC);
            output.accept(STICKYNOTE);
            output.accept(CAMO);
            output.accept(ModBlocks.LAVA_CORE);
        });
    }
}
