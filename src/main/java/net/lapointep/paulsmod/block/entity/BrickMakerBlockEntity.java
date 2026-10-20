package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import static net.lapointep.paulsmod.block.entity.ModBlockEntities.BRICKMAKER;
import static net.lapointep.paulsmod.block.entity.ModBlockEntities.MIXER;

public class BrickMakerBlockEntity extends ProcessorBlockEntity {

    public static final int REQUIRED_STACK_SIZE = 4;
    public BrickMakerBlockEntity(BlockPos pos, BlockState state)
    {
        super(BRICKMAKER, pos, state, REQUIRED_STACK_SIZE);
    }

    public boolean isCrafting() {
        //PaulsMod.LOGGER.info("Mixer isCrafting!");
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        if (inputStack.isEmpty()) {
            return false;
        }

        if (inputStack.getItem() == Items.BRICK) {
            if (inputStack.getCount() >= 4) {
                this.setOutputItem(Items.BRICKS);
                this.setRequiredInputItemSize(4);
                return true;
            }
        }
        else if (inputStack.getItem() == Items.NETHER_BRICK) {
            if (inputStack.getCount() >= 4) {
                this.setOutputItem(Items.NETHER_BRICKS);
                this.setRequiredInputItemSize(4);
                return true;
            }
        }
        else if (inputStack.getItem() == Items.RESIN_BRICK) {
            if (inputStack.getCount() >= 4) {
                this.setOutputItem(Items.RESIN_BRICKS);
                this.setRequiredInputItemSize(4);
                return true;
            }
        }
        else if (inputStack.getItem() == Items.QUARTZ) {
            if (inputStack.getCount() >= 4) {
                this.setOutputItem(Items.QUARTZ_BLOCK);
                this.setRequiredInputItemSize(4);
                return true;
            }
        }
        else if (inputStack.getItem() == Items.AMETHYST_SHARD) {
            if (inputStack.getCount() >= 4) {
                this.setOutputItem(Items.AMETHYST_BLOCK);
                this.setRequiredInputItemSize(4);
                return true;
            }
        }
        else if (inputStack.getItem() == Items.GOLD_NUGGET) {
            if (inputStack.getCount() >= 9) {
                this.setOutputItem(Items.GOLD_INGOT);
                this.setRequiredInputItemSize(9);
                return true;
            }
        }
        else if (inputStack.getItem() == Items.IRON_NUGGET) {
            if (inputStack.getCount() >= 9) {
                this.setOutputItem(Items.IRON_INGOT);
                this.setRequiredInputItemSize(9);
                return true;
            }
        }
        else if (inputStack.getItem() == Items.COPPER_NUGGET) {
            if (inputStack.getCount() >= 9) {
                this.setOutputItem(Items.COPPER_INGOT);
                this.setRequiredInputItemSize(9);
                return true;
            }
        }

        return false;
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.brickmaker");
    }
}
