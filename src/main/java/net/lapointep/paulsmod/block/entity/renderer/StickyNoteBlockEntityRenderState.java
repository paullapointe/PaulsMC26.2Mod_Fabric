package net.lapointep.paulsmod.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class StickyNoteBlockEntityRenderState extends BlockEntityRenderState {
    public Level level;
    public float rotation;
    public String noteText = "";
    public Direction facing = Direction.NORTH;

    final ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
}
