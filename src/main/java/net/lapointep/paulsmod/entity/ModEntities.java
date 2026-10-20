package net.lapointep.paulsmod.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final ResourceKey<EntityType<?>> TELEPORTER_ARROW_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "teleporter_arrow")
    );

    public static final EntityType<TeleporterArrowEntity> TELEPORTER_ARROW = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            TELEPORTER_ARROW_KEY,
            EntityType.Builder.<TeleporterArrowEntity>of(TeleporterArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(TELEPORTER_ARROW_KEY)
    );

    public static final ResourceKey<EntityType<?>> GUIDE_ENTITY_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "guide_entity")
    );

    public static final EntityType<GuideEntity> GUIDE_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            GUIDE_ENTITY_KEY,
            EntityType.Builder.<GuideEntity>of(GuideEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(GUIDE_ENTITY_KEY)
    );
    
    public static final ResourceKey<EntityType<?>> TORCH_ENTITY_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "torch_entity")
    );

    public static final EntityType<TorchEntity> TORCH_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            TORCH_ENTITY_KEY,
            EntityType.Builder.<TorchEntity>of(TorchEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(TORCH_ENTITY_KEY)
    );

    public static void registerModEntities() {
        PaulsMod.LOGGER.info("Registering Entities for " + PaulsMod.MOD_ID);
    }
}