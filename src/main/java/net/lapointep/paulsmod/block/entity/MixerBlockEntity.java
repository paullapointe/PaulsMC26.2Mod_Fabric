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
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.GLASS_BOTTLE) {
            this.setOutputItem(PotionContents.createItemStack(Items.POTION, Potions.WATER));
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.black()) {
            this.setOutputItem(Items.CONCRETE.black());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.white()) {
            this.setOutputItem(Items.CONCRETE.white());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.blue()) {
            this.setOutputItem(Items.CONCRETE.blue());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.black()) {
            this.setOutputItem(Items.CONCRETE.black());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.lightBlue()) {
            this.setOutputItem(Items.CONCRETE.lightBlue());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.brown()) {
            this.setOutputItem(Items.CONCRETE.brown());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.cyan()) {
            this.setOutputItem(Items.CONCRETE.cyan());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.gray()) {
            this.setOutputItem(Items.CONCRETE.gray());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.lightGray()) {
            this.setOutputItem(Items.CONCRETE.lightGray());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.green()) {
            this.setOutputItem(Items.CONCRETE.green());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.lime()) {
            this.setOutputItem(Items.CONCRETE.lime());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.magenta()) {
            this.setOutputItem(Items.CONCRETE.magenta());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.orange()) {
            this.setOutputItem(Items.CONCRETE.orange());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.pink()) {
            this.setOutputItem(Items.CONCRETE.pink());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.purple()) {
            this.setOutputItem(Items.CONCRETE.purple());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.red()) {
            this.setOutputItem(Items.CONCRETE.red());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.CONCRETE_POWDER.yellow()) {
            this.setOutputItem(Items.CONCRETE.yellow());
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.DIRT) {
            this.setOutputItem(Items.MUD);
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.COARSE_DIRT) {
            this.setOutputItem(Items.MUD);
            this.increaseCraftingProgress();
        }
        else if (inputStack.getItem() == Items.MAGMA_BLOCK) {
            this.setOutputItem(Blocks.OBSIDIAN.asItem());
            this.increaseCraftingProgress();
        }

        int waterCount = PaulsCommon.countAdjacentWater(this.level, this.worldPosition);
        if (waterCount >= 1) {
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
