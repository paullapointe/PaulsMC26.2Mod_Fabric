package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.block.BlockWithCyclableState;
import net.lapointep.paulsmod.block.custom.EnderLampBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static net.lapointep.paulsmod.block.custom.EnderLampBlock.MAX_STATE;

public class EnderLampBlockEntity extends BlockEntity implements BlockWithCyclableState {

    final int MAX_COUNTER = 20*2;
    final BETicker ticker = new BETicker(MAX_COUNTER);
    AABB range;
    public static final int RANGE = 33;

    public EnderLampBlockEntity(BlockPos pos, BlockState state) {

        super(ModBlockEntities.ENDERLAMP, pos, state);
        range = new AABB(pos).inflate(RANGE);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnderLampBlockEntity blockEntity) {
        // Tick logic here

        if (level.isClientSide()) {
            return;
        }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }

        cycleState(level, pos, state);
        List<EnderMan> endermen = level.getEntitiesOfClass(EnderMan.class, range);
        if (endermen.isEmpty()) { return; }
        EnderMan enderman = endermen.getFirst();

        enderman.teleportTo(pos.getX(), pos.getY() - 3, pos.getZ());
        enderman.lavaHurt();
        //PaulsMod.LOGGER.info("EnderLamp Teleporting {} {}", pos, enderman.position());
    }

    public void cycleState(Level level, BlockPos pos, BlockState state) {
        int currentState = state.getValue(EnderLampBlock.STATE);
        int newState = (currentState + 1) % (MAX_STATE);
        level.setBlock(pos, state.setValue(EnderLampBlock.STATE, newState), 3);
    }
}
