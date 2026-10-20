package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.block.custom.StickyNoteBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class StickyNoteBlockItem extends BlockItem {
    public StickyNoteBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public void setNoteText(ItemStack stack, String text) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putString("note_text", text);
        });
    }
    
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockPos placePos = pos.relative(face);
        Player player = context.getPlayer();

        if (player == null) {
            return InteractionResult.FAIL;
        }

        if (face.getAxis().isHorizontal() && level.getBlockState(placePos).canBeReplaced()) {
            if (!level.isClientSide()) {
                BlockState state = ModBlocks.STICKYNOTE.defaultBlockState().setValue(StickyNoteBlock.FACING, face);
                level.setBlock(placePos, state, 3);

                ItemStack stack = context.getItemInHand();
                net.minecraft.world.level.block.entity.BlockEntity blockEntity = level.getBlockEntity(placePos);
                if (blockEntity instanceof net.lapointep.paulsmod.block.entity.StickyNoteBlockEntity stickyNoteBlockEntity) {
                    net.minecraft.world.item.component.CustomData customData = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
                    if (customData != null) {
                        net.minecraft.nbt.CompoundTag tag = customData.copyTag();
                        if (tag.contains("note_text")) {
                            stickyNoteBlockEntity.setNoteText(tag.getStringOr("note_text", ""));
                        }
                    }
                }

                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            String initialText = "";
            net.minecraft.world.item.component.CustomData customData = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
            if (customData != null) {
                net.minecraft.nbt.CompoundTag tag = customData.copyTag();
                if (tag.contains("note_text")) {
                    initialText = tag.getStringOr("note_text", "");
                }
            }
            openStickyNoteScreen(initialText);
        }
        return InteractionResult.SUCCESS;
    }

    @net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
    private void openStickyNoteScreen(String initialText) {
        net.minecraft.client.Minecraft.getInstance().gui.setScreen(new net.lapointep.paulsmod.menu.StickyNoteScreen(initialText));
    }
}
