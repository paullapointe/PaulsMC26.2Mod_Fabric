package net.lapointep.paulsmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lapointep.paulsmod.block.custom.HookBlock;
import net.lapointep.paulsmod.block.custom.StickyNoteBlock;
import net.lapointep.paulsmod.block.entity.HookBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class HookBlockEntityRenderer implements BlockEntityRenderer<HookBlockEntity, HookBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public HookBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public HookBlockEntityRenderState createRenderState() {
        return new HookBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(HookBlockEntity blockEntity, HookBlockEntityRenderState state,
                                   float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.level = blockEntity.getLevel();
        state.facing = blockEntity.getBlockState().getValue(HookBlock.FACING);
        itemModelResolver.updateForTopItem(state.itemStackRenderState,
                blockEntity.getTheItem(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(HookBlockEntityRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        Direction facing = state.facing;
        float yOff = 0.4f;

        if (facing == Direction.NORTH) {
            poseStack.translate(0.5, yOff, 0.89); // Offset to prevent z-fighting
        }
        if (facing == Direction.SOUTH) {
            poseStack.translate(0.5, yOff, 0.1); // Offset to prevent z-fighting
        }
        else if (facing == Direction.EAST) {
            poseStack.translate(0.1, yOff, 0.5); // Offset to prevent z-fighting
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
        }
        else if (facing == Direction.WEST) {
            poseStack.translate(0.89, yOff, 0.5); // Offset to prevent z-fighting
            poseStack.mulPose(Axis.YP.rotationDegrees(270));
        }
        poseStack.scale(0.5f, 0.5f, 0.5f);
        state.itemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }
}
