package net.lapointep.paulsmod.menu;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lapointep.paulsmod.network.ModMessages;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class OscBlockScreen extends Screen {
    private final BlockPos pos;
    private final int initialTimeUp;
    private final int initialTimeDown;
    private EditBox timeUpEdit;
    private EditBox timeDownEdit;

    public OscBlockScreen(BlockPos pos, int timeUp, int timeDown) {
        super(Component.translatable("gui.paulsmod.osc_block.title"));
        this.pos = pos;
        this.initialTimeUp = timeUp;
        this.initialTimeDown = timeDown;
    }


    @Override
    protected void init() {
        int centerX = this.width / 2;


        StringWidget timeUpLabel = new StringWidget(
                centerX - 100,
                48,
                200,
                10,
                Component.translatable("gui.paulsmod.osc_block.time_up_label"),
                this.font
        );
        this.addRenderableWidget(timeUpLabel);
        this.timeUpEdit = new EditBox(this.font, centerX - 100, 60, 200, 20, Component.translatable("gui.paulsmod.osc_block.time_up"));
        this.timeUpEdit.setMaxLength(5);
        this.timeUpEdit.setValue(String.valueOf(this.initialTimeUp));
        this.addRenderableWidget(this.timeUpEdit);

        StringWidget timeDownLabel = new StringWidget(
                centerX - 100,
                88,
                200,
                10,
                Component.translatable("gui.paulsmod.osc_block.time_down_label"),
                this.font
        );
        this.addRenderableWidget(timeDownLabel);

        this.timeDownEdit = new EditBox(this.font, centerX - 100, 100, 200, 20, Component.translatable("gui.paulsmod.osc_block.time_down"));
        this.timeDownEdit.setMaxLength(5);
        this.timeDownEdit.setValue(String.valueOf(this.initialTimeDown));
        this.addRenderableWidget(this.timeDownEdit);

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onDone();
        }).bounds(centerX - 100, 140, 200, 20).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
            this.onClose();
        }).bounds(centerX - 100, 170, 200, 20).build());
        
        this.setInitialFocus(this.timeUpEdit);
    }

    private void onDone() {
        try {
            int timeUp = Integer.parseInt(this.timeUpEdit.getValue());
            int timeDown = Integer.parseInt(this.timeDownEdit.getValue());
            ClientPlayNetworking.send(new ModMessages.OscUpdatePayload(this.pos, timeUp, timeDown));
            this.onClose();
        } catch (NumberFormatException e) {
            // Invalid input
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
