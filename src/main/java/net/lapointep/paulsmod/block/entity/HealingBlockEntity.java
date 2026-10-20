package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.block.custom.HealingBlock;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class HealingBlockEntity extends BlockEntity {

    final int FIND_COUNTER = 10;
    final BETicker cooldownTicker = new BETicker(COOLDOWN);
    final BETicker alterFindTicker = new BETicker(FIND_COUNTER);

    AABB range;
    public static final int RANGE = 4;
    public static final int COOLDOWN = 20*2;

    BlockPos alterPos;
    final int ENERGY_USE = 10;
    private int state = 0;

    public HealingBlockEntity(BlockPos pos, BlockState state) {

        super(ModBlockEntities.HEALING, pos, state);
        range = new AABB(pos).inflate(RANGE);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HealingBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (cooldownTicker.notReadyToRun()) { return; }
       // PaulsMod.LOGGER.info("Healing Ticking {} {}", pos, state.getValue(HealingBlock.STATE));
        if (state.getValue(HealingBlock.STATE) == 1) {
            setState(0);
        }

        if (alterFindTicker.notReadyToRun()) { return; }
      //  PaulsMod.LOGGER.info("Alter Finding");
        alterPos =  PaulsCommon.findFirstBlock(level, pos, ModBlocks.ALTER, RANGE);
        //PaulsMod.LOGGER.info("Alter Pos: " + alterPos);
    }

    public void heal(LivingEntity entity) {
        if (alterPos != null) {
            BlockEntity blockEntity = level.getBlockEntity(alterPos);
            if (blockEntity == null) { return; }
            if (blockEntity instanceof AltarBlockEntity alter) {
                int healingEnergy = alter.getEnergy(ENERGY_USE);
                if (healingEnergy > 0) {
                    entity.heal(healingEnergy);
                    setState(1);
                }
            }
        }
    }

    public void setState(int state) {
        this.state = state;
        BlockState blockState = this.getBlockState();
        level.setBlock(this.worldPosition, blockState.setValue(HealingBlock.STATE, state), 3);
        this.setChanged();
    }
}
