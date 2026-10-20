package net.lapointep.paulsmod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class Destination {
    private final BlockPos pos;
    private final ResourceKey<Level> dimension;

    public Destination(BlockPos pos, ResourceKey<Level> dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public BlockEntity getBlockEntity() {
        MinecraftServer server = ServerProvider.getServer();
        if (server != null) {
            ServerLevel level = server.getLevel(this.dimension);
            if (level != null) {
                return level.getBlockEntity(this.pos);
            }
        }
        return null;
    }

    /*
    public BlockPos getDirection(BlockPos fromPos) {
        return new BlockPos(this.pos.getX() - fromPos.getX(), this.pos.getY() - fromPos.getY(), this.pos.getZ() - fromPos.getZ());
    }

    public void teleport(ServerPlayer player) {
        ServerLevel targetLevel = player.level().getServer().getLevel(this.dimension);
        if (targetLevel != null) {
            player.teleportTo(targetLevel, this.pos.getX() + 0.5, this.pos.getY(), this.pos.getZ() + 0.5, Set.of(), player.getYRot(), player.getXRot(), false);
        }
    }
    */

    public Vec3 getDirectionToTarget(Player player) {
        Vec3 startPos = player.getEyePosition();
        Vec3 targetVec = Vec3.atCenterOf(pos);
        return targetVec.subtract(startPos).normalize();
    }

    public void teleport(Entity entity) {
      //  if (entity instanceof ServerPlayer player) {
     //       teleport(player);
      //  } else

        if (entity.level() instanceof ServerLevel level) {
            ServerLevel targetLevel = level.getServer().getLevel(this.dimension);
            if (targetLevel != null) {
                entity.teleportTo(targetLevel, this.pos.getX() + 0.5, this.pos.getY()+1, this.pos.getZ() + 0.5, Set.of(), entity.getYRot(), entity.getXRot(), false);
            }
        }
    }
/*
    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putLong("pos", this.pos.asLong());
        nbt.putString("dimension", this.dimension.identifier().toString());
        return nbt;
    }

    public static Destination fromNbt(CompoundTag nbt) {
        BlockPos pos = BlockPos.of(nbt.getLongOr("pos", 0L));
        ResourceKey<Level> dimension = ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, Identifier.parse(nbt.getStringOr("dimension", "minecraft:overworld")));
        return new Destination(pos, dimension);
    }
    */
}
