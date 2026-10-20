package net.lapointep.paulsmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lapointep.paulsmod.block.custom.StoreShelfBlock;
import net.lapointep.paulsmod.block.entity.StoreShelfBlockEntity;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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

public class StoreShelfBlockEntityRenderer implements BlockEntityRenderer<StoreShelfBlockEntity, StoreShelfBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;
    private final Font font;

    public StoreShelfBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.font();
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public StoreShelfBlockEntityRenderState createRenderState() {

        return new StoreShelfBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(StoreShelfBlockEntity blockEntity, StoreShelfBlockEntityRenderState state,
                                   float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.level = blockEntity.getLevel();
        state.cost = blockEntity.getBlockState().getValue(StoreShelfBlock.COST);
        for (int i = 0; i < 6; i++) {
            itemModelResolver.updateForTopItem(state.itemStackRenderState[i],
                    blockEntity.getItem(i), ItemDisplayContext.FIXED,
                    blockEntity.getLevel(), null, 0);
        }


    }


    @Override
    public void submit(StoreShelfBlockEntityRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float scale = 0.3f;
        displayItem(0, state, poseStack, submitNodeCollector, camera, 0.25f, 0.75f, 0.5f, scale);
        displayItem(1, state, poseStack, submitNodeCollector, camera, 0.5f,  0.75f, 0.5f, scale);
        displayItem(2, state, poseStack, submitNodeCollector, camera, 0.75f, 0.75f, 0.5f, scale);
        displayItem(3, state, poseStack, submitNodeCollector, camera, 0.25f, 0.25f, 0.5f, scale);
        displayItem(4, state, poseStack, submitNodeCollector, camera, 0.5f,  0.25f, 0.5f, scale);
        displayItem(5, state, poseStack, submitNodeCollector, camera, 0.75f, 0.25f, 0.5f, scale);

        poseStack.pushPose();
        int color = 0xFF00FF00;
        scale = 0.05f;
        String line = "" + state.cost;

        // Pass submitNodeCollector directly instead of bufferSource
        PaulsCommon.placeTextOnBlock(poseStack, submitNodeCollector, font, color, scale, line, state.lightCoords, 0.5f, 1.5f, 0.5f);
        poseStack.popPose();
    }


    public void displayItem(int i, StoreShelfBlockEntityRenderState state, PoseStack poseStack,
                            SubmitNodeCollector submitNodeCollector, CameraRenderState camera,
                            float xOff, float yOff, float zOff, float scale) {
        poseStack.pushPose();
        poseStack.translate(xOff, yOff, zOff);
        poseStack.scale(scale, scale, scale);
        state.itemStackRenderState[i].submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
