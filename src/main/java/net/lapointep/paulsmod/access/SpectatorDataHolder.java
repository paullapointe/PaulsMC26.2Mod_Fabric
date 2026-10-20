package net.lapointep.paulsmod.access;

import net.minecraft.world.level.GameType;

public interface SpectatorDataHolder {
    int paulsmod$getSpectatorTicks();
    void paulsmod$setSpectatorTicks(int ticks);
    GameType paulsmod$getOriginalGameMode();
    void paulsmod$setOriginalGameMode(GameType gameMode);
}
