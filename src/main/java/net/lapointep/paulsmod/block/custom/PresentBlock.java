package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.block.entity.PresentBlockEntity;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;

public class PresentBlock extends BaseEntityBlock {
    public static final MapCodec<PresentBlock> CODEC = simpleCodec(PresentBlock::new);

    public PresentBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable net.minecraft.world.level.redstone.Orientation orientation, boolean notify) {
        // Optional: keep it or remove it. BaseEntityBlock doesn't strictly need it.
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
                ItemStack stack = new ItemStack(ModItems.PRESENT);
                if (!presentBlockEntity.getWrappedItemTag().isEmpty()) {
                    CustomData.update(CUSTOM_DATA, stack, tag -> {
                        tag.put("WrappedItem", presentBlockEntity.getWrappedItemTag());
                    });
                }
                
                if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }
                
                presentBlockEntity.setRemovedByPlayer(true);
                level.removeBlock(pos, false);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PresentBlockEntity(pos, state);
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
                if (!presentBlockEntity.isRemovedByPlayer()) {
                    ItemStack stack = new ItemStack(ModItems.PRESENT);
                    if (!presentBlockEntity.getWrappedItemTag().isEmpty()) {
                        CustomData.update(CUSTOM_DATA, stack, tag -> {
                            tag.put("WrappedItem", presentBlockEntity.getWrappedItemTag());
                        });
                    }
                    Block.popResource(level, pos, stack);
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
