package net.lapointep.paulsmod.item.custom;

import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.effect.ModEffects;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ElvenRingItem extends DwarvenRingItem {
    private static final Identifier HEALTH_MODIFIER_ID = Identifier.fromNamespaceAndPath("paulsmod", "elven_ring_health");

    public ElvenRingItem(Properties properties) {
        super(properties);
    }
    public TargetType currentTarget = TargetType.IRON;

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level instanceof ServerLevel serverLevel) {
            if (player instanceof ServerPlayer serverPlayer) {

                // First time use logic
                // We check if the modifier is already present on the player.
                AttributeInstance healthAttribute = serverPlayer.getAttribute(Attributes.MAX_HEALTH);
                if (healthAttribute != null && healthAttribute.getModifier(HEALTH_MODIFIER_ID) == null) {
                    healthAttribute.addPermanentModifier(new AttributeModifier(HEALTH_MODIFIER_ID, 20.0, AttributeModifier.Operation.ADD_VALUE));
                    serverPlayer.setHealth(serverPlayer.getMaxHealth()); // Heal to new max

                    // Increase rate of healing by 50%
                    // Applying a permanent healing effect (amplifier 0).
                    serverPlayer.addEffect(new MobEffectInstance(net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.HEALING), -1, 0, false, false, true));
                    serverPlayer.sendSystemMessage(Component.literal("The Elven Ring pulses with ancient energy, granting you increased health and vitality!"));
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);
                }
            }
        }

        return super.use(level, player, hand);
    }

}
