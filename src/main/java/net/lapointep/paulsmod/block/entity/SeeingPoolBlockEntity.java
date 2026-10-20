package net.lapointep.paulsmod.block.entity;


import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.custom.SeeingPoolBlock;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.item.custom.GuideStoneItem;
import net.lapointep.paulsmod.util.Destination;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jspecify.annotations.Nullable;

import static net.lapointep.paulsmod.util.PaulsCommon.PendingChatMessages;
import static net.lapointep.paulsmod.util.PaulsCommon.isItemFuel;

public class SeeingPoolBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem {
    public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private int state = 0;
    private int energy = 0;
    public static final int MAX_COUNT = 20*1;
    BETicker ticker = new BETicker(MAX_COUNT);
    public static final int WAITING_SECONDS = 30;
    public int isWaiting = WAITING_SECONDS;

    public static final int NEEDED_ENERGY = 100;

    public SeeingPoolBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.SEEING_POOL, worldPosition, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SeeingPoolBlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            blockEntity.tick(serverLevel, pos, state);
        }
    }

    public void tick(ServerLevel serverLevel, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }

        //PaulsMod.LOGGER.info("Energy: %d waiting: %d", energy, isWaiting);
        if (energy == 0) {
            setState(0);
        }
        else {
            setState(1);
        }

        if (!inventory.getFirst().isEmpty()) {
            //PaulsMod.LOGGER.info("Seeing pool has item!");

            ItemStack stack = inventory.getFirst();
            if (isItemFuel(serverLevel, stack)) {
                energy += serverLevel.fuelValues().burnDuration(stack);
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
        }

        if (isWaiting > 0) {
            isWaiting--;
            PaulsMod.LOGGER.info("Waiting for player to say something. {} seconds left.", isWaiting);
            PaulsCommon.PendingChatMessage pending = PendingChatMessages.poll();
            if (pending == null) {
                PaulsMod.LOGGER.info("No pending chat message found.");
                return;
            }
            String message = pending.message().toLowerCase();
            ServerPlayer player = pending.player();
            PendingChatMessages.clear();
            PaulsMod.LOGGER.info("Player {} said: {}", player.getName().getString(), message);
            BlockPos targetPos = PaulsCommon.FindStructureMatching(serverLevel, message, pos);
            if (targetPos == null) {
                PaulsMod.LOGGER.info("No structure found matching {}", message);
                player.sendSystemMessage(Component.literal("What you seek cannot be seen."));
                return;
            }


            BlockPos abovePos = pos.above();
            targetPos = targetPos.offset(0, abovePos.getY() ,0);
            PaulsMod.LOGGER.info("Found structure at {}. Creating guide stone.", targetPos);

            ItemStack guideStoneStack = new ItemStack(ModItems.GUIDE_STONE);
            ((GuideStoneItem)ModItems.GUIDE_STONE).saveDestination(guideStoneStack, targetPos, serverLevel.dimension(), player);

            ItemEntity itemEntity = new ItemEntity(serverLevel, abovePos.getX() + 0.5, abovePos.getY() + 0.5, abovePos.getZ() + 0.5, guideStoneStack);
            serverLevel.addFreshEntity(itemEntity);
            
            player.sendSystemMessage(Component.literal("Follow this stone to your fate."));
            isWaiting = 0;
            this.energy = 0;

            return;
        }
        else if (energy > NEEDED_ENERGY) {
            PaulsMod.LOGGER.info("Energy is high enough to search for a structure. {}", energy);
            // Ask the player for something to search for
            Component coloredMessage = Component.literal("The fates are listening. Say what you seek.")
                    .withStyle(ChatFormatting.GREEN);
            serverLevel.getServer().getPlayerList().broadcastSystemMessage(coloredMessage, false);
            isWaiting = WAITING_SECONDS;
        }

        PaulsMod.LOGGER.info("Energy: {} State: {}", energy, state.getValue(SeeingPoolBlock.STATE));
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
        if (level == null) { return; }
        if(!level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void setState(int state) {
        if (level == null) { return; }
        this.state = state;
        BlockState blockState = this.getBlockState();
        level.setBlock(this.worldPosition, blockState.setValue(SeeingPoolBlock.STATE, state), 3);
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
