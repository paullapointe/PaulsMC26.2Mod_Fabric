package net.lapointep.paulsmod.mixin;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.potions.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin {


    @Inject(method = "onHitBlock", at = @At("HEAD"))
    private void onProjectileHitBlock(BlockHitResult blockHitResult, CallbackInfo ci) {
        Projectile projectile = (Projectile) (Object) this;
        //PaulsMod.LOGGER.info("Projectile hit block");
        // 1. Verify this projectile is actually a Splash Potion
        if (projectile instanceof ThrownSplashPotion potionEntity) {
            PaulsMod.LOGGER.info("Projectile is a Splash Potion");
            // 2. Ensure we only run logic on the server to prevent desyncs
            Level level = potionEntity.level();
            if (potionEntity.level().isClientSide()) return;
            if (level instanceof ServerLevel serverLevel) {
                ItemStack stack = potionEntity.getItem();
                PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

                // 3. Check if the potion contents match your target potion
                BlockPos hitPos = blockHitResult.getBlockPos();
                BlockState blockState = level.getBlockState(hitPos);

                if (contents.is(ModPotions.NETHERRACK_POTION)) {
                    PaulsMod.LOGGER.info("Projectile is a NETHERRACK Potion");
                    if (blockState.is(Blocks.NETHERRACK)) {
                        PaulsMod.LOGGER.info("Projectile hit a NETHERRACK");
                        level.setBlockAndUpdate(hitPos, ModBlocks.UNSTABLENETHERRACK.defaultBlockState());
                    }
                }
                else if (contents.is(ModPotions.LAVA_POTION)) {
                    BlockPos minPos = hitPos.offset(-1, -1, -1);
                    BlockPos maxPos = hitPos.offset(1, 1, 1);
                    for (BlockPos currentPos : BlockPos.betweenClosed(minPos, maxPos)) {
                        BlockState blockState2 = level.getBlockState(currentPos);
                        if (blockState2.is(Blocks.LAVA)) {
                            level.setBlockAndUpdate(currentPos, ModBlocks.UNSTABLELAVA.defaultBlockState());
                        }
                    }
                }
                else if (contents.is(ModPotions.WATER_POTION)) {
                    BlockPos minPos = hitPos.offset(-1, -1, -1);
                    BlockPos maxPos = hitPos.offset(1, 1, 1);

                    for (BlockPos currentPos : BlockPos.betweenClosed(minPos, maxPos)) {
                        BlockState blockState2 = level.getBlockState(currentPos);
                        if (blockState2.is(Blocks.WATER)) {
                            level.setBlockAndUpdate(currentPos, ModBlocks.UNSTABLEWATER.defaultBlockState());
                        }
                    }
                }
                else if (contents.is(ModPotions.DIRT_POTION)) {
                    if (blockState.is(Blocks.DIRT) || blockState.is(Blocks.COARSE_DIRT)) {
                        level.setBlockAndUpdate(hitPos, ModBlocks.UNSTABLEDIRT.defaultBlockState());
                    }
                }
                else if (contents.is(ModPotions.STONE_POTION)) {
                    if (blockState.is(Blocks.STONE)) {
                        level.setBlockAndUpdate(hitPos, ModBlocks.UNSTABLESTONE.defaultBlockState());
                    }
                }
                else if (contents.is(ModPotions.SCULKICIDE_POTION)) {
                    if (
                            (blockState.is(Blocks.SCULK)) ||
                                    (blockState.is(Blocks.SCULK_CATALYST)) ||
                                    (blockState.is(Blocks.SCULK_VEIN)) ||
                                    (blockState.is(Blocks.SCULK_SHRIEKER))
                    ) {
                        clearSculkAround(serverLevel, hitPos);
                    }
                }
                /*
                else if (contents.is(ModPotions.GLUE_POTION)) {
                    ItemEntity itemEntity = new ItemEntity(level, hitPos.getX() + 0.5, hitPos.getY() + 1.0, hitPos.getZ() + 0.5, new ItemStack(net.lapointep.paulsmod.item.ModItems.GLUE));
                    level.addFreshEntity(itemEntity);
                }*/
            }



        }
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void onProjectileHitEntity(net.minecraft.world.phys.EntityHitResult entityHitResult, CallbackInfo ci) {
        Projectile projectile = (Projectile) (Object) this;
        if (projectile instanceof ThrownSplashPotion potionEntity) {
            Level level = potionEntity.level();
            if (level.isClientSide()) return;

            ItemStack stack = potionEntity.getItem();
            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

          /*  if (contents.is(ModPotions.GLUE_POTION)) {
                Entity entity = entityHitResult.getEntity();
                if (entity instanceof net.minecraft.world.entity.player.Player player) {
                    ItemStack glueStack = new ItemStack(net.lapointep.paulsmod.item.ModItems.GLUE);
                    if (!player.getInventory().add(glueStack)) {
                        player.drop(glueStack, false);
                    }
                } else {
                    ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(net.lapointep.paulsmod.item.ModItems.GLUE));
                    level.addFreshEntity(itemEntity);
                }
            }
           */
        }
    }

    void clearSculkAround(ServerLevel level, BlockPos center) {
        final int SCULK_RANGE = 5;
        final int SCULK_VERTICAL_RANGE = 3;

        BlockPos minPos = center.offset(-SCULK_RANGE, -SCULK_VERTICAL_RANGE, -SCULK_RANGE);
        BlockPos maxPos = center.offset(SCULK_RANGE, SCULK_VERTICAL_RANGE, SCULK_RANGE);

        // Iterate through all blocks in the bounding box
        for (BlockPos currentPos : BlockPos.betweenClosed(minPos, maxPos)) {
            BlockState state = level.getBlockState(currentPos);
            Block block = state.getBlock();

            int xpToDrop = -1;

            // Determine if it's a sculk block and how much XP it should drop
            if (block == Blocks.SCULK) {
                xpToDrop = 1;
            } else if (block == Blocks.SCULK_SENSOR || block == Blocks.CALIBRATED_SCULK_SENSOR ||
                    block == Blocks.SCULK_SHRIEKER || block == Blocks.SCULK_CATALYST) {
                xpToDrop = 5;
            } else if (block == Blocks.SCULK_VEIN) {
                xpToDrop = 0; // Veins don't drop XP, but we still want to destroy them
            }

            // If an XP value was assigned, it means we found a sculk block
            if (xpToDrop >= 0) {
                // Get the center of the block before destroying it
                Vec3 dropLocation = Vec3.atCenterOf(currentPos);

                // Destroy the block.
                // We pass 'false' for dropItems because sculk blocks don't drop items naturally
                // without Silk Touch, and we are handling the XP drop manually.
                level.destroyBlock(currentPos, false);

                // Spawn the experience orb
                if (xpToDrop > 0) {
                    ExperienceOrb.award(level, dropLocation, xpToDrop);
                }
            }
        }
    }
}