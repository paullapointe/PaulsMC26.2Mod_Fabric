package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.block.custom.SpawnerBlock;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.cubemob.MagmaCube;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jspecify.annotations.Nullable;

import java.util.List;


public class SpawnerBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem {
    public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    public static final int MAX_COUNT = 20*10;
    BETicker ticker = new BETicker(MAX_COUNT);
    public SpawnerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.SPAWNER, worldPosition, blockState);
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
        inventory.set(0, itemStack.copyWithCount(1));
        setChanged();
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerSingleItem.BlockContainerSingleItem.super.removeItem(slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
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
        ContainerHelper.saveAllItems(output, this.inventory);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.inventory.clear();
        ContainerHelper.loadAllItems(input, this.inventory);
    }


    /* BLOCK ENTITY SYNC METHODS */
    @Override
    public void setChanged() {
        super.setChanged();
        if(level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, SpawnerBlockEntity blockEntity) {
        if (level instanceof ServerLevel) {
            blockEntity.tick((ServerLevel) level, pos, state);
        }
    }

    public void tick(ServerLevel serverLevel, BlockPos pos, BlockState state) {
        if (!state.getValue(SpawnerBlock.POWERED)) { return; }
        if (ticker.notReadyToRun()) { return; }
        //PaulsMod.LOGGER.info("Spawner Ticking {}", pos);
        ItemStack itemStack = inventory.getFirst();
        if (itemStack.isEmpty()) { return; }

        List<Entity> currentEntities = PaulsCommon.getEntitiesInRange(serverLevel, pos, 1);
        if (!currentEntities.isEmpty()) {
            //PaulsMod.LOGGER.info("Spawner already has entities {}", currentEntities);
            return;
        }

        LivingEntity target = null;
        BlockPos blockPos = pos.west();
        if (itemStack.is(Items.ROTTEN_FLESH)) {
            target = new Zombie(EntityTypes.ZOMBIE, serverLevel);
        }
        else if (itemStack.is(Items.BONE)) {
            target = new Skeleton(EntityTypes.SKELETON, serverLevel);
        }
        else if (itemStack.is(Items.SPIDER_EYE)) {
            target = new Spider(EntityTypes.SPIDER, serverLevel);
        }
        else if (itemStack.is(Items.BLAZE_ROD)) {
            target = new Blaze(EntityTypes.BLAZE, serverLevel);
        }
        else if (itemStack.is(Items.GUNPOWDER)) {
            target = new Creeper(EntityTypes.CREEPER, serverLevel);
        }
        else if (itemStack.is(Items.BREEZE_ROD)) {
            target = new Breeze(EntityTypes.BREEZE, serverLevel);
        }
        else if (itemStack.is(Items.SLIME_BALL)) {
            target = new Slime(EntityTypes.SLIME, serverLevel);
        }
        else if (itemStack.is(Items.MAGMA_CREAM)) {
            target = new MagmaCube(EntityTypes.MAGMA_CUBE, serverLevel);
        }
        else if (itemStack.is(Items.ENDER_PEARL)) {
            target = new EnderMan(EntityTypes.ENDERMAN, serverLevel);
        }

        if (target != null) {
            //PaulsMod.LOGGER.info("Spawning {}", target.getName());
            // 2. Position the entity at the center of the target block
            target.teleportTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);

            // 3. Add the entity to the server level
            serverLevel.addFreshEntity(target);
        }
    }
}
