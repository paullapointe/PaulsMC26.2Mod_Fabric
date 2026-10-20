package net.lapointep.paulsmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lapointep.paulsmod.block.entity.CamoBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.item.ItemModelResolver;

public class CamoBlockEntityRenderer implements BlockEntityRenderer<CamoBlockEntity, CamoBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public CamoBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CamoBlockEntityRenderState createRenderState() {
        return new CamoBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(CamoBlockEntity blockEntity, CamoBlockEntityRenderState state,
                                   float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.camoState = blockEntity.getCamoState();
        if (state.camoState != null && !state.camoState.isAir()) {
            itemModelResolver.updateForTopItem(state.itemStackRenderState, new ItemStack(state.camoState.getBlock().asItem()), ItemDisplayContext.NONE, blockEntity.getLevel(), null, 0);
        }
    }

    @Override
    public void submit(CamoBlockEntityRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.camoState != null && !state.camoState.isAir()) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.5f, 0.5f);
            state.itemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }
}
