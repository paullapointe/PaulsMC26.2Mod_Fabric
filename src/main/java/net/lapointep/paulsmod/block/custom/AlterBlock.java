package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.block.entity.AltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AlterBlock extends BaseEntityBlock {

    public static final int MAX_STATE = 1;
    public static final MapCodec<AlterBlock> CODEC = simpleCodec(AlterBlock::new);
    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, MAX_STATE);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public AlterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, 0));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Spawn particles roughly 20% of client ticks
        int stateValue = state.getValue(STATE);
        if (stateValue == 1) {
            if (random.nextFloat() < 0.8f) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
                double y = pos.getY() + 1.05;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;

                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0.0, 0.03, 0.0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AltarBlockEntity altarBlockEntity) {
                ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack stackInAlter = altarBlockEntity.getItem(0);

                if (stackInAlter.isEmpty() && !stackInHand.isEmpty()) {
                    // Place item
                    PaulsMod.LOGGER.info("Placing item in alter");
                    altarBlockEntity.setItem(0, stackInHand.split(1));
                    return InteractionResult.SUCCESS;
                } else if (!stackInAlter.isEmpty()) {
                    // Take item
                    if (stackInHand.isEmpty()) {
                        PaulsMod.LOGGER.info("Taking item from alter");
                        player.setItemInHand(InteractionHand.MAIN_HAND, altarBlockEntity.removeItem(0, 1));
                    } else {
                        if (!player.getInventory().add(altarBlockEntity.removeItem(0, 1))) {
                            player.drop(altarBlockEntity.removeItem(0, 1), false);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }

                // If no item interaction, cycle state
                int currentState = state.getValue(STATE);
                int newState = (currentState + 1) % 3;
                level.setBlock(pos, state.setValue(STATE, newState), 3);
                altarBlockEntity.setState(newState);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ALTER, AltarBlockEntity::tick);
    }

}
