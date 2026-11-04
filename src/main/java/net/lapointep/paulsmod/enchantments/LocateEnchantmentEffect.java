package net.lapointep.paulsmod.enchantments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.entity.GuideEntity;
import net.lapointep.paulsmod.entity.ModEntities;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record LocateEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<LocateEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(LocateEnchantmentEffect::amount)
    ).apply(instance, LocateEnchantmentEffect::new));

    public static final int RANGE = 32;
    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof Player player) {
            BlockPos blockPos = player.blockPosition();
            List<Entity> entitiesList = PaulsCommon.getEntitiesInRange(serverLevel, blockPos, RANGE);
            for (Entity entity: entitiesList) {
                if ((entity instanceof Pillager) || (entity instanceof Ravager)) {
                    BlockPos dest = entity.blockPosition();
                    // Spawn a guide entity to point towards the pillager/ravager
                    // Create and position entity at player eye height
                    GuideEntity guide = ModEntities.GUIDE_ENTITY.create(serverLevel, EntitySpawnReason.TRIGGERED);
                    if (guide != null) {
                        PaulsMod.LOGGER.info("Creating guide entity at " + pos);
                        Vec3 spawnPos = player.getEyePosition().add(player.getLookAngle().scale(0.5));

                        // Set position and rotation using explicit Mojang mapping setters
                        guide.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                        guide.setYRot(player.getYRot());
                        guide.setXRot(player.getXRot());

                        // Sync old position and rotation to prevent client-side interpolation glitches from (0,0,0)
                        guide.setOldPosAndRot();

                        Vec3 direction = PaulsCommon.GetDirectionToTarget(player, dest);
                        guide.setGuideDirection(direction, 0.2); // 0.2 blocks/tick speed
                        serverLevel.addFreshEntity(guide);
                    }
                    else {
                        PaulsMod.LOGGER.error("Guide entity is null");
                    }
                }
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}