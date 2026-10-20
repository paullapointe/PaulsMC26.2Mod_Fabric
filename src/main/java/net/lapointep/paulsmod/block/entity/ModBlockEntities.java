package net.lapointep.paulsmod.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<TeleporterBlockEntity> TELEPORTER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "teleporter"), FabricBlockEntityTypeBuilder.create(TeleporterBlockEntity::new, ModBlocks.TELEPORTER).build());
    public static final BlockEntityType<FuelerBlockEntity> FUELER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "fueler"), FabricBlockEntityTypeBuilder.create(FuelerBlockEntity::new, ModBlocks.FUELER).build());
    public static final BlockEntityType<RecieverBlockEntity> RECIEVER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "reciever"), FabricBlockEntityTypeBuilder.create(RecieverBlockEntity::new, ModBlocks.RECIEVER).build());
    public static final BlockEntityType<HelperBlockEntity> HELPER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "helper"), FabricBlockEntityTypeBuilder.create(HelperBlockEntity::new, ModBlocks.HELPER).build());
    public static final BlockEntityType<OscBlockEntity> OSC = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "osc"), FabricBlockEntityTypeBuilder.create(OscBlockEntity::new, ModBlocks.OSC).build());
    public static final BlockEntityType<StickyNoteBlockEntity> STICKYNOTE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "stickynote"), FabricBlockEntityTypeBuilder.create(StickyNoteBlockEntity::new, ModBlocks.STICKYNOTE).build());
    public static final BlockEntityType<HearthBlockEntity> HEARTH = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "hearth"), FabricBlockEntityTypeBuilder.create(HearthBlockEntity::new, ModBlocks.HEARTH).build());
    public static final BlockEntityType<ClockBlockEntity> CLOCK = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "clock"), FabricBlockEntityTypeBuilder.create(ClockBlockEntity::new, ModBlocks.CLOCK).build());

    public static final BlockEntityType<EnderLampBlockEntity> ENDERLAMP = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "enderlamp"), FabricBlockEntityTypeBuilder.create(EnderLampBlockEntity::new, ModBlocks.ENDERLAMP).build());
    public static final BlockEntityType<HealingBlockEntity> HEALING = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "healing"), FabricBlockEntityTypeBuilder.create(HealingBlockEntity::new, ModBlocks.HEALING).build());
    public static final BlockEntityType<CensusBlockEntity> CENSUS = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "census"), FabricBlockEntityTypeBuilder.create(CensusBlockEntity::new, ModBlocks.CENSUS).build());

    public static final BlockEntityType<StatueBlockEntity> STATUE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "statue"), FabricBlockEntityTypeBuilder.create(StatueBlockEntity::new, ModBlocks.STATUE).build());
    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "pedestal"), FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new, ModBlocks.PEDESTAL).build());
    public static final BlockEntityType<PlateBlockEntity> PLATE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "plate"), FabricBlockEntityTypeBuilder.create(PlateBlockEntity::new, ModBlocks.PLATE).build());
    public static final BlockEntityType<SpawnerBlockEntity> SPAWNER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "spawner"), FabricBlockEntityTypeBuilder.create(SpawnerBlockEntity::new, ModBlocks.SPAWNER).build());
    public static final BlockEntityType<HookBlockEntity> HOOK = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "hook"), FabricBlockEntityTypeBuilder.create(HookBlockEntity::new, ModBlocks.HOOK).build());
    public static final BlockEntityType<AltarBlockEntity> ALTER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "alter"), FabricBlockEntityTypeBuilder.create(AltarBlockEntity::new, ModBlocks.ALTER).build());
    public static final BlockEntityType<SeeingPoolBlockEntity> SEEING_POOL = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "seeing_pool"), FabricBlockEntityTypeBuilder.create(SeeingPoolBlockEntity::new, ModBlocks.SEEING_POOL).build());
    public static final BlockEntityType<StainGlassBlockEntity> STAINGLASS = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "stainglass"), FabricBlockEntityTypeBuilder.create(StainGlassBlockEntity::new, ModBlocks.STAIN_GLASS).build());

    public static final BlockEntityType<LavaCoreBlockEntity> LAVA_CORE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "lava_core"), FabricBlockEntityTypeBuilder.create(LavaCoreBlockEntity::new, ModBlocks.LAVA_CORE).build());
    public static final BlockEntityType<MixerBlockEntity> MIXER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "mixer"), FabricBlockEntityTypeBuilder.create(MixerBlockEntity::new, ModBlocks.MIXER).build());
    public static final BlockEntityType<DryerBlockEntity> DRYER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "dryer"), FabricBlockEntityTypeBuilder.create(DryerBlockEntity::new, ModBlocks.DRYER).build());
    public static final BlockEntityType<ATMBlockEntity> ATM = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "atm"), FabricBlockEntityTypeBuilder.create(ATMBlockEntity::new, ModBlocks.ATM).build());
    public static final BlockEntityType<BrickMakerBlockEntity> BRICKMAKER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "brick_maker"), FabricBlockEntityTypeBuilder.create(BrickMakerBlockEntity::new, ModBlocks.BRICKMAKER).build());
    public static final BlockEntityType<SorterBlockEntity> SORTER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "sorter"), FabricBlockEntityTypeBuilder.create(SorterBlockEntity::new, ModBlocks.SORTER).build());
    public static final BlockEntityType<BrewerBlockEntity> BREWER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "brewer"), FabricBlockEntityTypeBuilder.create(BrewerBlockEntity::new, ModBlocks.BREWER).build());
    public static final BlockEntityType<StoreShelfBlockEntity> STORESHELF = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "storeshelf"), FabricBlockEntityTypeBuilder.create(StoreShelfBlockEntity::new, ModBlocks.STORESHELF).build());
    public static final BlockEntityType<CamoBlockEntity> CAMO = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "camo"), FabricBlockEntityTypeBuilder.create(CamoBlockEntity::new, ModBlocks.CAMO).build());
    public static final BlockEntityType<PresentBlockEntity> PRESENT = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "present"), FabricBlockEntityTypeBuilder.create(PresentBlockEntity::new, ModBlocks.PRESENT).build());

    public static final BlockEntityType<LockingDoorBlockEntity> LOCKING_DOOR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "locking_door"), FabricBlockEntityTypeBuilder.create(LockingDoorBlockEntity::new, ModBlocks.LOCKING_DOOR).build());

    public static final BlockEntityType<UnstableBlockEntity> UNSTABLENETHERRACK = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "unstablenetherrack"),  FabricBlockEntityTypeBuilder.create((pos, state) -> new UnstableBlockEntity(ModBlockEntities.UNSTABLENETHERRACK, pos, state), ModBlocks.UNSTABLENETHERRACK).build());
    public static final BlockEntityType<UnstableBlockEntity> UNSTABLELAVA = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "unstablelava"), FabricBlockEntityTypeBuilder.create((pos, state) -> new UnstableBlockEntity(ModBlockEntities.UNSTABLELAVA, pos, state), ModBlocks.UNSTABLELAVA).build());
    public static final BlockEntityType<UnstableBlockEntity> UNSTABLEWATER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "unstablewater"), FabricBlockEntityTypeBuilder.create((pos, state) -> new UnstableBlockEntity(ModBlockEntities.UNSTABLEWATER, pos, state), ModBlocks.UNSTABLEWATER).build());
    public static final BlockEntityType<UnstableBlockEntity> UNSTABLESTONE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "unstablestone"), FabricBlockEntityTypeBuilder.create((pos, state) -> new UnstableBlockEntity(ModBlockEntities.UNSTABLESTONE, pos, state), ModBlocks.UNSTABLESTONE).build());
    public static final BlockEntityType<UnstableBlockEntity> UNSTABLEDIRT = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "unstabledirt"), FabricBlockEntityTypeBuilder.create((pos, state) -> new UnstableBlockEntity(ModBlockEntities.UNSTABLEDIRT, pos, state), ModBlocks.UNSTABLEDIRT).build());

    public static void registerModBlockEntities() {
        PaulsMod.LOGGER.info("Registering Mod Block Entities for " + PaulsMod.MOD_ID);
    }
}
