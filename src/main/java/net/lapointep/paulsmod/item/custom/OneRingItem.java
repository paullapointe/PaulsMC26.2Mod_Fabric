package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.access.SpectatorDataHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

public class OneRingItem extends Item {
    public OneRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            toggleGameMode(serverPlayer);
        }

        return InteractionResult.SUCCESS;
    }

    public static void toggleGameMode(ServerPlayer serverPlayer) {
        if (serverPlayer instanceof SpectatorDataHolder dataHolder) {
            GameType currentMode = serverPlayer.gameMode.getGameModeForPlayer();

            if (currentMode != GameType.SPECTATOR) {
                dataHolder.paulsmod$setOriginalGameMode(currentMode);
                dataHolder.paulsmod$setSpectatorTicks(-1); // Use -1 to indicate indefinite spectator mode
                serverPlayer.setGameMode(GameType.SPECTATOR);
            } else {
                serverPlayer.setGameMode(dataHolder.paulsmod$getOriginalGameMode());
                dataHolder.paulsmod$setSpectatorTicks(0);
            }
        }
    }
}
