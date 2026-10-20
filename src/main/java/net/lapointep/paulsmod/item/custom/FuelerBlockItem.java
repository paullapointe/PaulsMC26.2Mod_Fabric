package net.lapointep.paulsmod.item.custom;
 
import net.lapointep.paulsmod.block.entity.FuelerBlockEntity;
import net.lapointep.paulsmod.block.entity.LavaCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FuelerBlockItem extends BlockItem {
    public FuelerBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (player != null && player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof LavaCoreBlockEntity lavaCoreBlockEntity) {
                    saveDestination(stack, pos, level, player);
                } else if (blockEntity instanceof Container container) {
                    saveStorageDestination(stack, pos, level, player);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean updated = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FuelerBlockEntity fuelerBlockEntity) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                CompoundTag tag = customData.copyTag();
                if (tag.contains("x") && tag.contains("y") && tag.contains("z") && tag.contains("dim")) {
                    fuelerBlockEntity.setTarget(new BlockPos(tag.getInt("x").orElse(0), tag.getInt("y").orElse(0), tag.getInt("z").orElse(0)), tag.getString("dim").orElse("minecraft:overworld"));
                    updated = true;
                }
                if (tag.contains("storageX") && tag.contains("storageY") && tag.contains("storageZ") && tag.contains("storageDim")) {
                    fuelerBlockEntity.setStorageTarget(new BlockPos(tag.getInt("storageX").orElse(0), tag.getInt("storageY").orElse(0), tag.getInt("storageZ").orElse(0)), tag.getString("storageDim").orElse("minecraft:overworld"));
                    updated = true;
                }
            }
        }
        return updated;
    }

    private void saveDestination(ItemStack stack, BlockPos pos, Level level, Player player) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putString("dim", level.dimension().identifier().toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        player.sendSystemMessage(Component.translatable("item.paulsmod.fueler.destination_set",
                pos.getX(), pos.getY(), pos.getZ(), level.dimension().identifier().toString()));
    }

    private void saveStorageDestination(ItemStack stack, BlockPos pos, Level level, Player player) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putInt("storageX", pos.getX());
        nbt.putInt("storageY", pos.getY());
        nbt.putInt("storageZ", pos.getZ());
        nbt.putString("storageDim", level.dimension().identifier().toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        player.sendSystemMessage(Component.translatable("item.paulsmod.fueler.storage_destination_set",
                pos.getX(), pos.getY(), pos.getZ(), level.dimension().identifier().toString()));
    }
}
