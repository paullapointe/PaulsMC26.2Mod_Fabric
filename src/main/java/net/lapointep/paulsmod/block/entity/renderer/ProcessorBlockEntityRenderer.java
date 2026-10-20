package net.lapointep.paulsmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lapointep.paulsmod.block.entity.ProcessorBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ProcessorBlockEntityRenderer implements BlockEntityRenderer<ProcessorBlockEntity, ProcessorBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public ProcessorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public ProcessorBlockEntityRenderState createRenderState() {
        return new ProcessorBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(ProcessorBlockEntity blockEntity, ProcessorBlockEntityRenderState state,
                                   float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.level = blockEntity.getLevel();

        ItemStack itemStack = blockEntity.displayItem();
        if ((itemStack == null) || (itemStack.isEmpty())) { return; }
        itemModelResolver.updateForTopItem(state.itemStackRenderState,
                itemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(ProcessorBlockEntityRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.translate(0.5f, 0.35f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);

        state.itemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }
}
