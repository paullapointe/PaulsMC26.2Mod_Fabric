package net.lapointep.paulsmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lapointep.paulsmod.block.custom.StickyNoteBlock;
import net.lapointep.paulsmod.block.entity.StickyNoteBlockEntity;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import static net.lapointep.paulsmod.util.PaulsCommon.splitByLength;
import static net.minecraft.core.Direction.*;

public class StickyNoteBlockEntityRenderer implements BlockEntityRenderer<StickyNoteBlockEntity, StickyNoteBlockEntityRenderState> {
    private final Font font;

    public StickyNoteBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.font();
    }

    @Override
    public StickyNoteBlockEntityRenderState createRenderState() {
        return new StickyNoteBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(StickyNoteBlockEntity blockEntity, StickyNoteBlockEntityRenderState state,
                                   float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.level = blockEntity.getLevel();
        state.noteText = blockEntity.getNoteText();
        state.facing = blockEntity.getBlockState().getValue(StickyNoteBlock.FACING);
    }

    @Override
    public void submit(StickyNoteBlockEntityRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {

        //state.noteText = "01234567890123456789012345679012345678901234567890";
        if (state.noteText == null || state.noteText.isEmpty()) {
            return;
        }
        int color = 0xFFFFFFFF;
        float scale = 0.005f;

        poseStack.pushPose();

        Direction facing = state.facing;
        if (facing == Direction.NORTH) {
            poseStack.translate(0.5, 0.7, 0.89); // Offset to prevent z-fighting
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            color = 0xFFFF0000;
        }
        if (facing == Direction.SOUTH) {
            poseStack.translate(0.5, 0.7, 0.1); // Offset to prevent z-fighting
        }
        else if (facing == Direction.EAST) {
            poseStack.translate(0.1, 0.7, 0.5); // Offset to prevent z-fighting
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            color = 0xFF0000FF;
        }
        else if (facing == Direction.WEST) {
            poseStack.translate(0.89, 0.7, 0.5); // Offset to prevent z-fighting
            poseStack.mulPose(Axis.YP.rotationDegrees(270));
            color = 0xFF00FF00;
        }

        String[] lines = splitByLength(state.noteText, 14);

        float lineSpacing = 10.0F * scale;
        float startY = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            float yOffset = startY - (i * lineSpacing);
            PaulsCommon.placeTextOnBlock(poseStack, submitNodeCollector, font, color, scale, line, state.lightCoords, 0f, yOffset, 0f);
        }

        poseStack.popPose();
    }
}
