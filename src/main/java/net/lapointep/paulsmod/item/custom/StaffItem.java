package net.lapointep.paulsmod.item.custom;

import com.mojang.brigadier.Message;
import com.mojang.datafixers.util.Pair;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.enchantments.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.lapointep.paulsmod.util.PaulsCommon;

import java.util.ArrayList;

import static net.lapointep.paulsmod.util.PaulsCommon.getRomanNumeral;


public class StaffItem extends Item {

    public enum StaffType {
        staff, diamond, emerald, am, end
    }

    public StaffType staffType;

    public StaffItem(StaffType type, Properties properties) {
        super(properties);
        staffType = type;
    }

    public int getStaffCooldown() {
        if (staffType == StaffType.staff) {
            return 40;
        }
        else if (staffType == StaffType.diamond) {
            return 20;
        }
        else if (staffType == StaffType.emerald) {
            return 10;
        }
        else if (staffType == StaffType.am) {
            return 5;
        }
        else if (staffType == StaffType.end) {
            return 0;
        }

        return 0;
    }

    public boolean isCoolDownOver(ItemStack stack, Level level) {
        long lastUsedTime = getLastUsedTime(stack);
        return level.getGameTime() - lastUsedTime >= getStaffCooldown();
    }

    private long getLastUsedTime(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("lastUsedTime")) {
                return tag.getLongOr("lastUsedTime", 0L);
            }
        }
        return 0L;
    }

    private void setLastUsedTime(ItemStack stack, long time) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putLong("lastUsedTime", time);
        });
    }

    private int getCurrentSpell(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("currentSpell")) {
                return tag.getIntOr("currentSpell", 0);
            }
        }
        return 0;
    }

    private void setCurrentSpell(ItemStack stack, int spell) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putInt("currentSpell", spell);
        });
    }

    public int getCurrentEnchantmentLevel(ItemStack stack) {
        ArrayList<Pair<Enchantment, Integer>> enchantments = EnchantmentUtils.getEnchantmentsOnItem(stack);
        if (enchantments.isEmpty()) {
            return 0;
        }
        int currentSpell = getCurrentSpell(stack);
        if (currentSpell >= 0 && currentSpell < enchantments.size()) {
            return enchantments.get(currentSpell).getSecond();
        }
        return 0;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        PaulsMod.LOGGER.info("1: Player {} is using staff", player.getName().getString());

        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack staff = player.getItemInHand(hand);
            ArrayList<Pair<Enchantment, Integer>> enchantments = EnchantmentUtils.getEnchantmentsOnItem(staff);
            if (enchantments.isEmpty()) {
                PaulsMod.LOGGER.info("Player {} tried to cast spell without enchantments", player.getName().getString());
                return InteractionResult.PASS;
            }

            if (player.isCrouching()) {
                cycleSpell(staff, player);
                return InteractionResult.SUCCESS;
            }
            else {
                if (!isCoolDownOver(staff, level)) {
                    player.sendSystemMessage(Component.translatable("Staff is not ready"));
                    return InteractionResult.SUCCESS;
                }
                
                int currentSpell = getCurrentSpell(staff);
                Pair<Enchantment, Integer> enchantmentEntry = enchantments.get(currentSpell);
                Enchantment enchantment = enchantmentEntry.getFirst();
                Integer enchantmentLevel = enchantmentEntry.getSecond();
                
                PaulsMod.LOGGER.info("Player {} is casting spell {} Lvl {}", player.getName().getString(), enchantment.toString(), enchantmentLevel);
                
                // Prevent the player from casting harmful spells on themselves (enflame, lightning, bury, arise)
                if (isHarmfulSpell(enchantment)) {
                    EntityHitResult hitResult = PaulsCommon.getEntityHitResult(player, 10.0D);
                    if (hitResult != null && hitResult.getEntity() == player) {
                        player.sendSystemMessage(Component.translatable("Cannot cast this spell on yourself"));
                        return InteractionResult.SUCCESS;
                    }
                }

                EntityHitResult hitResult = PaulsCommon.getEntityHitResult(player, 10.0D);
                if (hitResult != null && hitResult.getEntity() instanceof LivingEntity target) {

                    castSpellOnTarget(staff, target, player, enchantment, enchantmentLevel);
                } else {
                    // If no entity hit, try casting at the location the player is looking at
                    Vec3 lookVector = player.getLookAngle();
                    Vec3 startPos = player.getEyePosition();
                    Vec3 endPos = startPos.add(lookVector.scale(10.0D));

                    // For harmful spells, prevent casting at own location if fallback to self happens
                    if (isHarmfulSpell(enchantment)) {
                        // In castSpellAtLocation, the attacker (player) is used as the victim.
                        // We should prevent this if it's a harmful spell.
                        player.sendSystemMessage(Component.translatable("Cannot cast this spell on yourself"));
                        return InteractionResult.SUCCESS;
                    }

                    castSpellAtLocation(staff, player, enchantment, enchantmentLevel, endPos);
                }
                giveExperience(player, enchantmentLevel);
                damageStaff(staff, player);
                setLastUsedTime(staff, level.getGameTime());
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.SUCCESS;
    }

    public void damageStaff(ItemStack staff, LivingEntity entity) {
        int amount = getCurrentEnchantmentLevel(staff);
        staff.hurtAndBreak(amount, entity, EquipmentSlot.MAINHAND);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Run logic on the logical server side to prevent double-execution
        if (!attacker.level().isClientSide()) {

            ArrayList<Pair<Enchantment, Integer>> enchantments = EnchantmentUtils.getEnchantmentsOnItem(stack);
            if (enchantments.isEmpty()) {
                PaulsMod.LOGGER.info("Player {} tried to cast spell without enchantments", attacker.getName().getString());
                super.hurtEnemy(stack, target, attacker);
                return;
            }

            int currentSpell = getCurrentSpell(stack);
            Pair<Enchantment, Integer> enchantmentEntry = enchantments.get(currentSpell);
            Enchantment enchantment = enchantmentEntry.getFirst();
            Integer enchantmentLevel = enchantmentEntry.getSecond();

            if (isHarmfulSpell(enchantment) && target == attacker) {
                if (attacker instanceof Player player) {
                    player.sendSystemMessage(Component.translatable("Cannot cast this spell on yourself"));
                }
                return;
            }

            castSpellOnTarget(stack, target, attacker, enchantment, enchantmentLevel);

        }
        damageStaff(stack, attacker);
        // Must return true for the hit to be counted (e.g. for statistics/durability)
        super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void postHurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        //super.postHurtEnemy(itemStack, mob, attacker);

        //PaulsMod.LOGGER.info("Player {} is attacking {}", attacker.getName().getString(), mob.getName().getString());
        damageStaff(itemStack, attacker);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // Use on a block, not an entity
        PaulsMod.LOGGER.info("2: Player {} is using staff", context.getPlayer().getName().getString());
        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        BlockPos targetPos = context.getClickedPos();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack staff = player.getItemInHand(hand);
            ArrayList<Pair<Enchantment, Integer>> enchantments = EnchantmentUtils.getEnchantmentsOnItem(staff);
            int currentSpell = getCurrentSpell(staff);
            Pair<Enchantment, Integer> enchantmentEntry = enchantments.get(currentSpell);
            Enchantment enchantment = enchantmentEntry.getFirst();
            Integer enchantmentLevel = enchantmentEntry.getSecond();

            if (player.isCrouching()) {
                cycleSpell(staff, player);
                return InteractionResult.SUCCESS;
            }
            else {
                if (!isCoolDownOver(staff, level)) {
                    player.sendSystemMessage(Component.translatable("Staff is not ready"));
                    return InteractionResult.SUCCESS;
                }

                giveExperience(player, enchantmentLevel);
                PaulsMod.LOGGER.info("Player {} is casting spell {} Lvl {}", player.getName().getString(),enchantment.toString(),enchantmentLevel);
                if (isHarmfulSpell(enchantment)) {
                    player.sendSystemMessage(Component.translatable("Cannot cast this spell on yourself"));
                    return InteractionResult.SUCCESS;
                }

                Vec3 centerPos = Vec3.atCenterOf(targetPos);
                castSpellAtLocation(staff, player, enchantment, enchantmentLevel, centerPos);

                damageStaff(staff, player);
                setLastUsedTime(staff, level.getGameTime());
            }
        }

        return InteractionResult.SUCCESS;
    }

    public StaffType getStaffType() {
        return this.staffType;
    }

    public void giveExperience(Player player, int enchantmentLevel) {
        int experienceBoost = enchantmentLevel * getStaffType().ordinal();
        player.experienceLevel = player.experienceLevel + experienceBoost;
    }

    public void cycleSpell(ItemStack stack, Player player) {
        PaulsMod.LOGGER.info("Player {} is switching spells", player.getName().getString());
        ArrayList<Pair<Enchantment, Integer>> enchantments = EnchantmentUtils.getEnchantmentsOnItem(stack);
        int currentSpell = (getCurrentSpell(stack) + 1) % enchantments.size();
        setCurrentSpell(stack, currentSpell);

        Pair<Enchantment, Integer> enchantmentEntry = enchantments.get(currentSpell);
        Enchantment enchantment = enchantmentEntry.getFirst();
        Integer enchantmentLevel = enchantmentEntry.getSecond();

        PaulsMod.LOGGER.info("Using spell {} Lvl {}", enchantment.toString(), enchantmentLevel);
        player.sendSystemMessage(Component.translatable("%s %s", enchantment.toString(), getRomanNumeral(enchantmentLevel)));
    }

    private void castSpellOnTarget(ItemStack stack, Entity target, Entity attacker, Enchantment enchantment, int level) {
        if (attacker.level() instanceof ServerLevel serverLevel && attacker instanceof LivingEntity livingAttacker) {
            EnchantedItemInUse context = new EnchantedItemInUse(stack, EquipmentSlot.MAINHAND, livingAttacker);
            var postAttackEffects = enchantment.effects().get(EnchantmentEffectComponents.POST_ATTACK);
            if (postAttackEffects != null) {
                postAttackEffects.forEach(effect -> {
                    effect.effect().apply(serverLevel, level, context, target, target.position());
                });
            }
        }
    }

    private void castSpellAtLocation(ItemStack stack, Entity attacker, Enchantment enchantment, int level, Vec3 pos) {
        if (attacker.level() instanceof ServerLevel serverLevel && attacker instanceof LivingEntity livingAttacker) {
            EnchantedItemInUse context = new EnchantedItemInUse(stack, EquipmentSlot.MAINHAND, livingAttacker);
            // In 1.21, post_attack effects are the primary way to cast spells on entities.
            // We use them even for location-based casting by using the attacker as the "victim" at the target position.
            var postAttackEffects = enchantment.effects().get(EnchantmentEffectComponents.POST_ATTACK);
            if (postAttackEffects != null) {
                postAttackEffects.forEach(effect -> {
                    effect.effect().apply(serverLevel, level, context, attacker, pos);
                });
            }
        }
    }

    private boolean isHarmfulSpell(Enchantment enchantment) {
        var postAttackEffects = enchantment.effects().get(EnchantmentEffectComponents.POST_ATTACK);
        if (postAttackEffects != null) {
            for (var effect : postAttackEffects) {
                var entityEffect = effect.effect();
                if (entityEffect instanceof EnflameEnchantmentEffect ||
                    entityEffect instanceof LighteningEnchantmentEffect ||
                    entityEffect instanceof BuryEnchantmentEffect ||
                    entityEffect instanceof FreezeEnchantmentEffect ||
                    entityEffect instanceof AriseEnchantmentEffect) {
                    return true;
                }
            }
        }
        return false;
    }
}
