package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.entity.CensusBlockEntity;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.network.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.saveddata.maps.*;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CensusBlock extends BaseEntityBlock {
    public static final MapCodec<CensusBlock> CODEC = simpleCodec(CensusBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public CensusBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            PaulsMod.LOGGER.info("CensusBlock.useWithoutItem: side=" + (level.isClientSide() ? "CLIENT" : "SERVER"));
            if (player.getMainHandItem().getItem() instanceof MapItem) {
                PaulsMod.LOGGER.info("Player has a map in their main hand");
                ItemStack mapStack = player.getMainHandItem();
                MapId mapId = mapStack.get(DataComponents.MAP_ID);
                CensusBlockEntity censusBlockEntity = (CensusBlockEntity) level.getBlockEntity(pos);
                boolean success = addTextMarkerToMap(level, pos, mapId, censusBlockEntity.getAreaName(), censusBlockEntity);
                if (success) {
                    player.sendSystemMessage(Component.literal("Added marker: ").append(censusBlockEntity.getAreaName()));
                }
                return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            }

            // Default behavior - open census screen
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CensusBlockEntity census) {
                ServerPlayNetworking.send((ServerPlayer) player, new ModMessages.OpenCensusScreenPayload(pos, census.getAreaName()));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.CENSUS, CensusBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CensusBlockEntity(pos, state);
    }
    
    public boolean addTextMarkerToMap(Level level, BlockPos pos, MapId mapId, String customText, @Nullable CensusBlockEntity censusBlockEntity) {
        if (mapId == null) {
            return false;
        }

        // 2. Fetch saved map data from the Level
        MapItemSavedData mapData = MapItem.getSavedData(mapId, level);
        if (mapData == null) {
            return false;
        }

        // 3. Compute local map coordinates (-128 to 127) relative to map center and scale
        int mapX = (int) ((pos.getX() - mapData.centerX) / (1 << mapData.scale));
        int mapZ = (int) ((pos.getZ() - mapData.centerZ) / (1 << mapData.scale));

        // Check if the block position is within the bounds of this map
        if (mapX < -128 || mapX > 127 || mapZ < -128 || mapZ > 127) {
            return false; // Out of bounds for this map
        }

        // 4. Build a unique lookup key for this decoration instance
        String decorationKey = "census_marker_" + pos.getX() + "_" + pos.getZ();

        // 5. Construct the modern MapDecoration record
        // Uses Holder<MapDecorationType> provided by MapDecorationTypes

        Holder<MapDecorationType> villageType = MapDecorationTypes.PLAINS_VILLAGE;
        Holder<Biome> biomeHolder = level.getBiome(pos);
        if (biomeHolder.is(Biomes.DESERT)) {
            villageType = MapDecorationTypes.DESERT_VILLAGE;
        }
        else if (biomeHolder.is(Biomes.TAIGA)) {
            villageType = MapDecorationTypes.TAIGA_VILLAGE;
        }
        else if (biomeHolder.is(Biomes.SNOWY_PLAINS)) {
            villageType = MapDecorationTypes.SNOWY_VILLAGE;
        }
        else if (biomeHolder.is(Biomes.SAVANNA)) {
            villageType = MapDecorationTypes.SAVANNA_VILLAGE;
        }

        MapDecoration textDecoration = new MapDecoration(
                villageType, // Icon type asset (choose any from MapDecorationTypes)
                (byte) mapX,
                (byte) mapZ,
                (byte) 0, // Rotation (0 to 15)
                Optional.of(Component.literal(customText)) // Text label rendered with the marker
        );

        // 6. Insert into decorations map and flag data as dirty for saving/syncing
        mapData.decorations.put(decorationKey, textDecoration);
        mapData.setDirty();
        if (censusBlockEntity != null) {
            censusBlockEntity.addKnownMap(mapId.id());
        }
        return true;
    }

    @Override
    public MutableComponent getName() {
        return  Component.translatable("test");
    }
}
