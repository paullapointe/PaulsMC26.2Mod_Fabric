package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DwarvenRingItem extends Item {
    public DwarvenRingItem(Properties properties) {
        super(properties);
    }
    public BlockState TargetBlockState = Blocks.AIR.defaultBlockState();
    public BlockPos targetPos = null;

    public enum TargetType {
        IRON,
        DIAMOND,
        EMERALD,
        AMETHYST,
        ANCIENT_DEBRIS,
        SEEING_POOL,
        GOLD,
        LAPIS,
        REDSTONE;

        // Cache values() to avoid allocating a new array on every call
        private static final TargetType[] VALUES = values();

        public TargetType next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if (player != null && player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                BlockState state = level.getBlockState(pos);
                this.TargetBlockState = state;
                player.sendSystemMessage(Component.literal("Target set to: " + state.getBlock().getName().getString()));

            }
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        ItemStack ringStack = player.getItemInHand(hand);

        if (level instanceof ServerLevel serverLevel) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (player.isShiftKeyDown()) {
                    // Check if the player is holding an offhand item
                    ItemStack offhandItem = player.getOffhandItem();
                    if (offhandItem.isEmpty()) {
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK , SoundSource.BLOCKS, 1.0f, 1.0f);
                        player.sendSystemMessage(Component.literal("No item in offhand"));
                        return InteractionResult.SUCCESS;
                    }

                    String itemName = offhandItem.getItem().getDescriptionId();
                    TargetType currentTarget = getCurrentTargetType(itemName);
                    BlockPos foundPos = findClosestBlock(level, serverPlayer.blockPosition(), currentTarget, 16);

                    if (foundPos != null) {
                        TargetBlockState = null;
                        BlockPos playerPos = serverPlayer.blockPosition();
                        serverLevel.sendParticles(ParticleTypes.PORTAL, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 32, 0.5, 0.5, 0.5, 0.1);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.BLOCKS, 1.0f, 1.0f);
                        this.saveDestination(ringStack, foundPos.offset(0, -1, 0), level.dimension(), player);
                    } else {
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK , SoundSource.BLOCKS, 1.0f, 1.0f);
                        player.sendSystemMessage(Component.literal("No " + itemName + "s found"));
                    }
                    return InteractionResult.SUCCESS;
                }

                if (TargetBlockState != null) {
                    BlockPos foundPos = findClosestBlock(level, serverPlayer.blockPosition(), TargetBlockState, 16);
                    if (foundPos != null) {
                        this.saveDestination(ringStack, foundPos, level.dimension(), player);
                        PaulsCommon.showTheWay(serverLevel, serverPlayer, this.targetPos.offset(0, -1, 0));
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                else {
                    // The player is not sneaking, show the guide particles
                    if (this.targetPos != null) {
                        PaulsCommon.showTheWay(serverLevel, serverPlayer, this.targetPos);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                    else {
                       // player.sendSystemMessage(Component.literal("No targetPos set"));
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    TargetType getCurrentTargetType(String itemName) {
        itemName = itemName.toLowerCase();
        if (itemName.contains("diamond")) {
            return TargetType.DIAMOND;
        }
        else if (itemName.contains("emerald")) {
            return TargetType.EMERALD;
        }
        else if (itemName.contains("amethyst")) {
            return TargetType.AMETHYST;
        }
        else if (itemName.contains("ancient_debris") || itemName.contains("netherite")) {
            return TargetType.ANCIENT_DEBRIS;
        }
        else if (itemName.contains("iron")) {
            return TargetType.IRON;
        }
        else if (itemName.contains("gold")) {
            return TargetType.GOLD;
        }
        else if (itemName.contains("lapis")) {
            return TargetType.LAPIS;
        }
        else if (itemName.contains("redstone")) {
            return TargetType.REDSTONE;
        }

        return null;
    }

    public void saveDestination(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension, Player player) {
        this.targetPos = pos;
        net.minecraft.world.item.component.CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putInt("destX", targetPos.getX());
            tag.putInt("destY", targetPos.getY());
            tag.putInt("destZ", targetPos.getZ());
        });

        if (player != null) {
            player.sendSystemMessage(Component.literal("Destination saved to ring!"));
        }
    }

    public static BlockPos findClosestBlock(LevelReader level, BlockPos center, BlockState targetState, int radius) {
        BlockPos closestPos = null;
        BlockPos minPos = center.offset(-radius, -radius, -radius);
        BlockPos maxPos = center.offset(radius, radius, radius);

        for (BlockPos currentPos : BlockPos.betweenClosed(minPos, maxPos)) {
            BlockState state = level.getBlockState(currentPos);
            if (state.equals(targetState)) {
                return currentPos.immutable();
            }
        }
        return null;
    }

    public static BlockPos findClosestBlock(LevelReader level, BlockPos center, TargetType targetType, int radius) {
        BlockPos closestPos = null;
        // Define the corners of the search cube
        BlockPos minPos = center.offset(-radius, -radius, -radius);
        BlockPos maxPos = center.offset(radius, radius, radius);

        // betweenClosed is highly optimized but reuses a single mutable BlockPos object
        for (BlockPos currentPos : BlockPos.betweenClosed(minPos, maxPos)) {
            BlockState state = level.getBlockState(currentPos);

            if ((targetType == TargetType.ANCIENT_DEBRIS) && (state.is(Blocks.ANCIENT_DEBRIS))) {
                closestPos = currentPos.immutable();
            }
            else if ((targetType == TargetType.DIAMOND) && (state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.DEEPSLATE_DIAMOND_ORE))) {
                closestPos = currentPos.immutable();
            }
            else if ((targetType == TargetType.EMERALD) && (state.is(Blocks.EMERALD_ORE))) {
                closestPos = currentPos.immutable();
            }
            else if ((targetType == TargetType.AMETHYST) && (state.is(Blocks.AMETHYST_BLOCK))) {
                closestPos = currentPos.immutable();
            }
            else if ((targetType == TargetType.SEEING_POOL) && (state.is(ModBlocks.SEEING_POOL))) {
                closestPos = currentPos.immutable();
            }
            else if ((targetType == TargetType.IRON) && ((state.is(Blocks.IRON_ORE))|| (state.is(Blocks.DEEPSLATE_IRON_ORE)))) {
                closestPos = currentPos.immutable();
            }
            else if ((targetType == TargetType.GOLD) && ((state.is(Blocks.GOLD_ORE))|| (state.is(Blocks.DEEPSLATE_GOLD_ORE)) || (state.is(Blocks.NETHER_GOLD_ORE)) )) {
                closestPos = currentPos.immutable();
            }
            else if ((targetType == TargetType.LAPIS) && ((state.is(Blocks.LAPIS_ORE))|| (state.is(Blocks.DEEPSLATE_LAPIS_ORE)))) {
                closestPos = currentPos.immutable();
            }
            else if ((targetType == TargetType.REDSTONE) && ((state.is(Blocks.REDSTONE_ORE))|| (state.is(Blocks.DEEPSLATE_REDSTONE_ORE)))) {
                closestPos = currentPos.immutable();
            }
        }
        return closestPos;
    }
}
