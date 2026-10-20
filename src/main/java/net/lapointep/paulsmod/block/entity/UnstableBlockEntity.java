package net.lapointep.paulsmod.block.entity;

import net.lapointep.paulsmod.block.BlockWithCyclableState;
import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.block.custom.UnstableBlock;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import static net.lapointep.paulsmod.block.custom.UnstableBlock.MAX_STATE;

public class UnstableBlockEntity extends BlockEntity implements BlockWithCyclableState {

    final int MAX_SPREAD = 8;
    final int MAX_COUNTER = 20;
    final BETicker ticker = new BETicker(MAX_COUNTER);
    RandomSource random;
    int spread = 0;
    BlockEntityType blockType;

    public UnstableBlockEntity(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        blockType = type;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, UnstableBlockEntity blockEntity) {
        // Tick logic here
        if (level.isClientSide()) {
            return;
        }
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (ticker.notReadyToRun()) { return; }
        random = level.getRandom();

       // PaulsMod.LOGGER.info("Unstable Ticking {} {}", pos, state.getValue(UnstableBlock.STATE));
        int currentState = state.getValue(UnstableBlock.STATE);

        if (currentState == MAX_STATE-1) {
        //    PaulsMod.LOGGER.info("Reached state MAX_STATE");
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            setChanged(level, pos, state);
            addDrama(pos, level, (ServerLevel) level, random);
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.WHITE_SMOKE, pos.getX(), pos.getY()+1, pos.getZ(), currentState*4, 0.5, 0.5, 0.5, 0.1);
        }

        cycleState(level, pos, state);
        setChanged(level, pos, state);

        if (spread >= MAX_SPREAD) { return; }

        // Propagate to neighboring blocks
        for (BlockPos neighborPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            BlockState neighborState = level.getBlockState(neighborPos);
            if (blockType == ModBlockEntities.UNSTABLENETHERRACK) {
                if (neighborState.is(Blocks.NETHERRACK)) {
                    level.setBlock(neighborPos, ModBlocks.UNSTABLENETHERRACK.defaultBlockState(), 3);
                    BlockEntity neighborBlockEntity = level.getBlockEntity(neighborPos);
                    if (neighborBlockEntity instanceof UnstableBlockEntity unstableBlockEntity) {
                        unstableBlockEntity.setSpread(spread+1);
                    }
                }
            }
            else if (blockType == ModBlockEntities.UNSTABLELAVA) {
                FluidState fluidState = state.getFluidState();
                boolean isFlowingLava = fluidState.is(FluidTags.WATER) && !fluidState.isSource();
                if (neighborState.is(Blocks.LAVA)||isFlowingLava) {
                    if (spread == MAX_SPREAD) {

                        BlockState blockState = Blocks.GRAVEL.defaultBlockState();
                        if (random.nextFloat() > 0.5f) {
                            blockState = Blocks.BASALT.defaultBlockState();
                        }
                        level.setBlock(neighborPos, blockState.getBlock().defaultBlockState(),3);
                        BlockEntity neighborBlockEntity = level.getBlockEntity(neighborPos);
                        if (neighborBlockEntity instanceof UnstableBlockEntity unstableBlockEntity) {
                            unstableBlockEntity.setSpread(spread+1);
                        }
                    }
                    else {
                        level.setBlock(neighborPos, ModBlocks.UNSTABLELAVA.defaultBlockState(), 3);
                        BlockEntity neighborBlockEntity = level.getBlockEntity(neighborPos);
                        if (neighborBlockEntity instanceof UnstableBlockEntity unstableBlockEntity) {
                            unstableBlockEntity.setSpread(spread+1);
                        }
                    }
                }
            }
            else if (blockType == ModBlockEntities.UNSTABLEWATER) {
                FluidState fluidState = state.getFluidState();
                boolean isFlowingWater = fluidState.is(FluidTags.WATER) && !fluidState.isSource();
                if (neighborState.is(Blocks.WATER)||isFlowingWater) {

                    if (spread == MAX_SPREAD) {
                        level.setBlock(neighborPos, Blocks.ICE.defaultBlockState(), 3);
                        BlockEntity neighborBlockEntity = level.getBlockEntity(neighborPos);
                        if (neighborBlockEntity instanceof UnstableBlockEntity unstableBlockEntity) {
                            unstableBlockEntity.setSpread(spread+1);
                        }
                    }
                    else {
                        level.setBlock(neighborPos, ModBlocks.UNSTABLEWATER.defaultBlockState(), 3);
                        BlockEntity neighborBlockEntity = level.getBlockEntity(neighborPos);
                        if (neighborBlockEntity instanceof UnstableBlockEntity unstableBlockEntity) {
                            unstableBlockEntity.setSpread(spread+1);
                        }
                    }
                }
            }
            else if (blockType == ModBlockEntities.UNSTABLESTONE) {
                if (neighborState.is(Blocks.STONE)) {
                    level.setBlock(neighborPos, ModBlocks.UNSTABLESTONE.defaultBlockState(), 3);
                    BlockEntity neighborBlockEntity = level.getBlockEntity(neighborPos);
                    if (neighborBlockEntity instanceof UnstableBlockEntity unstableBlockEntity) {
                        unstableBlockEntity.setSpread(spread+1);
                    }
                }
            }
            else if (blockType == ModBlockEntities.UNSTABLEDIRT) {
                if (neighborState.is(Blocks.DIRT)) {
                    level.setBlock(neighborPos, ModBlocks.UNSTABLEDIRT.defaultBlockState(), 3);
                    BlockEntity neighborBlockEntity = level.getBlockEntity(neighborPos);
                    if (neighborBlockEntity instanceof UnstableBlockEntity unstableBlockEntity) {
                        unstableBlockEntity.setSpread(spread+1);
                    }
                }
            }
        }
    }

    public void addDrama(BlockPos pos, Level level, ServerLevel serverLevel, RandomSource random) {
        if (random.nextFloat() < 0.05F) {
            PaulsCommon.spawnLightningAt(level, pos, false);
        }

        serverLevel.explode(
                null,
                pos.getX(), pos.getY(), pos.getZ(),
                2,
                Level.ExplosionInteraction.BLOCK
        );
    }

    public void cycleState(Level level, BlockPos pos, BlockState state) {
        int currentState = state.getValue(UnstableBlock.STATE);
        int newState = (currentState + 1) % (MAX_STATE);
        level.setBlock(pos, state.setValue(UnstableBlock.STATE, newState), 3);
        setChanged(level, pos, state);
    }

    public void setSpread(int spread) {
        this.spread = spread;
        this.setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("spread", spread);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.spread = input.getIntOr("placerName", MAX_SPREAD);
    }
}
