package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.access.ProtectionDataHolder;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.block.entity.ExtractorBlockEntity;
import net.lapointep.paulsmod.effect.ModEffects;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.util.Destination;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
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

public class ExtractorBlock extends BaseEntityBlock {

    public static final int MAX_STATE = 1;
    public static final MapCodec<ExtractorBlock> CODEC = simpleCodec(ExtractorBlock::new);
    static final int BOTTLE_AMOUNT = 9;
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public ExtractorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (level instanceof ServerLevel serverLevel) {

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ExtractorBlockEntity extractorBlockEntity) {
                ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack stackInAlter = extractorBlockEntity.getItem(0);

                if (stackInAlter.isEmpty() && !stackInHand.isEmpty()) {
                    // Place item
                    if (stackInHand.is(Items.GLASS_BOTTLE)) {
                        stackInHand.split(1);
                        player.giveExperiencePoints(-BOTTLE_AMOUNT);
                        ItemStack experienceBottle = new ItemStack(Items.EXPERIENCE_BOTTLE);
                        extractorBlockEntity.setItem(0, experienceBottle);
                        serverLevel.sendParticles(ParticleTypes.NOXIOUS_GAS, player.getX(), player.getY(), player.getZ(), 32, 0.5, 0.5, 0.5, 0.1);
                    }
                    else {
                        extractorBlockEntity.setItem(0, stackInHand.split(1));
                    }
                    extractorBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                } else if (!stackInAlter.isEmpty()) {
                    // Take item
                    if (stackInHand.isEmpty()) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, extractorBlockEntity.removeItem(0, 1));

                    } else {
                        if (!player.getInventory().add(extractorBlockEntity.removeItem(0, 1))) {
                            player.drop(extractorBlockEntity.removeItem(0, 1), false);
                        }
                    }
                    extractorBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
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
        return new ExtractorBlockEntity(pos, state);
    }

}
