package net.lapointep.paulsmod.block.entity;


import net.lapointep.paulsmod.block.custom.AlterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jspecify.annotations.Nullable;

import static net.lapointep.paulsmod.util.PaulsCommon.isItemFuel;

public class AltarBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem {
    public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private int state = 0;
    private int energy = 0;
    public static final int MAX_COUNT = 20*5;
    BETicker ticker = new BETicker(MAX_COUNT);

    public AltarBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.ALTER, worldPosition, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AltarBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }
        //PaulsMod.LOGGER.info("Alter Ticking {} {}", pos, state.getValue(AlterBlock.STATE));
        if (energy == 0) {
            setState(0);
        }
        else {
            setState(1);
        }

        //PaulsMod.LOGGER.info("Energy: " + energy);
        if (inventory.get(0).isEmpty()) {
            return;
        }
        //PaulsMod.LOGGER.info("Alter has item!");

        ItemStack stack = inventory.get(0);
        if (isItemFuel(level, stack)) {
            energy += level.fuelValues().burnDuration(stack);
            stack.shrink(1);
            this.setChanged();
        }
        else if (stack.isDamageableItem()) {
            stack.setDamageValue(stack.getDamageValue() + 1);
            energy += 1;
            this.setChanged();
        }
        else if (stack.has(DataComponents.FOOD)) {
            FoodProperties food = stack.get(DataComponents.FOOD);
            if (food != null) {
                int nutrition = food.nutrition();       // Hunger points restored
                energy += nutrition;
                stack.shrink(1);
                this.setChanged();
            }
        }

        if (energy > 0) {
            setState(1);
        }
        //PaulsMod.LOGGER.info("Energy: {} State: {}", energy, state.getValue(AlterBlock.STATE));
    }

    public int getEnergy(int wantedEnergy) {
        int available = Math.min(energy, wantedEnergy);
        energy -= available;
        return available;
    }

    @Override
    public BlockEntity getContainerBlockEntity() {
        return this;
    }

    @Override
    public ItemStack getTheItem() {
        return inventory.getFirst();
    }

    @Override
    public void setTheItem(ItemStack itemStack) {
        setChanged();
        inventory.set(0, itemStack.copyWithCount(1));
    }

    @Override
    public void clearContent() {
        inventory.set(0, ItemStack.EMPTY);
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, this.inventory);
    }

    /* SAVING DATA */
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("state", this.state);
        output.putInt("energy", this.energy);
        ContainerHelper.saveAllItems(output, this.inventory);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.state = input.getIntOr("state", 0);
        this.energy = input.getIntOr("energy", 0);
        this.inventory.clear();
        ContainerHelper.loadAllItems(input, this.inventory);
    }



    /* BLOCK ENTITY SYNC METHODS */
    @Override
    public void setChanged() {
        super.setChanged();
        if(!level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void setState(int state) {
        this.state = state;
        BlockState blockState = this.getBlockState();
        level.setBlock(this.worldPosition, blockState.setValue(AlterBlock.STATE, state), 3);
        this.setChanged();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

}
