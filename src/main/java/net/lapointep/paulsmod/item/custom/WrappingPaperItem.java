package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import static net.minecraft.world.level.block.state.BlockBehaviour.simpleCodec;

public class WrappingPaperItem extends Item {

    public WrappingPaperItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // Game logic modifying inventories and data components MUST run on the server
        if (!level.isClientSide()) {

            // Reference your registered item directly.
            // Replace 'ModItems.PRESENT' with whatever your registry reference is named.
            ItemStack present = new ItemStack(ModItems.PRESENT);

            wrapPresent(present, player.blockPosition(), level.dimension(), player);

            player.setItemInHand(hand, present);
            player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }

        // Return SUCCESS to indicate the action was performed
        return InteractionResult.SUCCESS;
    }

    private void wrapPresent(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension, Player player) {
        ItemStack toWrap = player.getOffhandItem();
        if (toWrap.isEmpty()) return;

        ItemStack wrapped = toWrap.copy();

        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            ItemStack.CODEC.encodeStart(player.level().registryAccess().createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE), wrapped)
                    .ifSuccess(element -> tag.put("WrappedItem", element));
        });
    }
}
