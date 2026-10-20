package net.lapointep.paulsmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lapointep.paulsmod.block.custom.BrewerBlock;
import net.lapointep.paulsmod.block.entity.BrewerBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BrewerBlockEntityRenderer implements BlockEntityRenderer<BrewerBlockEntity, BrewerBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;
    public BrewerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public BrewerBlockEntityRenderState createRenderState() {
        return new BrewerBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(BrewerBlockEntity blockEntity, BrewerBlockEntityRenderState state,
                                   float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.level = blockEntity.getLevel();
        state.rotation = (blockEntity.getLevel().getGameTime() + partialTicks * 0.5f) % 360f;
        state.facing = blockEntity.getBlockState().getValue(BrewerBlock.FACING);

        for (int i = 0; i < 6; i++) {
            itemModelResolver.updateForTopItem(state.itemStackRenderState[i],
                    blockEntity.getItem(i), ItemDisplayContext.FIXED,
                    blockEntity.getLevel(), null, 0);
        }
    }

    @Override
    public void submit(BrewerBlockEntityRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float scale = 0.3f;
        displayItem(0, state, poseStack, submitNodeCollector, camera, 0.25f, 0.75f, 0.5f, scale);
        displayItem(1, state, poseStack, submitNodeCollector, camera, 0.5f,  0.75f, 0.5f, scale);
        displayItem(2, state, poseStack, submitNodeCollector, camera, 0.75f, 0.75f, 0.5f, scale);
        displayItem(3, state, poseStack, submitNodeCollector, camera, 0.25f, 0.25f, 0.5f, scale);
        displayItem(4, state, poseStack, submitNodeCollector, camera, 0.5f,  0.25f, 0.5f, scale);
        displayItem(5, state, poseStack, submitNodeCollector, camera, 0.75f, 0.25f, 0.5f, scale);

    }

    public void displayItem(int i, BrewerBlockEntityRenderState state, PoseStack poseStack,
                            SubmitNodeCollector submitNodeCollector, CameraRenderState camera,
                            float xOff, float yOff, float zOff, float scale) {
        poseStack.pushPose();
     //   poseStack.mulPose(Axis.YN.rotationDegrees(state.rotation));
        poseStack.rotateAround(Axis.YN.rotationDegrees(state.rotation), xOff, yOff, zOff);
        poseStack.translate(xOff, yOff, zOff);
        poseStack.scale(scale, scale, scale);
        state.itemStackRenderState[i].submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
