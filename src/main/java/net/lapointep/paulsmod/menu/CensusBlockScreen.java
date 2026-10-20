package net.lapointep.paulsmod.menu;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lapointep.paulsmod.network.ModMessages;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class CensusBlockScreen extends Screen {
    private final BlockPos pos;
    private final String initialName;
    private EditBox nameEdit;

    public CensusBlockScreen(BlockPos pos, String initialName) {
        super(Component.translatable("gui.paulsmod.census_block.title"));
        this.pos = pos;
        this.initialName = initialName;
    }

    @Override
    protected void init() {
        this.nameEdit = new EditBox(this.font, this.width / 2 - 100, 60, 200, 20, Component.translatable("gui.paulsmod.census_block.area_name"));
        this.nameEdit.setMaxLength(32);
        this.nameEdit.setValue(this.initialName);
        this.addRenderableWidget(this.nameEdit);
        this.setInitialFocus(this.nameEdit);

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onDone();
        }).bounds(this.width / 2 - 100, 100, 200, 20).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
            this.onClose();
        }).bounds(this.width / 2 - 100, 130, 200, 20).build());
    }

    private void onDone() {
        ClientPlayNetworking.send(new ModMessages.CensusUpdatePayload(this.pos, this.nameEdit.getValue()));
        this.onClose();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
