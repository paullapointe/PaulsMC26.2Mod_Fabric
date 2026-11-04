package net.lapointep.paulsmod.item.custom;

import com.mojang.datafixers.util.Unit;
import net.lapointep.paulsmod.effect.ModEffects;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FHammerItem extends Item {
    public FHammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand hand) {
        stack.setDamageValue(0);
        if (interactionTarget instanceof Enemy) {
            Level level = player.level();
            if (!level.isClientSide()) {
                RandomSource random = level.getRandom();
                // Deal random damage between 6 and 12
                float damage = 9.0f + random.nextFloat() * 9.0f;
                interactionTarget.hurt(player.damageSources().playerAttack(player), damage);

                // 25% chance to catch fire
                if (random.nextFloat() < 0.25f) {
                    interactionTarget.setRemainingFireTicks(5 * 20);
                }
                // 10% chance to be hit by lightning
                else if (random.nextFloat() < 0.10f) {
                    PaulsCommon.spawnLightningAt(level, interactionTarget.blockPosition(), true);
                }
                // 35% chance to trigger fear effect
                else if (random.nextFloat() < 0.35f) {
                    interactionTarget.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.FEAR), 10 * 20, 0));
                }


            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        if (level.isClientSide()) { return InteractionResult.SUCCESS; }
        if (level instanceof ServerLevel serverLevel) {
            if (player != null) {
                if (player.isSecondaryUseActive()) {
                    Direction facing = player.getDirection();
                    Direction side1 = (facing.getAxis() == Direction.Axis.Y) ? Direction.EAST : facing.getClockWise();
                    Direction side2 = (facing.getAxis() == Direction.Axis.Y) ? Direction.SOUTH : Direction.UP;

                    for (int d = 0; d < 5; d++) {
                        for (int s1 = -1; s1 <= 1; s1++) {
                            for (int s2 = -1; s2 <= 1; s2++) {
                                BlockPos targetPos = pos.relative(facing, d)
                                        .relative(side1, s1)
                                        .relative(side2, s2);

                                BlockState targetState = level.getBlockState(targetPos);
                                if (targetState.is(Blocks.STONE) ||
                                        targetState.is(Blocks.COBBLESTONE) ||
                                        targetState.is(Blocks.DIRT) ||
                                        targetState.is(Blocks.GRASS_BLOCK) ||
                                        targetState.is(Blocks.DEEPSLATE) ||
                                        targetState.is(Blocks.TUFF) ||
                                        targetState.is(Blocks.GRASS_BLOCK) ||
                                        targetState.is(Blocks.DIRT) ||
                                        targetState.is(Blocks.NETHERRACK) ||
                                        targetState.is(Blocks.NETHER_WART_BLOCK) ||
                                        targetState.is(Blocks.SOUL_SOIL) ||
                                        targetState.is(Blocks.SOUL_SAND) ||
                                        targetState.is(Blocks.ROOTED_DIRT) ||
                                        targetState.is(Blocks.LAVA)
                                ) {
                                    serverLevel.explode(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 0, Level.ExplosionInteraction.BLOCK);
                                    level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                                }
                                else {
                                    level.destroyBlock(targetPos, true);
                                }
                            }
                        }
                    }
                    return InteractionResult.SUCCESS;
                } else {
                    BlockState state = level.getBlockState(pos);
                    Block.popResource(level, pos, new ItemStack(state.getBlock()));
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level instanceof ServerLevel serverLevel) {
            if (player instanceof ServerPlayer serverPlayer) {

                if (player.isShiftKeyDown()) {

                    return InteractionResult.SUCCESS;
                }

            }
        }


        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState state) {
        return 30f;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack itemStack, BlockState state) {
        return true;
    }


}
