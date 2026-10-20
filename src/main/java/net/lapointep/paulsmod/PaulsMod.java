package net.lapointep.paulsmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.block.entity.ModBlockEntities;
import net.lapointep.paulsmod.creativemodetab.ModCreativeModeTabs;
import net.lapointep.paulsmod.datagen.ModMobLootTableProvider;
import net.lapointep.paulsmod.effect.ModEffects;
import net.lapointep.paulsmod.enchantments.ModEnchantmentEffects;
import net.lapointep.paulsmod.entity.ModEntities;
import net.lapointep.paulsmod.event.ChatEventListener;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.menu.ModMenuTypes;
import net.lapointep.paulsmod.network.ModMessages;
import net.lapointep.paulsmod.potions.ModPotionRecipes;
import net.lapointep.paulsmod.potions.ModPotions;
import net.lapointep.paulsmod.util.BankAccount;
import net.lapointep.paulsmod.util.BankSavedData;
import net.lapointep.paulsmod.util.ServerProvider;
import net.lapointep.paulsmod.worldgen.ModPlacedFeatures;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Important Comment
public class PaulsMod implements ModInitializer {
	public static final String MOD_ID = "paulsmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Map<String, BankAccount> bankAccounts = new HashMap<>();

	private static MinecraftServer currentServer;

	@Override
	public void onInitialize() {
		ModCreativeModeTabs.registerModCreativeModeTabs();
        ServerProvider.register();

		ModItems.registerModItems();
		ModEntities.registerModEntities();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModMenuTypes.registerMenus();
		ModEffects.registerModEffects();
        ModEnchantmentEffects.registerModEnchantmentEffects();
        ModPotions.registerPotions();
		ModMessages.register();
		ModMessages.registerServerHandlers();
        ModPotionRecipes.registerPotionRecipes();
        ChatEventListener.register();

		ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
		ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);

        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (entity.level().isClientSide()) { return true; }
            if (isAnyonesPet(entity)) {

                // 2. Check if the direct or indirect attacker is a player
                if (damageSource.getEntity() instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer) damageSource.getEntity();
                    LivingEntity owner = getOwner(entity);
                    if (owner != null && owner.equals(player)) {
                        PaulsMod.LOGGER.info(player.getName().getString() + " killed their own pet!");

                        return true;
                    }
                    entity.setHealth(entity.getMaxHealth());
                    PaulsMod.LOGGER.info("Prevented " + player.getName().getString() + " from killing a cat!");

                    // 4. Return false to cancel death
                    return false;
                }
            }

            // Return true to allow normal death for all other entities/sources
            return true;
        });

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.LUSH_CAVES),
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModPlacedFeatures.SEEING_POOL_PLACED_KEY
        );

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            ModMobLootTableProvider.modifyLootTables(key, tableBuilder, source, registries);
        });
	}

    public static LivingEntity getOwner(Entity entity) {
        if (entity instanceof TamableAnimal tamable) {
            return tamable.getOwner();
        }
        else if (entity instanceof AbstractHorse horse) {
            return horse.getOwner();
        }
        return null;
    }

    public static boolean isAnyonesPet(Entity entity) {
        // 1. Cats, Dogs, Parrots
        if (entity instanceof TamableAnimal tamable) {
            return tamable.isTame() && tamable.getOwner() != null;
        }

        // 2. Horses, Donkeys, Mules, Llamas
        if (entity instanceof AbstractHorse horse) {
            return horse.isTamed() && horse.getOwner() != null;
        }

        return false;
    }

	public void onServerStarting(MinecraftServer server) {
		currentServer = server;
		BankSavedData.get(server.overworld());
	}

	public void onServerStopped(MinecraftServer server) {
		currentServer = null;
	}

	public List<BankAccount> getBankAccounts() {
		return new ArrayList<>(bankAccounts.values());
	}

	public BankAccount getBankAccount(String name) {
		return bankAccounts.get(name);
	}

	private void markDirty() {
		MinecraftServer server = currentServer;
		if (server != null) {
			BankSavedData.get(server.overworld()).setDirty();
		}
	}

	public static void Deposit(String name, int amount) {
		BankAccount account = bankAccounts.computeIfAbsent(name, k -> new BankAccount(k, 0));
		account.deposit(amount);
        PaulsMod.LOGGER.info("Deposited " + amount + " to " + name);
		//markDirty();
	}

	public static int Withdraw(String name, int amount) {
		BankAccount account = bankAccounts.get(name);
		if (account != null) {
			int withdrawn = account.withdraw(amount);
			//markDirty();
            PaulsMod.LOGGER.info("Withdrew " + withdrawn + " from " + name);
			return withdrawn;
		}
		return 0;
	}
}