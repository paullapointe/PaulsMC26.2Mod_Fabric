package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.block.BlockWithCyclableState;
import net.lapointep.paulsmod.block.custom.StatueBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import static net.lapointep.paulsmod.block.custom.StatueBlock.MAX_STATE;

public class StatueBlockEntity extends BlockEntity implements BlockWithCyclableState {

    private int state = 0;

    public StatueBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STATUE, pos, state);
    }

    public StatueBlockEntity(BlockEntityType T, BlockPos pos, BlockState state) {
        super(T, pos, state);
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
        this.setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("state", this.state);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.state = input.getIntOr("state", 0);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("state", this.state);
        return tag;
    }

    public void cycleState(Level level, BlockPos pos, BlockState state) {
        int currentState = state.getValue(StatueBlock.STATE);
        int newState = (currentState + 1) % (MAX_STATE + 1);
        level.setBlock(pos, state.setValue(StatueBlock.STATE, newState), 3);
    }
}
