package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.block.entity.PresentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PresentItem extends BlockItem {

    public PresentItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {  // Change to server-side
                ItemStack stack = player.getItemInHand(hand);
                unwrapPresent(stack, player);
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, hand);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean updated = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                CompoundTag tag = customData.copyTag();
                if (tag.contains("WrappedItem")) {
                    tag.getCompound("WrappedItem").ifPresent(presentBlockEntity::setWrappedItemTag);
                    updated = true;
                }
            }
        }
        return updated;
    }

    private void unwrapPresent(ItemStack stack, Player player) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;

        CompoundTag tag = customData.copyTag();
        if (!tag.contains("WrappedItem")) return;

        ItemStack.CODEC.parse(
                player.level().registryAccess().createSerializationContext(NbtOps.INSTANCE),
                tag.get("WrappedItem")
        ).ifSuccess(unwrappedStack -> {
            // Handle the retrieved ItemStack
            player.setItemInHand(InteractionHand.MAIN_HAND, unwrappedStack);
        });
    }
}
