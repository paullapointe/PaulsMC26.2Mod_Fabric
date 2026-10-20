package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.entity.BrewerBlockEntity;
import net.lapointep.paulsmod.block.entity.StatueBlockEntity;
import net.lapointep.paulsmod.block.entity.StainGlassBlockEntity;
import net.lapointep.paulsmod.block.entity.StoreShelfBlockEntity;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WrenchItem extends Item {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!level.isClientSide()) {
            if (context.getPlayer().isShiftKeyDown()) {
                if (blockEntity instanceof StoreShelfBlockEntity storeShelfBlockEntity) {
                    storeShelfBlockEntity.cycleCost(level, pos, state);
                    return InteractionResult.SUCCESS;
                }
                else if (blockEntity instanceof StatueBlockEntity statueBlockEntity) {
                    statueBlockEntity.cycleState(level, pos, state);
                    return InteractionResult.SUCCESS;
                }
                else if (blockEntity instanceof StainGlassBlockEntity stainglassBlockEntity) {
                    stainglassBlockEntity.cycleState(level, pos, state);
                    return InteractionResult.SUCCESS;
                }
                else {
                    PaulsCommon.rotateBlock(level, pos);
                    return InteractionResult.SUCCESS;
                }
            }
            else {
                //PaulsMod.LOGGER.info("Rotating block");
                PaulsCommon.rotateBlock(level, pos);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.SUCCESS;
    }
}
