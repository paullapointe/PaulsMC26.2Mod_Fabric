package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import static net.lapointep.paulsmod.block.entity.ModBlockEntities.DRYER;
import static net.lapointep.paulsmod.block.entity.ModBlockEntities.MIXER;

public class DryerBlockEntity extends ProcessorBlockEntity {
    public DryerBlockEntity(BlockPos pos, BlockState state) {
        super(DRYER, pos, state, 1);
    }

    public boolean isCrafting() {
        //PaulsMod.LOGGER.info("Mixer isCrafting!");
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        if (inputStack.isEmpty()) {
            return false;
        }

        if (inputStack.getItem() == Items.MUD) {
            this.setOutputItem(Items.CLAY_BALL);
            return true;
        }


        //PaulsMod.LOGGER.info("No water!");
        return false;
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.dryer");
    }
/*
    public static void tick(Level level, BlockPos pos, BlockState state, MixerBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        if (blockEntity.counter <= MAX_COUNTER) {
            blockEntity.counter++;
            return;
        }
        blockEntity.counter = 0;

        if (blockEntity.isCrafting()) {
            PaulsMod.LOGGER.info("Mixer Crafting! {}", pos);
            blockEntity.increaseCraftingProgress();
            setChanged(level, pos, state);
        }
    }
    */

}
