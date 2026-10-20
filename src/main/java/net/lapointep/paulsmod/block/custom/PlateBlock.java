package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.entity.ClockBlockEntity;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.block.entity.PlateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlateBlock extends BaseEntityBlock {

    public static final int MAX_STATE = 2;
    public static final MapCodec<PlateBlock> CODEC = simpleCodec(PlateBlock::new);
    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, MAX_STATE);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public PlateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PlateBlockEntity plateBlockEntity) {
                ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack stackInPlate = plateBlockEntity.getItem(0);

                if (stackInPlate.isEmpty() && !stackInHand.isEmpty()) {
                    // Place item
                    PaulsMod.LOGGER.info("Placing item in plate");
                    plateBlockEntity.setItem(0, stackInHand.split(1));
                    plateBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                } else if (!stackInPlate.isEmpty()) {
                    // Take item
                    if (stackInHand.isEmpty()) {
                        PaulsMod.LOGGER.info("Taking item from plate");
                        player.setItemInHand(InteractionHand.MAIN_HAND, plateBlockEntity.removeItem(0, 1));
                    } else {
                        ItemStack removedItem = plateBlockEntity.removeItem(0, 1);
                        if (!player.getInventory().add(removedItem)) {
                            player.drop(removedItem, false);
                        }
                    }
                    plateBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PlateBlockEntity plateBlockEntity) {
                Containers.dropContents(level, pos, plateBlockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(pos, state);
    }

}
