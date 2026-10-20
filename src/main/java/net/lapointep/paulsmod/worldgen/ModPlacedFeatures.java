package net.lapointep.paulsmod.worldgen;

import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SEEING_POOL_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "seeing_pool"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(
                SEEING_POOL_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModConfiguredFeatures.SEEING_POOL_KEY),
                        List.of(
                                // 1 in 40 chance per chunk
                                RarityFilter.onAverageOnceEvery(40),
                                // Spreads placement randomly across x/z within the chunk
                                InSquarePlacement.spread(),
                                // Restricts vertical bounds
                                HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(60)),
                                // Finds solid floor downward inside cave air
                                EnvironmentScanPlacement.scanningFor(
                                        Direction.DOWN,
                                        BlockPredicate.solid(),
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                        12
                                ),
                                // Ensures placement obeys biome rules
                                BiomeFilter.biome()
                        )
                )
        );
    }
}