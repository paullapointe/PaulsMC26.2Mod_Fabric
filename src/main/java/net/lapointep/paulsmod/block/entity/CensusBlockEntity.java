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
import java.util.HashMap;

import static net.lapointep.paulsmod.block.custom.CensusBlock.MAX_STATE;
import static net.lapointep.paulsmod.util.PaulsCommon.trackPlayers;

public class CensusBlockEntity extends BlockEntity implements BlockWithCyclableState {
    private String areaName = "";
    final int MAX_COUNTER = 20*2;
    final BETicker ticker = new BETicker(MAX_COUNTER);
    final BETicker ticker2 = new BETicker(60);
    AABB range;

    public static final int RANGE = 20;
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
        if (ticker.notReadyToRun()) { return; }

        //PaulsMod.LOGGER.info("Census Ticking {} {}", pos, state.getValue(CensusBlock.STATE));
        cycleState(level, pos, state);
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


    public void cycleState(Level level, BlockPos pos, BlockState state) {
        int currentState = state.getValue(CensusBlock.STATE);
        int newState = (currentState + 1) % (MAX_STATE);
        //PaulsMod.LOGGER.info("Cycle State {} {}", pos, newState);
        level.setBlock(pos, state.setValue(CensusBlock.STATE, newState), 3);
    }

    public void setAreaName(String name) {
        this.areaName = name;
        this.setChanged();
    }

    public String getAreaName() {
        return areaName;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("areaName", areaName);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.areaName = input.getStringOr("areaName", "");
    }
}
