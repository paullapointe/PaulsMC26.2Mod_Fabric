package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.block.entity.TeleporterBlockEntity;
import net.lapointep.paulsmod.util.Destination;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class TravelingStoneItem extends Item {

    public TravelingStoneItem(Properties properties) {
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
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            getDestination(stack).ifPresent(dest -> {
                dest.teleport((Entity)serverPlayer);
                if (InteractionHand.MAIN_HAND.equals(hand)) {
                    stack.hurtAndBreak(1, serverPlayer, EquipmentSlot.MAINHAND);
                }
                else {
                    stack.hurtAndBreak(1, serverPlayer, EquipmentSlot.OFFHAND);
                }
            });
        }

        return InteractionResult.SUCCESS;
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();

        if (player != null && player.isCrouching()) {
            if (!level.isClientSide()) {
                BlockState state = level.getBlockState(pos);
                if (state.is(ModBlocks.TELEPORTER)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TeleporterBlockEntity teleporterBlockEntity) {
                        getDestination(stack).ifPresent(dest -> {
                            teleporterBlockEntity.setTarget(dest.getPos(), dest.getDimension().identifier().toString());
                            player.sendSystemMessage(Component.translatable("item.paulsmod.teleporter.destination_set",
                                    dest.getPos().getX(), dest.getPos().getY(), dest.getPos().getZ(), dest.getDimension().identifier().toString()));
                        });
                        return InteractionResult.SUCCESS;
                    }
                }
                saveDestination(stack, pos, level.dimension(), player);
            }
            return InteractionResult.SUCCESS;
        } else {
            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                getDestination(stack).ifPresent(dest -> {
                    dest.teleport((Entity)serverPlayer);
                    stack.hurtAndBreak(1, serverPlayer, context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                });
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand hand) {
        Level level = player.level();
        if (!level.isClientSide()) {
            getDestination(stack).ifPresent(dest -> {
                dest.teleport(interactionTarget);
                if (player instanceof ServerPlayer serverPlayer) {
                    stack.hurtAndBreak(1, serverPlayer, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                }
            });
        }
        return InteractionResult.SUCCESS;
    }

    private void saveDestination(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension, Player player) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putString("dim", dimension.identifier().toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("item.paulsmod.traveling_stone.destination_set",
                pos.getX(), pos.getY(), pos.getZ(), dimension.identifier().toString()));
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
