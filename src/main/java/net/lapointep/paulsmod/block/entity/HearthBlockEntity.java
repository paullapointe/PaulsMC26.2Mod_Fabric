package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.BlockWithCyclableState;
import net.lapointep.paulsmod.block.custom.HearthBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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

import static net.lapointep.paulsmod.block.custom.HearthBlock.MAX_STATE;
import static net.lapointep.paulsmod.util.PaulsCommon.trackPlayers;

public class HearthBlockEntity extends BlockEntity implements BlockWithCyclableState {
    private String placerName = "";
    final int MAX_COUNTER = 20*2;
    final BETicker ticker = new BETicker(MAX_COUNTER);
    public static final int RANGE = 10;
    HashMap<ServerPlayer, GameType> playersInRange = new HashMap<>();
    AABB range;

    public HearthBlockEntity(BlockPos pos, BlockState state) {

        super(ModBlockEntities.HEARTH, pos, state);
        range = new AABB(pos).inflate(RANGE);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HearthBlockEntity blockEntity) {
        // Tick logic here

        if (level.isClientSide()) {
            return;
        }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }

        //PaulsMod.LOGGER.info("Hearth Ticking {} {}", pos, state.getValue(HearthBlock.STATE));
        cycleState(level, pos, state);
        String hello = "You have entered the realm of "+placerName+". Be respectful.";
        String goodbye = "You have left the realm of "+placerName+".";
        trackPlayers(level, playersInRange, range, true, placerName, hello, goodbye);
    }

    public void cycleState(Level level, BlockPos pos, BlockState state) {
        int currentState = state.getValue(HearthBlock.STATE);
        int newState = (currentState + 1) % (MAX_STATE);
        level.setBlock(pos, state.setValue(HearthBlock.STATE, newState), 3);
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
