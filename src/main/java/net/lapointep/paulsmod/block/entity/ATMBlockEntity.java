package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.menu.ATMMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static net.lapointep.paulsmod.block.entity.ModBlockEntities.ATM;

public class ATMBlockEntity extends ProcessorBlockEntity {

    public ATMBlockEntity(BlockPos pos, BlockState state) {
        super(ATM, pos, state, 1);
    }

    public boolean isCrafting() {
        return false;
    }

    @Override
    public Component getName() {
        return Component.translatable("block.paulsmod.atm");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {

        //PaulsMod.LOGGER.info("ATM Menu opened by "+ player.getDisplayName());
        int availableFunds = PaulsMod.Withdraw(player.getDisplayName().getString(), 64);
        this.setItem(OUTPUT_SLOT, new ItemStack(ModItems.FLORIN, availableFunds));
        this.setItem(INPUT_SLOT, ItemStack.EMPTY );
        return new ATMMenu(containerId, playerInventory, this, this.data);
    }

}
