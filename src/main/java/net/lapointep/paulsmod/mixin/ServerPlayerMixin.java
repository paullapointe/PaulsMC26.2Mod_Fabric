package net.lapointep.paulsmod.mixin;

import net.lapointep.paulsmod.access.SpectatorDataHolder;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void paulsmod$tickSpectator(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        SpectatorDataHolder data = (SpectatorDataHolder) player;
        
        int ticks = data.paulsmod$getSpectatorTicks();
        if (ticks > 0) {
            ticks--;
            data.paulsmod$setSpectatorTicks(ticks);
            if (ticks == 0) {
                player.setGameMode(data.paulsmod$getOriginalGameMode());
            }
        }
    }
}
