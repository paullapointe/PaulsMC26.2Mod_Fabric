package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.custom.HealingBlock;
import net.lapointep.paulsmod.block.custom.HelperBlock;
import net.lapointep.paulsmod.block.custom.LavaCoreBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

import static net.minecraft.world.inventory.AbstractFurnaceMenu.FUEL_SLOT;

public class HelperBlockEntity extends BlockEntity {

    public static final int MAX_COUNT_HIGH = 20*1;
    public static final int MAX_COUNT_LOW = 20*5;
    BETicker ticker = new BETicker(MAX_COUNT_LOW);

    public HelperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HELPER, pos, state);
    }

    public void setTarget(BlockPos pos, String dimension) {
        this.setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HelperBlockEntity blockEntity) {
        if (level.isClientSide()) { return; }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }

        boolean powered = state.getValue(HelperBlock.POWERED);
        if (powered) {
            // Turn off the signal.
            setState(false);
            ticker.setCounter(MAX_COUNT_LOW);
            return;
        }

        BlockEntity be = level.getBlockEntity(pos.relative(Direction.UP));
        if (be instanceof CrafterBlockEntity crafter) {
            int emptyStacks = 0;
            for (int i = 0; i < crafter.getContainerSize(); i++) {
                ItemStack stack = crafter.getItem(i);
                int count = stack.getCount();
                // If any item stack is down to it's last item,
                // don't turn on the signal.
                if (count == 1) { return; }
                if (stack.isEmpty()) { emptyStacks++; }
            }

            if (emptyStacks == crafter.getContainerSize()) {
                // All stacks are empty, do nothing.
                return;
            }

            setState(true);
            ticker.setCounter(MAX_COUNT_HIGH);
        }
    }

    public void setState(boolean state) {
        BlockState blockState = this.getBlockState();
        level.setBlock(this.worldPosition, blockState.setValue(HelperBlock.POWERED, state), 3);
        this.setChanged();
    }
}
