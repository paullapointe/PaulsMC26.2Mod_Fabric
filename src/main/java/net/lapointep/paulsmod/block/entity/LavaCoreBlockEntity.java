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
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        if (inputStack.isEmpty()) {
            return false;
        }
        //PaulsMod.LOGGER.info("LavaCore isCrafting!");
        if (inputStack.getItem() == Items.BUCKET) {
            this.setOutputItem(Items.LAVA_BUCKET);
            //PaulsMod.LOGGER.info("LavaCore isCrafting! Output is Lava Bucket");
        }
        else if (inputStack.getItem() == Items.ANDESITE) {
            this.setOutputItem(Items.MAGMA_BLOCK);
            //PaulsMod.LOGGER.info("LavaCore isCrafting! Output is Magma Block");
        }
        else if (inputStack.getItem() == Items.GRANITE) {
            this.setOutputItem(Items.BLACKSTONE);
            //PaulsMod.LOGGER.info("LavaCore isCrafting! Output is Blackstone");
        }
        else {
            return false;
        }

        int lavaCount = PaulsCommon.countAdjacentLava(this.level, this.worldPosition);
        if (lavaCount >= 1) {
            this.increaseCraftingProgress();
            return true;
        }
        return false;
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.lava_core");
    }
}
