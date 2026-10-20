package net.lapointep.paulsmod.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public class CamoBlockEntityRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
    public BlockState camoState;
}
