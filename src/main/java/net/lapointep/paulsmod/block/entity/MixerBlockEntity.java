package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.state.BlockState;

import static net.lapointep.paulsmod.block.entity.ModBlockEntities.MIXER;

public class MixerBlockEntity extends ProcessorBlockEntity {
    public MixerBlockEntity(BlockPos pos, BlockState state) {
        super(MIXER, pos, state,1);
    }

    public boolean isCrafting() {
        //PaulsMod.LOGGER.info("Mixer isCrafting!");
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        if (inputStack.isEmpty()) {
            return false;
        }

        if (inputStack.getItem() == Items.BUCKET) {
            this.setOutputItem(Items.WATER_BUCKET);
        }
        else if (inputStack.getItem() == Items.GLASS_BOTTLE) {
            this.setOutputItem(PotionContents.createItemStack(Items.POTION, Potions.WATER));
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.black()) {
            this.setOutputItem(Items.CONCRETE.black());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.white()) {
            this.setOutputItem(Items.CONCRETE.white());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.blue()) {
            this.setOutputItem(Items.CONCRETE.blue());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.black()) {
            this.setOutputItem(Items.CONCRETE.black());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.lightBlue()) {
            this.setOutputItem(Items.CONCRETE.lightBlue());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.brown()) {
            this.setOutputItem(Items.CONCRETE.brown());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.cyan()) {
            this.setOutputItem(Items.CONCRETE.cyan());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.gray()) {
            this.setOutputItem(Items.CONCRETE.gray());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.lightGray()) {
            this.setOutputItem(Items.CONCRETE.lightGray());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.green()) {
            this.setOutputItem(Items.CONCRETE.green());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.lime()) {
            this.setOutputItem(Items.CONCRETE.lime());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.magenta()) {
            this.setOutputItem(Items.CONCRETE.magenta());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.orange()) {
            this.setOutputItem(Items.CONCRETE.orange());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.pink()) {
            this.setOutputItem(Items.CONCRETE.pink());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.purple()) {
            this.setOutputItem(Items.CONCRETE.purple());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.red()) {
            this.setOutputItem(Items.CONCRETE.red());
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.yellow()) {
            this.setOutputItem(Items.CONCRETE.yellow());
        }
        else if (inputStack.getItem() == Items.DIRT) {
            this.setOutputItem(Items.MUD);
        }
        else if (inputStack.getItem() == Items.COARSE_DIRT) {
            this.setOutputItem(Items.MUD);
        }
        else if (inputStack.getItem() == Items.MAGMA_BLOCK) {
            this.setOutputItem(Blocks.OBSIDIAN.asItem());

        }

        int waterCount = PaulsCommon.countAdjacentWater(this.level, this.worldPosition);
        if (waterCount >= 1) {
            this.increaseCraftingProgress();
            //PaulsMod.LOGGER.info("Found water in adjacent blocks");
            return true;
        }
        //PaulsMod.LOGGER.info("No water!");
        return false;
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.mixer");
    }

}
