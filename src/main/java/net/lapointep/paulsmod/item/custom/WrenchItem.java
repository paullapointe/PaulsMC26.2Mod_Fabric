package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.entity.BrewerBlockEntity;
import net.lapointep.paulsmod.block.entity.StatueBlockEntity;
import net.lapointep.paulsmod.block.entity.StainGlassBlockEntity;
import net.lapointep.paulsmod.block.entity.StoreShelfBlockEntity;
import net.lapointep.paulsmod.block.entity.DisplayShelfBlockEntity;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class WrenchItem extends Item {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand hand) {
        if (interactionTarget instanceof Cat cat) {
            Level level = player.level();
            if (!level.isClientSide()) {
                Optional<Registry<CatVariant>> registryOptional = level.registryAccess().lookup(Registries.CAT_VARIANT);
                if (registryOptional.isPresent()) {
                    Registry<CatVariant> registry = registryOptional.get();
                    Holder<CatVariant> currentVariant = cat.getVariant();

                    List<Holder.Reference<CatVariant>> variants = registry.listElements().toList();
                    if (!variants.isEmpty()) {
                        int currentIndex = -1;
                        for (int i = 0; i < variants.size(); i++) {
                            if (variants.get(i).equals(currentVariant)) {
                                currentIndex = i;
                                break;
                            }
                        }

                        int nextIndex = (currentIndex + 1) % variants.size();
                        cat.setComponent(DataComponents.CAT_VARIANT, variants.get(nextIndex));
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, hand);
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
