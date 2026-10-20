package net.lapointep.paulsmod.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lapointep.paulsmod.block.entity.LockingDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

public class LockingDoorBlock extends DoorBlock implements EntityBlock {
    public static final MapCodec<LockingDoorBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(BlockSetType.CODEC.fieldOf("set_type").forGetter(DoorBlock::type), propertiesCodec())
                    .apply(instance, LockingDoorBlock::new));

    @Override
    public MapCodec<? extends LockingDoorBlock> codec() {
        return CODEC;
    }

    public LockingDoorBlock(BlockSetType type, Properties properties) {
        super(type, properties);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable net.minecraft.world.level.redstone.Orientation orientation, boolean notify) {
        // Do nothing to ignore redstone signals
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LockingDoorBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide() && placer instanceof Player player) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof LockingDoorBlockEntity doorBe) {
                doorBe.setOwner(player.getUUID());
            }
            // Also set owner for the other half
            BlockPos otherHalfPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockEntity otherBe = level.getBlockEntity(otherHalfPos);
            if (otherBe instanceof LockingDoorBlockEntity otherDoorBe) {
                otherDoorBe.setOwner(player.getUUID());
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof LockingDoorBlockEntity doorBe) {
                if (doorBe.getOwner() != null && !doorBe.getOwner().equals(player.getUUID())) {
                    return InteractionResult.FAIL;
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
