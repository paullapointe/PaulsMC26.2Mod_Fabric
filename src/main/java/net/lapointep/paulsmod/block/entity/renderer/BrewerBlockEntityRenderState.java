package net.lapointep.paulsmod.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class BrewerBlockEntityRenderState extends BlockEntityRenderState {
    public Level level;
    public float rotation;
    public Direction facing = Direction.NORTH;
    final ItemStackRenderState[] itemStackRenderState = new ItemStackRenderState[6];
    BrewerBlockEntityRenderState() {
        for (int i = 0; i < 6; i++) {
            itemStackRenderState[i] = new ItemStackRenderState();
        }
    }
}
