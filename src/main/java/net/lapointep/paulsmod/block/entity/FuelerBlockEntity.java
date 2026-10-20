package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.custom.FuelerBlock;
import net.lapointep.paulsmod.block.custom.LavaCoreBlock;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

import static net.minecraft.world.inventory.AbstractFurnaceMenu.FUEL_SLOT;

public class FuelerBlockEntity extends BlockEntity {
    private BlockPos targetPos = BlockPos.ZERO;
    private String targetDimension = "minecraft:overworld";

    private BlockPos storageTargetPos = BlockPos.ZERO;
    private String storageTargetDimension = "minecraft:overworld";
    public static final int FURNACE_OUTPUT_SLOT = 2;

    public static final int MAX_COUNT = 20*3;
    BETicker ticker = new BETicker(MAX_COUNT);
    public FuelerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FUELER, pos, state);
    }

    public void setTarget(BlockPos pos, String dimension) {
        this.targetPos = pos;
        this.targetDimension = dimension;
        this.setChanged();
    }

    public void setStorageTarget(BlockPos pos, String dimension) {
        this.storageTargetPos = pos;
        this.storageTargetDimension = dimension;
        this.setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FuelerBlockEntity blockEntity) {
        if (level.isClientSide()) { return; }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }

        // Check the attached block to see if it's a furnace and if it needs fuel
        BlockPos attachedBlockPos = pos.relative(state.getValue(FuelerBlock.FACING));
        BlockEntity attachedBlockEntity = level.getBlockEntity(attachedBlockPos);
        if (attachedBlockEntity instanceof AbstractFurnaceBlockEntity furnaceBlockEntity) {
            // First, handle any buckets in the furnace
            // and make sure the furnace has fuel
            BlockEntity target = level.getBlockEntity(targetPos);
            if (target instanceof LavaCoreBlockEntity lavaCoreBlockEntity) {
                ItemStack fuelStack = furnaceBlockEntity.getItem(FUEL_SLOT);

                if (fuelStack.is(Items.BUCKET)) {
                    // Grab the bucket and put it in the lava core
                    fuelStack.shrink(1);
                    ItemStack lavaCoreInput = lavaCoreBlockEntity.getItem(ProcessorBlockEntity.INPUT_SLOT);
                    if (lavaCoreInput.isEmpty()) {
                        lavaCoreBlockEntity.setItem(ProcessorBlockEntity.INPUT_SLOT, new ItemStack(Items.BUCKET));
                        lavaCoreBlockEntity.setChanged();
                    }
                    else if (lavaCoreInput.is(Items.BUCKET)) {
                        lavaCoreInput.grow(1);
                        lavaCoreBlockEntity.setChanged();
                    }
                    PaulsMod.LOGGER.info("Returned bucket to lava core");
                }

                if (fuelStack.isEmpty()) {
                    // Need fuel, empty slot
                    ItemStack lavaCoreOutput = lavaCoreBlockEntity.getItem(ProcessorBlockEntity.OUTPUT_SLOT);
                    if (lavaCoreOutput.is(Items.LAVA_BUCKET)) {
                        lavaCoreOutput.shrink(1);
                        furnaceBlockEntity.setItem(FUEL_SLOT, new ItemStack(Items.LAVA_BUCKET));
                        PaulsMod.LOGGER.info("Fueled furnace");
                    }
                }
            }
            // Move any completed items to the storage target
            BlockEntity storageTarget = level.getBlockEntity(storageTargetPos);
            if (storageTarget instanceof Container container) {
                ItemStack outputStack = furnaceBlockEntity.getItem(FURNACE_OUTPUT_SLOT);
                if (!outputStack.isEmpty()) {
                    if (PaulsCommon.tryInsertItem(level, storageTargetPos, null, outputStack)){
                        outputStack.shrink(1);
                    }
                    else {
                        PaulsMod.LOGGER.warn("Failed to insert item to storage target at " + storageTargetPos);
                    }
                }
            }
        }
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("targetX", targetPos.getX());
        output.putInt("targetY", targetPos.getY());
        output.putInt("targetZ", targetPos.getZ());
        output.putString("targetDim", targetDimension);

        output.putInt("storageTargetX", storageTargetPos.getX());
        output.putInt("storageTargetY", storageTargetPos.getY());
        output.putInt("storageTargetZ", storageTargetPos.getZ());
        output.putString("storageTargetDim", storageTargetDimension);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        int x = input.getIntOr("targetX", 0);
        int y = input.getIntOr("targetY", 0);
        int z = input.getIntOr("targetZ", 0);
        this.targetPos = new BlockPos(x, y, z);
        this.targetDimension = input.getStringOr("targetDim", "minecraft:overworld");

        int sx = input.getIntOr("storageTargetX", 0);
        int sy = input.getIntOr("storageTargetY", 0);
        int sz = input.getIntOr("storageTargetZ", 0);
        this.storageTargetPos = new BlockPos(sx, sy, sz);
        this.storageTargetDimension = input.getStringOr("storageTargetDim", "minecraft:overworld");
    }
}
