package net.lapointep.paulsmod.mixin;

import net.lapointep.paulsmod.access.ProtectionDataHolder;
import net.lapointep.paulsmod.access.SpectatorDataHolder;
import net.lapointep.paulsmod.item.custom.OneRingItem;
import net.lapointep.paulsmod.util.Destination;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin implements SpectatorDataHolder, ProtectionDataHolder {
    @Unique
    private int paulsmod$spectatorTicks = 0;
    @Unique
    private GameType paulsmod$originalGameMode = GameType.SURVIVAL;
    @Unique
    private Destination paulsmod$protectionDestination = null;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void paulsmod$writeSpectatorNbt(ValueOutput output, CallbackInfo ci) {
        if (paulsmod$spectatorTicks > 0) {
            output.putInt("PaulsSpectatorTicks", paulsmod$spectatorTicks);
            output.putString("PaulsOriginalGameMode", paulsmod$originalGameMode.getName());
        }
        if (paulsmod$protectionDestination != null) {
            output.putInt("PaulsProtX", paulsmod$protectionDestination.getPos().getX());
            output.putInt("PaulsProtY", paulsmod$protectionDestination.getPos().getY());
            output.putInt("PaulsProtZ", paulsmod$protectionDestination.getPos().getZ());
            output.putString("PaulsProtDim", paulsmod$protectionDestination.getDimension().identifier().toString());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void paulsmod$readSpectatorNbt(ValueInput input, CallbackInfo ci) {
        paulsmod$spectatorTicks = input.getIntOr("PaulsSpectatorTicks", 0);
        paulsmod$originalGameMode = GameType.byName(input.getStringOr("PaulsOriginalGameMode", "survival"), GameType.SURVIVAL);
        if (input.contains("PaulsProtX")) {
            BlockPos pos = new BlockPos(input.getIntOr("PaulsProtX", 0), input.getIntOr("PaulsProtY", 0), input.getIntOr("PaulsProtZ", 0));
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, Identifier.parse(input.getStringOr("PaulsProtDim", "minecraft:overworld")));
            paulsmod$protectionDestination = new Destination(pos, dim);
        }
    }

    @Unique
    public Destination paulsmod$getProtectionDestination() {
        return paulsmod$protectionDestination;
    }

    @Unique
    public void paulsmod$setProtectionDestination(Destination destination) {
        paulsmod$protectionDestination = destination;
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
