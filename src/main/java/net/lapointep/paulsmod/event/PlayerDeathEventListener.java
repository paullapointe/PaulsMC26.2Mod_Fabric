package net.lapointep.paulsmod.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class PlayerDeathEventListener {
    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayer player) {
                BlockPos pos = player.blockPosition();
                PaulsMod.LOGGER.info("Player {} died at block position: [x={}, y={}, z={}]",
                        player.getName().getString(),
                        pos.getX(), pos.getY(), pos.getZ());
            }
        });
    }
}
