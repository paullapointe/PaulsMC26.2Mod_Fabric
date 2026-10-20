package net.lapointep.paulsmod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.item.custom.OneRingItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ModMessages {
    public static final Identifier TOGGLE_SPECTATOR_ID = Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "toggle_spectator");

    public record ToggleSpectatorPayload() implements CustomPacketPayload {
        public static final Type<ToggleSpectatorPayload> TYPE = new Type<>(TOGGLE_SPECTATOR_ID);
        public static final StreamCodec<FriendlyByteBuf, ToggleSpectatorPayload> CODEC = StreamCodec.unit(new ToggleSpectatorPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static final Identifier CENSUS_UPDATE_ID = Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "census_update");
    public static final Identifier OPEN_CENSUS_SCREEN_ID = Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "open_census_screen");
    public static final Identifier STICKY_NOTE_UPDATE_ID = Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "sticky_note_update");
    public static final Identifier OSC_UPDATE_ID = Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "osc_update");
    public static final Identifier OPEN_OSC_SCREEN_ID = Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "open_osc_screen");

    public record CensusUpdatePayload(BlockPos pos, String areaName) implements CustomPacketPayload {
        public static final Type<CensusUpdatePayload> TYPE = new Type<>(CENSUS_UPDATE_ID);
        public static final StreamCodec<FriendlyByteBuf, CensusUpdatePayload> CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, CensusUpdatePayload::pos,
                StreamCodec.of((buf, val) -> buf.writeUtf(val), buf -> buf.readUtf()), CensusUpdatePayload::areaName,
                CensusUpdatePayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record OpenCensusScreenPayload(BlockPos pos, String areaName) implements CustomPacketPayload {
        public static final Type<OpenCensusScreenPayload> TYPE = new Type<>(OPEN_CENSUS_SCREEN_ID);
        public static final StreamCodec<FriendlyByteBuf, OpenCensusScreenPayload> CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, OpenCensusScreenPayload::pos,
                StreamCodec.of((buf, val) -> buf.writeUtf(val), buf -> buf.readUtf()), OpenCensusScreenPayload::areaName,
                OpenCensusScreenPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record StickyNoteUpdatePayload(String text) implements CustomPacketPayload {
        public static final Type<StickyNoteUpdatePayload> TYPE = new Type<>(STICKY_NOTE_UPDATE_ID);
        public static final StreamCodec<FriendlyByteBuf, StickyNoteUpdatePayload> CODEC = StreamCodec.composite(
                StreamCodec.of((buf, val) -> buf.writeUtf(val), buf -> buf.readUtf()), StickyNoteUpdatePayload::text,
                StickyNoteUpdatePayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record OscUpdatePayload(BlockPos pos, int timeUp, int timeDown) implements CustomPacketPayload {
        public static final Type<OscUpdatePayload> TYPE = new Type<>(OSC_UPDATE_ID);
        public static final StreamCodec<FriendlyByteBuf, OscUpdatePayload> CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, OscUpdatePayload::pos,
                ByteBufCodecs.VAR_INT, OscUpdatePayload::timeUp,
                ByteBufCodecs.VAR_INT, OscUpdatePayload::timeDown,
                OscUpdatePayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record OpenOscScreenPayload(BlockPos pos, int timeUp, int timeDown) implements CustomPacketPayload {
        public static final Type<OpenOscScreenPayload> TYPE = new Type<>(OPEN_OSC_SCREEN_ID);
        public static final StreamCodec<FriendlyByteBuf, OpenOscScreenPayload> CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, OpenOscScreenPayload::pos,
                ByteBufCodecs.VAR_INT, OpenOscScreenPayload::timeUp,
                ByteBufCodecs.VAR_INT, OpenOscScreenPayload::timeDown,
                OpenOscScreenPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(ToggleSpectatorPayload.TYPE, ToggleSpectatorPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(CensusUpdatePayload.TYPE, CensusUpdatePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(OpenCensusScreenPayload.TYPE, OpenCensusScreenPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(StickyNoteUpdatePayload.TYPE, StickyNoteUpdatePayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(OscUpdatePayload.TYPE, OscUpdatePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(OpenOscScreenPayload.TYPE, OpenOscScreenPayload.CODEC);
    }

    public static void registerServerHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(ToggleSpectatorPayload.TYPE, (payload, context) -> {
            MinecraftServer server = context.server();
            ServerPlayer player = context.player();
            server.execute(() -> {
                if (player.gameMode.getGameModeForPlayer() == GameType.SPECTATOR) {
                    OneRingItem.toggleGameMode(player);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(CensusUpdatePayload.TYPE, (payload, context) -> {
            MinecraftServer server = context.server();
            ServerPlayer player = context.player();
            server.execute(() -> {
                BlockPos pos = payload.pos();
                if (player.level().isLoaded(pos) && player.level().getBlockEntity(pos) instanceof net.lapointep.paulsmod.block.entity.CensusBlockEntity census) {
                    census.setAreaName(payload.areaName());
                    // Notify client of the change if needed, but here we just update server side.
                    // The ticker in CensusBlockEntity uses the areaName.
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(StickyNoteUpdatePayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                net.minecraft.world.item.ItemStack stack = player.getItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND);
                if (!stack.is(net.lapointep.paulsmod.item.ModItems.STICKYNOTE)) {
                    stack = player.getItemInHand(net.minecraft.world.InteractionHand.OFF_HAND);
                }

                if (stack.is(net.lapointep.paulsmod.item.ModItems.STICKYNOTE)) {
                    ((net.lapointep.paulsmod.item.custom.StickyNoteBlockItem) stack.getItem()).setNoteText(stack, payload.text());
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(OscUpdatePayload.TYPE, (payload, context) -> {
            MinecraftServer server = context.server();
            ServerPlayer player = context.player();
            server.execute(() -> {
                BlockPos pos = payload.pos();
                if (player.level().isLoaded(pos) && player.level().getBlockEntity(pos) instanceof net.lapointep.paulsmod.block.entity.OscBlockEntity osc) {
                    osc.setTimeUp(payload.timeUp());
                    osc.setTimeDown(payload.timeDown());
                    osc.setChanged();
                }
            });
        });
    }
}
