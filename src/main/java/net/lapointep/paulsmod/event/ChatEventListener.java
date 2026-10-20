package net.lapointep.paulsmod.event;

import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class ChatEventListener {

    // Call this method from your main ModInitializer's onInitialize()
    public static void register() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {

            // message.signedContent() returns the raw string the player typed
            String rawMessage = message.signedContent();

            // Target a specific block position or search loaded block entities
            PaulsMod.LOGGER.info("Received chat message from {}: {}", sender.getName().getString(), rawMessage);
            PaulsCommon.receiveChatMessage(sender, rawMessage);

        });
    }
}
