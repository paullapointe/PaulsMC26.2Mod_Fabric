package net.lapointep.paulsmod.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class StoreShelfBlockEntityRenderState extends BlockEntityRenderState {
    public Level level;
    public int cost = 0;
    final ItemStackRenderState[] itemStackRenderState = new ItemStackRenderState[6];
    StoreShelfBlockEntityRenderState() {
        for (int i = 0; i < 6; i++) {
            itemStackRenderState[i] = new ItemStackRenderState();
        }
    }
}
