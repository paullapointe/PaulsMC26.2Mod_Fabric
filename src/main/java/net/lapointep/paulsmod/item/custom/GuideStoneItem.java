package net.lapointep.paulsmod.item.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.entity.GuideEntity;
import net.lapointep.paulsmod.entity.ModEntities;
import net.lapointep.paulsmod.util.Destination;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class GuideStoneItem extends Item {

    public GuideStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // 1. Verify we are on the logical server (optional, depends on your item)
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        ItemStack stack = hand.equals(InteractionHand.MAIN_HAND) ? player.getItemInHand(InteractionHand.MAIN_HAND) : player.getItemInHand(InteractionHand.OFF_HAND);
        // 2. Your custom item logic here

        if (level instanceof ServerLevel serverLevel) {
            if (player instanceof ServerPlayer serverPlayer) {
                showTheWay(serverLevel, serverPlayer, stack);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player != null && player.isCrouching()) {
            if (!level.isClientSide()) {
                BlockPos pos = context.getClickedPos();
                saveDestination(stack, pos, level.dimension(), player);
            }
            return InteractionResult.SUCCESS;
        } else {
            if (level instanceof ServerLevel serverLevel) {
                if (player instanceof ServerPlayer serverPlayer) {
                    showTheWay(serverLevel, serverPlayer, stack);
                    //player.getCooldowns().addCooldown(this, 20); // 1 sec cooldown
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    public void showTheWay(ServerLevel serverLevel, ServerPlayer serverPlayer, ItemStack stack) {
        getDestination(stack).ifPresent(dest -> {
            BlockPos pos = dest.getPos();
            /*
            BlockPos diff = pos.subtract(serverPlayer.blockPosition());
            serverPlayer.sendSystemMessage(net.minecraft.network.chat.Component.translatable("item.paulsmod.guide_stone.message",
                    diff.getX(), diff.getY(), diff.getZ()));
            */
            // Create and position entity at player eye height
            GuideEntity guide = ModEntities.GUIDE_ENTITY.create(serverLevel, EntitySpawnReason.TRIGGERED);
            if (guide != null) {
                PaulsMod.LOGGER.info("Creating guide entity at " + pos);
                Vec3 spawnPos = serverPlayer.getEyePosition().add(serverPlayer.getLookAngle().scale(0.5));

                // Set position and rotation using explicit Mojang mapping setters
                guide.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                guide.setYRot(serverPlayer.getYRot());
                guide.setXRot(serverPlayer.getXRot());

                // Sync old position and rotation to prevent client-side interpolation glitches from (0,0,0)
                guide.setOldPosAndRot();

                Vec3 direction = dest.getDirectionToTarget(serverPlayer);
                guide.setGuideDirection(direction, 0.2); // 0.2 blocks/tick speed
                serverLevel.addFreshEntity(guide);
            }
            else {
                PaulsMod.LOGGER.error("Guide entity is null");
            }
        });
    }

    public void saveDestination(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension, Player player) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putString("dim", dimension.identifier().toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("item.paulsmod.guide_stone.destination_set"));
    }

    private Optional<Destination> getDestination(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag nbt = customData.copyTag();
            if (nbt.contains("x") && nbt.contains("y") && nbt.contains("z") && nbt.contains("dim")) {
                BlockPos pos = new BlockPos(nbt.getIntOr("x", 0), nbt.getIntOr("y", 0), nbt.getIntOr("z", 0));
                ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, Identifier.parse(nbt.getStringOr("dim", "minecraft:overworld")));
                return Optional.of(new Destination(pos, dimension));
            }
        }
        return Optional.empty();
    }
}
