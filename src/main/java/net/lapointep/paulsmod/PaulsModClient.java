package net.lapointep.paulsmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.block.entity.renderer.*;
import net.lapointep.paulsmod.menu.*;
import net.lapointep.paulsmod.entity.ModEntities;
import net.lapointep.paulsmod.entity.TeleporterArrowEntity;
import net.lapointep.paulsmod.network.ModMessages;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class PaulsModClient implements ClientModInitializer {
    public static KeyMapping toggleSpectatorKey;

    @Override
    public void onInitializeClient() {
        // Screen registration
        MenuScreens.register(ModMenuTypes.SORTER_MENU, SorterScreen::new);
        MenuScreens.register(ModMenuTypes.BREWER_MENU, BrewerScreen::new);
        MenuScreens.register(ModMenuTypes.STORESHELF_MENU, StoreShelfScreen::new);
        MenuScreens.register(ModMenuTypes.PROCESSOR_MENU, ProcessorScreen::new);
        MenuScreens.register(ModMenuTypes.ATM_MENU, ATMScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(ModMessages.OpenCensusScreenPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                context.client().setScreenAndShow(new CensusBlockScreen(payload.pos(), payload.areaName()));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModMessages.OpenOscScreenPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                context.client().setScreenAndShow(new OscBlockScreen(payload.pos(), payload.timeUp(), payload.timeDown()));
            });
        });

        // Block entity renderer registration
        BlockEntityRenderers.register(ModBlockEntities.PEDESTAL, PedestalBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.PLATE, PlateBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SPAWNER, SpawnerBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HOOK, HookBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.ALTER, AlterBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LAVA_CORE, ProcessorBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.MIXER, ProcessorBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.STICKYNOTE, StickyNoteBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.BREWER, BrewerBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.STORESHELF, StoreShelfBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.CAMO, CamoBlockEntityRenderer::new);

        // Other client-specific initialization
        toggleSpectatorKey = new KeyMapping(
                "key.paulsmod.toggle_spectator",
                GLFW.GLFW_KEY_Z,
                KeyMapping.Category.MISC
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleSpectatorKey.consumeClick()) {
                if (client.player != null && client.player.isSpectator()) {
                    ClientPlayNetworking.send(new ModMessages.ToggleSpectatorPayload());
                }
            }
        });

        // Entity renderer registration
        EntityRendererRegistry.register(ModEntities.TELEPORTER_ARROW, (EntityRendererProvider.Context context) -> new ArrowRenderer<TeleporterArrowEntity, ArrowRenderState>(context) {
            @Override
            public ArrowRenderState createRenderState() {
                return new ArrowRenderState();
            }

            @Override
            public void extractRenderState(TeleporterArrowEntity entity, ArrowRenderState state, float partialTick) {
                super.extractRenderState(entity, state, partialTick);
            }

            @Override
            protected Identifier getTextureLocation(ArrowRenderState state) {
                return Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "textures/entity/projectiles/teleporter_arrow.png");
            }
        });

        EntityRenderers.register(ModEntities.GUIDE_ENTITY, NoopRenderer::new);
        EntityRenderers.register(ModEntities.TORCH_ENTITY, NoopRenderer::new);
    }
}
