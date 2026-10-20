package net.lapointep.paulsmod.util;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerProvider {
    private static MinecraftServer SERVER_INSTANCE;

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            SERVER_INSTANCE = server;
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            SERVER_INSTANCE = null; // Prevent memory leaks
        });
    }

    public static MinecraftServer getServer() {
        return SERVER_INSTANCE;
    }
}