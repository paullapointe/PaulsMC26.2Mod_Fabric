package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.BlockWithCyclableState;
import net.lapointep.paulsmod.block.custom.CensusBlock;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static net.lapointep.paulsmod.util.PaulsCommon.trackPlayers;

public class CensusBlockEntity extends BlockEntity {
    private String areaName = "";
    private List<Integer> knownMapIds = new ArrayList<>();
    private boolean isFirstTick = true;
    final int MAX_COUNTER = 20*2;
    final BETicker ticker = new BETicker(MAX_COUNTER);
    final BETicker ticker2 = new BETicker(60);
    AABB range;

    public static final int RANGE = 32;
    public static final int FREE_BEDS = 1;
    HashMap<ServerPlayer, GameType> playersInRange = new HashMap<>();

    public CensusBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CENSUS, pos, state);
        range = new AABB(pos).inflate(RANGE);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CensusBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (isFirstTick) {
            isFirstTick = false;
            updateMapsOnFirstTick(level, pos);
        }
        if (ticker.notReadyToRun()) { return; }

        //PaulsMod.LOGGER.info("Census Ticking {} {}", pos, state);
        String hello = "You have entered "+areaName+".";
        String goodbye = "You have left "+areaName+".";
        trackPlayers(level, playersInRange, range, false, areaName, hello, goodbye);

        if (ticker2.notReadyToRun()) { return; }
        PaulsMod.LOGGER.info("Census Ticking2 {}", pos);
        int villagerCount = PaulsCommon.countEntitiesInRange(level, Villager.class, pos, RANGE);
        int bedCount = PaulsCommon.countBedsInRange(level, pos, RANGE);
        int villagersToAdd = (bedCount - FREE_BEDS) - villagerCount;
        if (villagersToAdd > 0) {
            for (int i = 0; i < villagersToAdd; i++) {
                EntityTypes.VILLAGER.spawn((ServerLevel) level, pos.east(), EntitySpawnReason.MOB_SUMMONED);
            }
            PaulsMod.LOGGER.info("Spawned {} villager(s)", villagersToAdd);
        }
    }

    public void setAreaName(String name) {
        this.areaName = name;
        this.setChanged();
        updateMarkersOnKnownMaps();
    }

    private void updateMapsOnFirstTick(Level level, BlockPos pos) {
        if (level.getBlockState(pos).getBlock() instanceof CensusBlock censusBlock) {
            for (int mapIdInt : knownMapIds) {
                net.minecraft.world.level.saveddata.maps.MapId mapId = new net.minecraft.world.level.saveddata.maps.MapId(mapIdInt);
                censusBlock.addTextMarkerToMap(level, pos, mapId, this.areaName, null);
            }
        }
    }

    private void updateMarkersOnKnownMaps() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }

        for (int mapIdInt : knownMapIds) {
            net.minecraft.world.level.saveddata.maps.MapId mapId = new net.minecraft.world.level.saveddata.maps.MapId(mapIdInt);
            net.minecraft.world.level.saveddata.maps.MapItemSavedData mapData = net.minecraft.world.item.MapItem.getSavedData(mapId, this.level);
            if (mapData != null) {
                String decorationKey = "census_marker_" + this.worldPosition.getX() + "_" + this.worldPosition.getZ();
                net.minecraft.world.level.saveddata.maps.MapDecoration existing = mapData.decorations.get(decorationKey);
                if (existing != null) {
                    net.minecraft.world.level.saveddata.maps.MapDecoration newDecoration = new net.minecraft.world.level.saveddata.maps.MapDecoration(
                            existing.type(),
                            existing.x(),
                            existing.y(),
                            existing.rot(),
                            java.util.Optional.of(net.minecraft.network.chat.Component.literal(this.areaName))
                    );
                    mapData.decorations.put(decorationKey, newDecoration);
                    mapData.setDirty();
                }
            }
        }
    }

    public String getAreaName() {
        return areaName;
    }

    public void addKnownMap(int mapId) {
        if (!knownMapIds.contains(mapId)) {
            knownMapIds.add(mapId);
            this.setChanged();
        }
    }

    public List<Integer> getKnownMapIds() {
        return knownMapIds;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("areaName", areaName);
        output.putInt("knownMapCount", knownMapIds.size());
        for (int i = 0; i < knownMapIds.size(); i++) {
            output.putInt("mapId_" + i, knownMapIds.get(i));
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.areaName = input.getStringOr("areaName", "");
        int count = input.getIntOr("knownMapCount", 0);
        knownMapIds.clear();
        for (int i = 0; i < count; i++) {
            knownMapIds.add(input.getIntOr("mapId_" + i, 0));
        }
    }


}
