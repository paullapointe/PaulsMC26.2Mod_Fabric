package net.lapointep.paulsmod.mixin;

import net.lapointep.paulsmod.access.SpectatorDataHolder;
import net.lapointep.paulsmod.item.custom.OneRingItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin implements SpectatorDataHolder {
    @Unique
    private int paulsmod$spectatorTicks = 0;
    @Unique
    private GameType paulsmod$originalGameMode = GameType.SURVIVAL;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void paulsmod$writeSpectatorNbt(ValueOutput output, CallbackInfo ci) {
        if (paulsmod$spectatorTicks > 0) {
            output.putInt("PaulsSpectatorTicks", paulsmod$spectatorTicks);
            output.putString("PaulsOriginalGameMode", paulsmod$originalGameMode.getName());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void paulsmod$readSpectatorNbt(ValueInput input, CallbackInfo ci) {
        paulsmod$spectatorTicks = input.getIntOr("PaulsSpectatorTicks", 0);
        paulsmod$originalGameMode = GameType.byName(input.getStringOr("PaulsOriginalGameMode", "survival"), GameType.SURVIVAL);
    }

    @Unique
    public int paulsmod$getSpectatorTicks() {
        return paulsmod$spectatorTicks;
    }

    @Unique
    public void paulsmod$setSpectatorTicks(int ticks) {
        paulsmod$spectatorTicks = ticks;
    }

    @Unique
    public GameType paulsmod$getOriginalGameMode() {
        return paulsmod$originalGameMode;
    }

    @Unique
    public void paulsmod$setOriginalGameMode(GameType gameMode) {
        paulsmod$originalGameMode = gameMode;
    }
}
