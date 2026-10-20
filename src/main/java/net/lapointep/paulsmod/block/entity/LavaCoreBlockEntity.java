package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import static net.lapointep.paulsmod.block.entity.ModBlockEntities.LAVA_CORE;

public class LavaCoreBlockEntity extends ProcessorBlockEntity {
    public LavaCoreBlockEntity(BlockPos pos, BlockState state) {
        super(LAVA_CORE, pos, state, 1);
    }

    public boolean isCrafting() {
        //PaulsMod.LOGGER.info("LavaCore isCrafting!");
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        if (inputStack.isEmpty()) {
            return false;
        }

        if (inputStack.getItem() == Items.BUCKET) {
            this.setOutputItem(Items.LAVA_BUCKET);
            this.increaseCraftingProgress();
        }

        int waterCount = PaulsCommon.countAdjacentLava(this.level, this.worldPosition);
        if (waterCount >= 1) {
            //PaulsMod.LOGGER.info("Found lava in adjacent blocks");
            return true;
        }
        //PaulsMod.LOGGER.info("No lava!");
        return false;
    }

    public ItemStack getOutputItem() {
        return new ItemStack(Items.LAVA_BUCKET);
    }


    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.lava_core");
    }
}
