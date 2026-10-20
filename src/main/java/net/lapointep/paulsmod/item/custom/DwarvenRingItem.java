package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DwarvenRingItem extends Item {
    public DwarvenRingItem(Properties properties) {
        super(properties);
    }
    public TargetType currentTarget = TargetType.IRON;

    public enum TargetType {
        IRON,
        DIAMOND,
        EMERALD,
        AMETHYST,
        ANCIENT_DEBRIS,
        SEEING_POOL;

        // Cache values() to avoid allocating a new array on every call
        private static final TargetType[] VALUES = values();

        public TargetType next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level instanceof ServerLevel serverLevel) {
            if (player instanceof ServerPlayer serverPlayer) {


                if (player.isShiftKeyDown()) {
                    currentTarget = currentTarget.next();
                    player.sendSystemMessage(Component.literal("Looking for " + currentTarget + "s"));
                    return InteractionResult.SUCCESS;
                }

                ItemStack offhandItem = player.getOffhandItem();
                if (offhandItem.is(ModItems.GUIDE_STONE))
                {
                    BlockPos pos = findClosestBlock(level, serverPlayer.blockPosition(), currentTarget,10);

                    if (pos == null) {
                        BlockPos targetPos = serverPlayer.blockPosition();
                        serverLevel.sendParticles(ParticleTypes.PORTAL, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 32, 0.5, 0.5, 0.5, 0.1);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.BLOCKS, 1.0f, 1.0f);

                        return InteractionResult.SUCCESS;
                    }

                    GuideStoneItem guideStone = (GuideStoneItem) offhandItem.getItem();
                    guideStone.saveDestination(offhandItem, pos, level.dimension(), player);
                }
            }
        }


        return InteractionResult.SUCCESS;
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
        }
        return closestPos;

    }
}
