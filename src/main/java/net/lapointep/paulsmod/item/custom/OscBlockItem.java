package net.lapointep.paulsmod.item.custom;
 
import net.lapointep.paulsmod.block.entity.LavaCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class OscBlockItem extends BlockItem {
    public OscBlockItem(Block block, Properties properties) {
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
                saveDestination(stack, pos, level, player);
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    private void saveDestination(ItemStack stack, BlockPos pos, Level level, Player player) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putString("dim", level.dimension().identifier().toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        player.sendSystemMessage(Component.translatable("item.paulsmod.osc.destination_set",
                pos.getX(), pos.getY(), pos.getZ(), level.dimension().identifier().toString()));
    }
}
