package net.lapointep.paulsmod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.lapointep.paulsmod.entity.TeleporterArrowEntity;

public class TeleporterArrowItem extends ArrowItem {

    public TeleporterArrowItem(Properties properties) {
        super(properties);
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
        }
        return super.useOn(context);
    }

    private void saveDestination(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension, Player player) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putString("dim", dimension.identifier().toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        player.sendSystemMessage(Component.translatable("item.paulsmod.teleporter_arrow.destination_set",
                pos.getX(), pos.getY(), pos.getZ(), dimension.identifier().toString()));
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter, @org.jetbrains.annotations.Nullable ItemStack weapon) {
        TeleporterArrowEntity arrow = new TeleporterArrowEntity(level, shooter, stack.copyWithCount(1), weapon);
        return arrow;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        TeleporterArrowEntity teleporterArrowEntity = new TeleporterArrowEntity(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        teleporterArrowEntity.pickup = AbstractArrow.Pickup.ALLOWED;
        return teleporterArrowEntity;
    }
}
