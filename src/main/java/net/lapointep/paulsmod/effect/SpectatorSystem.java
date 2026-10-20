package net.lapointep.paulsmod.effect;

import net.lapointep.paulsmod.access.SpectatorDataHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class SpectatorSystem {
    public static void triggerSpectatorEffect(ServerPlayer player, int durationTicks) {
        if (player instanceof SpectatorDataHolder data) {
            data.paulsmod$setOriginalGameMode(player.gameMode.getGameModeForPlayer());
            data.paulsmod$setSpectatorTicks(durationTicks);
            player.setGameMode(GameType.SPECTATOR);
        }
    }
}
