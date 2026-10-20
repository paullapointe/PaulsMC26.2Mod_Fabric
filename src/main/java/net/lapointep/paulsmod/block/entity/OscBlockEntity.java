package net.lapointep.paulsmod.block.entity;


import net.lapointep.paulsmod.block.custom.OscBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class OscBlockEntity extends BlockEntity {

    public static final int MAX_COUNT = 20;
    BETicker ticker = new BETicker(MAX_COUNT);
    BETicker tickerUp = new BETicker(MAX_COUNT);
    BETicker tickerDown = new BETicker(MAX_COUNT);

    public OscBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OSC, pos, state);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, OscBlockEntity blockEntity) {
        if (level.isClientSide()) { return; }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }
        if ((tickerDown.getCounter() == 0) || (tickerUp.getCounter()==0)) { return;}

        boolean powered = state.getValue(OscBlock.POWERED);
        if (powered) {
            if (tickerUp.notReadyToRun()) { return; }
            // Flip the state
            setState(false);
        }
        else {
            if (tickerDown.notReadyToRun()) { return; }
            // Flip the state
            setState(true);
        }
    }

    public void setTimeUp(int time) {
        tickerUp.setCounter(time);
    }

    public void setTimeDown(int time) {
        tickerDown.setCounter(time);
    }

    public int getTimeUp() {
        return tickerUp.getCounter();
    }

    public int getTimeDown() {
        return tickerDown.getCounter();
    }

    public void setState(boolean state) {
        BlockState blockState = this.getBlockState();
        level.setBlock(this.worldPosition, blockState.setValue(OscBlock.POWERED, state), 3);
        this.setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("timeUp", tickerUp.getCounter());
        output.putInt("timeDown", tickerDown.getCounter());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        int timeUp = input.getIntOr("timeUp", 0);
        int timeDown = input.getIntOr("timeDown", 0);
        tickerUp.setCounter(timeUp);
        tickerDown.setCounter(timeDown);
    }
}
