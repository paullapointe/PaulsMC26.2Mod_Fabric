package net.lapointep.paulsmod.menu;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.network.ModMessages;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class StickyNoteScreen extends Screen {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "textures/gui/sticky_note_gui.png");
    private final String initialText;
    private EditBox textEdit;
    private final int imageWidth = 200;
    private final int imageHeight = 166;

    public StickyNoteScreen(String initialText) {
        super(Component.translatable("gui.paulsmod.sticky_note.title"));
        this.initialText = initialText;
    }

    @Override
    protected void init() {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.textEdit = new EditBox(this.font, x + 20, y + 40, 180, 20, Component.translatable("gui.paulsmod.sticky_note.text"));
        this.textEdit.setMaxLength(120);
        this.textEdit.setValue(this.initialText);
        this.addRenderableWidget(this.textEdit);
        this.setInitialFocus(this.textEdit);

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onDone();
        }).bounds(x + 20, y + 100, 60, 20).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
            this.onClose();
        }).bounds(x + 96, y + 100, 60, 20).build());
    }

    private void onDone() {
        ClientPlayNetworking.send(new ModMessages.StickyNoteUpdatePayload(this.textEdit.getValue()));
        this.onClose();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
