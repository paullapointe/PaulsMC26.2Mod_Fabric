package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.BlockWithCyclableState;
import net.lapointep.paulsmod.block.custom.LifePreserverBlock;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.lapointep.paulsmod.block.custom.LifePreserverBlock.MAX_STATE;
import static net.lapointep.paulsmod.util.PaulsCommon.trackPlayers;

public class LifePreserverBlockEntity extends BlockEntity {
    private String placerName = "";
    final int MAX_COUNTER = 20*2;
    final BETicker ticker = new BETicker(MAX_COUNTER);
    public static final int RANGE = 10;
    AABB range;

    public LifePreserverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIFEPRESERVER, pos, state);
        range = new AABB(pos).inflate(RANGE);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LifePreserverBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }

        //PaulsMod.LOGGER.info("LifePreserver Ticking {} {}", pos, state.getValue(LifePreserverBlock.STATE));
        List<Entity> entitiesInRange = PaulsCommon.getEntitiesInRange(level, pos, RANGE);
        for (Entity entity : entitiesInRange) {
            if ((entity instanceof Villager villager) || (entity instanceof IronGolem)){
                boolean inWater = entity.isInWater();
                if (inWater) {
                    PaulsMod.LOGGER.info("Pulling villager out of water from {} to {}", entity, pos);
                    entity.teleportTo(pos.getX()+1.5, pos.getY(), pos.getZ());
                }
            }
        }
    }

    public void cycleState(Level level, BlockPos pos, BlockState state) {
        int currentState = state.getValue(LifePreserverBlock.STATE);
        int newState = (currentState + 1) % (MAX_STATE);
        level.setBlock(pos, state.setValue(LifePreserverBlock.STATE, newState), 3);
    }

    public void setPlacerName(String name) {
        this.placerName = name;
        this.setChanged();
    }

    public String getPlacerName() {
        return placerName;
    }


    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("placerName", placerName);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.placerName = input.getStringOr("placerName", "");
    }
}
