package net.lapointep.paulsmod.item.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.custom.SorterBlock;
import net.lapointep.paulsmod.block.entity.SorterBlockEntity;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class SortingWandItem extends Item {

    public SortingWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isCrouching()) {
            if (!level.isClientSide()) {
                PaulsCommon.ItemTypes currentType = getSelectedItemType(stack);
                PaulsCommon.ItemTypes[] values = PaulsCommon.ItemTypes.values();
                int nextIndex = (currentType.ordinal() + 1) % values.length;
                PaulsCommon.ItemTypes nextType = values[nextIndex];
                setSelectedItemType(player, stack, nextType);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();

        if (level.isClientSide()) {
            return player.isCrouching() ? InteractionResult.SUCCESS : super.useOn(context);
        }

        if (player.isCrouching()) {
            // Check for Container
            BlockEntity blockEntity = level.getBlockEntity(clickedPos);
            if (blockEntity instanceof Container) {
                getStoredBlockPos(stack).ifPresent(sorterPos -> {
                    if (level.getBlockEntity(sorterPos) instanceof SorterBlockEntity sorter) {
                        PaulsCommon.ItemTypes selectedType = getSelectedItemType(stack);
                        sorter.addBlockPosForItemType(selectedType, clickedPos);
                        player.sendSystemMessage(Component.literal("Added " + selectedType + " position: " + clickedPos.toShortString()));
                    }
                });
                return InteractionResult.SUCCESS;
            }

            // If crouching but not clicking a special block, we still want to return SUCCESS
            // to prevent fallback to use(), which would cycle the item type.
            // BUT wait, if we return SUCCESS here, use() is NOT called.
            // If the user shift-clicks a random block, should it cycle?
            // "If the player shift-clicks a block position ... it should not change the selected item type."
            // This means it should NOT cycle.
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
       // return super.useOn(context);
    }

    public void setStoredBlockPos(ItemStack stack, BlockPos pos) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }

    public Optional<BlockPos> getStoredBlockPos(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag nbt = customData.copyTag();
            if (nbt.contains("x") && nbt.contains("y") && nbt.contains("z")) {
                return Optional.of(new BlockPos(nbt.getIntOr("x", 0), nbt.getIntOr("y", 0), nbt.getIntOr("z", 0)));
            }
        }
        return Optional.empty();
    }

    public void setSelectedItemType(Player player, ItemStack stack, PaulsCommon.ItemTypes type) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putString("SelectedItemType", type.name());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        player.sendSystemMessage(Component.literal("Selected Item Type: " +  type.name()));
    }

    public PaulsCommon.ItemTypes getSelectedItemType(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag nbt = customData.copyTag();
            if (nbt.contains("SelectedItemType")) {
                try {
                    return PaulsCommon.ItemTypes.valueOf(nbt.getStringOr("SelectedItemType", "Misc"));
                } catch (IllegalArgumentException e) {
                    return PaulsCommon.ItemTypes.Misc;
                }
            }
        }
        return PaulsCommon.ItemTypes.Misc;
    }
}
