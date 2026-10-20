package net.lapointep.paulsmod.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class PaulsCommon {

    // --- 1. Chat & Messaging ---
    public static final Queue<PendingChatMessage> PendingChatMessages = new ConcurrentLinkedQueue<>();

    public record PendingChatMessage(ServerPlayer player, String message) {}

    public static void receiveChatMessage(ServerPlayer player, String message) {
        PendingChatMessages.clear();
        PendingChatMessages.add(new PendingChatMessage(player, message));
        PaulsMod.LOGGER.info("Messages received: " + PendingChatMessages.size());

    }



    // --- 2. World & Block Manipulation ---

    @Nullable
    public static BlockPos findFirstBlock(Level level, BlockPos centerPos, Block targetBlock, int range) {
        if (level == null || centerPos == null || targetBlock == null || range < 0) {
            return null;
        }

        BlockPos minPos = centerPos.offset(-range, -range, -range);
        BlockPos maxPos = centerPos.offset(range, range, range);

        // betweenClosed reuses a single MutableBlockPos instance during iteration to avoid allocations
        for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
            if (level.getBlockState(pos).is(targetBlock)) {
                // IMPORTANT: You MUST call .immutable() before returning,
                // otherwise the position value will mutate as the iterator advances.
                return pos.immutable();
            }
        }

        return null;
    }

    public static BlockPos findFirstNonEmptyCauldron(Level level, BlockPos startPos, int radius, boolean lookForWater) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = startPos.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (lookForWater) {
                        if (state.getBlock() instanceof LayeredCauldronBlock && state.getValue(LayeredCauldronBlock.LEVEL) > 0) {
                            return pos;
                        }
                    } else {
                        if (state.is(Blocks.LAVA_CAULDRON) || (state.getBlock() instanceof LayeredCauldronBlock && state.getValue(LayeredCauldronBlock.LEVEL) > 0)) {
                            return pos;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void lowerCauldronLevel(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof LayeredCauldronBlock) {
            int i = state.getValue(LayeredCauldronBlock.LEVEL);
            if (i > 1) {
                level.setBlockAndUpdate(pos, state.setValue(LayeredCauldronBlock.LEVEL, i - 1));
            } else {
                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
            }
        } else if (state.is(Blocks.LAVA_CAULDRON)) {
            level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
        }
    }

    public static void replaceBlock(Level level, BlockPos pos, Block newBlock) {
        if (!level.isClientSide()) {
            level.setBlock(pos, newBlock.defaultBlockState(), 3);
        }
    }

    public static int countAdjacentLava(BlockGetter level, BlockPos pos) {
        int count = 0;
        for (Direction direction : Direction.values()) {
            if (level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA)) {
                count++;
            }
        }
        return count;
    }

    public static int countAdjacentWater(BlockGetter level, BlockPos pos) {
        int count = 0;
        for (Direction direction : Direction.values()) {
            if (level.getFluidState(pos.relative(direction)).is(FluidTags.WATER)) {
                count++;
            }
        }
        return count;
    }

    public static void rotateBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        PaulsMod.LOGGER.info("Rotating block at {}, current state: {}", pos, state);
        net.minecraft.world.level.block.state.properties.Property<?> directionProperty = null;
        for (net.minecraft.world.level.block.state.properties.Property<?> prop : state.getProperties()) {
            PaulsMod.LOGGER.info("Property: {} = {}", prop.getName(), state.getValue(prop));
            if (prop.getName().contains("facing") || prop.getName().contains("rotation")) {
                directionProperty = prop;
                break;
            }
        }

        if (directionProperty != null) {
            PaulsMod.LOGGER.info("Found direction property: {}", directionProperty.getName());
            rotateHelperInternal(level, pos, state, directionProperty);
        } else {
            PaulsMod.LOGGER.info("No direction property found, using default rotation");
            level.setBlockAndUpdate(pos, state.rotate(Rotation.CLOCKWISE_90));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void rotateHelperInternal(Level level, BlockPos pos, BlockState state, net.minecraft.world.level.block.state.properties.Property<T> prop) {
        T current = state.getValue(prop);
        Collection<T> values = prop.getPossibleValues();
        List<T> sortedValues = new ArrayList<>(values);
        int index = sortedValues.indexOf(current);
        T next = sortedValues.get((index + 1) % sortedValues.size());
        //PaulsMod.LOGGER.info("Rotating {} from {} to {}", prop.getName(), current, next);
        BlockState newState = state.setValue(prop, next);
        //PaulsMod.LOGGER.info("Setting new state: {}", newState);
        level.setBlock(pos, newState, 3);
        level.sendBlockUpdated(pos, state, newState, 3);
        //PaulsMod.LOGGER.info("Block updated, new state is: {}", level.getBlockState(pos));
    }

    public static void spawnLightningAt(Level level, BlockPos pos, boolean doDamage) {
        // Lightning must be spawned on the logical server, not the client
        if (level instanceof ServerLevel) {
            // Instantiate using the plural EntityTypes registry
            LightningBolt lightning = new LightningBolt(EntityTypes.LIGHTNING_BOLT, level);

            // Set position (centered horizontally on the BlockPos)
            lightning.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

            // setVisualOnly(true) prevents the lightning from doing damage or starting fires
            lightning.setVisualOnly(!doDamage);

            // Spawn it into the world
            level.addFreshEntity(lightning);
        }
    }

    // --- 3. Entity Retrieval & Interaction ---
    public static List<ItemEntity> getItemsInArea(Level level, BlockPos pos, int radius) {
        AABB aabb = new AABB(pos).inflate(radius);
        return level.getEntitiesOfClass(ItemEntity.class, aabb);
    }

    public static List<Entity> getEntitiesInRange(Level level, BlockPos pos, double distance) {
        AABB aabb = new AABB(pos).inflate(distance);
        return level.getEntities((Entity)null, aabb);
    }

    /**
     * Counts the number of a specific entity type within a radius of a given position.
     *
     * @param level       The current server or client level (world)
     * @param entityClass The class of the entity to search for (e.g., Villager.class)
     * @param center      The center BlockPos to search around
     * @param range       The search radius in blocks
     * @param <T>         The entity type
     * @return The total count of valid entities of the specified type in the area
     */
    public static <T extends Entity> int countEntitiesInRange(Level level, Class<T> entityClass, BlockPos center, double range) {
        // Create an Axis-Aligned Bounding Box (AABB) at the block position,
        // inflated by the range in all directions
        AABB searchBox = new AABB(center).inflate(range);

        // Retrieve all instances of the generic entity type intersecting the bounding box
        List<T> entities = level.getEntitiesOfClass(
                entityClass,
                searchBox,
                entity -> entity.isAlive() && !entity.isSpectator()
        );

        return entities.size();
    }

    public static int countBedsInRange(Level level, BlockPos center, int range) {
        int count = 0;

        // Define the bounding box min/max positions
        BlockPos min = center.offset(-range, -range, -range);
        BlockPos max = center.offset(range, range, range);

        // Iterate through all block positions in the bounding area
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            BlockState state = level.getBlockState(pos);

            // Check if the block is tagged as a bed (covers all vanilla colors & modded beds)
            if (state.is(BlockTags.BEDS)) {
                // Beds are 2 blocks long. Only count the HEAD block to avoid double-counting.
                if (!state.hasProperty(BedBlock.PART) || state.getValue(BedBlock.PART) == BedPart.HEAD) {
                    count++;
                }
            }
        }

        return count;
    }

    public static void healOrDamagePlayer(Player player, float amount) {
        if (amount > 0) {
            player.heal(amount);
        } else {
            player.hurt(player.damageSources().generic(), -amount);
        }
    }

    public static void trackPlayers(Level level, HashMap<ServerPlayer, GameType> trackedPlayers, AABB warningZone, boolean changeModes, String owner, String welcome, String goodbye) {
        List<ServerPlayer> playersInZone = level.getEntitiesOfClass(ServerPlayer.class, warningZone);
        
        for (ServerPlayer player : playersInZone) {
            if (!trackedPlayers.containsKey(player)) {
                trackedPlayers.put(player, player.gameMode.getGameModeForPlayer());
                player.sendSystemMessage(Component.literal(welcome));
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1.0f, 1.0f);
                
                if (changeModes && !player.getGameProfile().name().equals(owner)) {
                    player.setGameMode(GameType.ADVENTURE);
                }
            }
        }

        Iterator<Map.Entry<ServerPlayer, GameType>> it = trackedPlayers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ServerPlayer, GameType> entry = it.next();
            ServerPlayer player = entry.getKey();
            if (!playersInZone.contains(player)) {
                player.sendSystemMessage(Component.literal(goodbye));
                if (changeModes && !player.getGameProfile().name().equals(owner)) {
                    player.setGameMode(entry.getValue());
                }
                it.remove();
            }
        }
    }

    public static BlockPos FindStructureMatching(ServerLevel level, String structureName, BlockPos originPos) {
        // 1. Parse string to Identifier
        Identifier structureId = IdentifyDesiredStructure(level, structureName);
        if (structureId == null) {
            structureId = IdentifyDesiredBiome(level, structureName);
            if (structureId == null) {
                return null;
            }
        }

        // 2. Create ResourceKey for the structure
        ResourceKey<Structure> structureKey = ResourceKey.create(Registries.STRUCTURE, structureId);

        // 3. Access the lookup provider via lookup()
        var lookupOptional = level.registryAccess().lookup(Registries.STRUCTURE);
        if (lookupOptional.isEmpty()) {
            return null;
        }
        var structureLookup = lookupOptional.get();

        // 4. Get the Holder for the structure key
        var structureHolderOptional = structureLookup.get(structureKey);
        if (structureHolderOptional.isEmpty()) {
            return null; // Structure key not registered
        }
        Holder<Structure> structureHolder = structureHolderOptional.get();

        // 5. Wrap holder in a HolderSet
        HolderSet<Structure> structureSet = HolderSet.direct(structureHolder);

        // 6. Search for nearest structure via the chunk generator
        int searchRadiusInChunks = 100;
        boolean skipExistingChunks = false;

        Pair<BlockPos, Holder<Structure>> result = level.getChunkSource().getGenerator()
                .findNearestMapStructure(level, structureSet, originPos, searchRadiusInChunks, skipExistingChunks);

        // 7. Return position if found
        if (result != null) {
            return result.getFirst();
        }

        return null;
    }

    public static Identifier IdentifyDesiredStructure(Level level, String desiredStructureName) {
        var lookupOptional = level.registryAccess().lookup(Registries.STRUCTURE);
        AtomicReference<Identifier> targetId = new AtomicReference<>();

        if (lookupOptional.isPresent()) {
            var structureLookup = lookupOptional.get();
            int maxScore = 0;
            structureLookup.listElementIds().forEach(resourceKey -> {
                Identifier id = resourceKey.identifier();
                String structureName = id.toString();

                PaulsMod.LOGGER.info("Registered structure: " + structureName);

                double score = getSimilarityScore(desiredStructureName, structureName);
                if (score > maxScore) {
                    targetId.set(id);
                }
            });
        }
        return targetId.get();
    }

    public static Identifier IdentifyDesiredBiome(Level level, String desiredStructureName) {
        var lookupOptional = level.registryAccess().lookup(Registries.BIOME);
        AtomicReference<Identifier> targetId = new AtomicReference<>();

        if (lookupOptional.isPresent()) {
            var structureLookup = lookupOptional.get();
            int maxScore = 0;
            structureLookup.listElementIds().forEach(resourceKey -> {
                Identifier id = resourceKey.identifier();
                String structureName = id.toString();

                PaulsMod.LOGGER.info("Registered biome: " + structureName);

                double score = getSimilarityScore(desiredStructureName, structureName);
                if (score > maxScore) {
                    targetId.set(id);
                }
            });
        }
        return targetId.get();
    }

    public static boolean tryInsertItem(Level level, BlockPos pos, @Nullable Direction side, ItemStack stack) {
        if (stack.isEmpty()) return false;

        // Find any inventory at this block (Vanilla or Modded)
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, side);
        if (storage == null) return false;

        long originalCount = stack.getCount();

        try (Transaction transaction = Transaction.openOuter()) {
            // Fabric API automatically prioritizes existing matching stacks, then empty slots
            long inserted = storage.insert(ItemVariant.of(stack), stack.getCount(), transaction);

            if (inserted > 0) {
                transaction.commit();          // Finalize the insertion
                stack.shrink((int) inserted);  // Reduce the stack by the amount accepted
                return true;
            }
        }

        return false;
    }


    public static boolean addItemStackToBlockInventory(BlockEntity be, ItemStack stack, Optional<Integer> slot) {
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(be.getLevel(), be.getBlockPos(), null);
        if (storage == null) return false;

        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = storage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            if (inserted > 0) {
                stack.shrink((int) inserted);
                transaction.commit();
                return true;
            }
        }
        return false;
    }

    public static boolean addItemStackToBlockInventory(Level level, BlockPos pos, ItemStack stack, Optional<Integer> slot) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be != null) {
            return addItemStackToBlockInventory(be, stack, slot);
        }
        return false;
    }

    public static boolean addItemStackToContainer(ItemStack stack, Container container) {
        if (stack.isEmpty()) {
            return false;
        }

        boolean itemsMoved = false;
        int size = container.getContainerSize();

        // Pass 1: Attempt to merge into existing slots containing the exact same item
        for (int i = 0; i < size; i++) {
            if (stack.isEmpty()) break;
            if (!container.canPlaceItem(i, stack)) continue;

            ItemStack slotStack = container.getItem(i);

            // Version Note: Use isSameItemSameTags() if you are on 1.20.4 or older.
            // 1.20.5+ replaced NBT with the component system.
            if (!slotStack.isEmpty() && ItemStack.isSameItemSameComponents(stack, slotStack)) {

                // Respect both the item's max stack limit and the container's max slot limit
                int maxStack = Math.min(slotStack.getMaxStackSize(), container.getMaxStackSize());
                int spaceLeft = maxStack - slotStack.getCount();

                if (spaceLeft > 0) {
                    int amountToMove = Math.min(stack.getCount(), spaceLeft);

                    slotStack.grow(amountToMove);
                    stack.shrink(amountToMove);
                    itemsMoved = true;
                }
            }
        }

        // Pass 2: Distribute any remaining items into empty slots
        for (int i = 0; i < size; i++) {
            if (stack.isEmpty()) break;
            if (!container.canPlaceItem(i, stack)) continue;

            if (container.getItem(i).isEmpty()) {
                int maxStack = Math.min(stack.getMaxStackSize(), container.getMaxStackSize());
                int amountToMove = Math.min(stack.getCount(), maxStack);

                // Copy the stack, set the count, and insert it
                ItemStack toInsert = stack.copy();
                toInsert.setCount(amountToMove);
                container.setItem(i, toInsert);

                // Shrink the original stack
                stack.shrink(amountToMove);
                itemsMoved = true;
            }
        }

        // Inform the game to save the inventory block if anything changed
        if (itemsMoved) {
            container.setChanged();
        }

        // Returns true if at least 1 item was successfully moved into the container
        return itemsMoved;
    }




    public static ItemStack removeItemStackFromBlockInventory(Level level, BlockPos pos, int slot, Optional<Integer> amount) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof net.minecraft.world.Container container) {
            int count = amount.orElse(container.getItem(slot).getCount());
            ItemStack removed = container.removeItem(slot, count);
            be.setChanged();
            return removed;
        }
        return ItemStack.EMPTY;
    }

    public static int GetFirstSlot(Level level, BlockPos blockPos, boolean empty) {
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof net.minecraft.world.Container container) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                if (empty) {
                    if (container.getItem(i).isEmpty()) return i;
                } else {
                    if (!container.getItem(i).isEmpty()) return i;
                }
            }
        }
        return -1;
    }


    public static boolean isItemFuel(Level level, ItemStack stack) {
        if (stack.is(Items.LAVA_BUCKET)) return true;
        // Fuel checks must now be queried against the world's specific FuelValues registry
        return level.fuelValues().isFuel(stack);
    }

    public static boolean hasItemQuantity(Player player, Item item, int amountRequired) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count >= amountRequired;
    }

    public static void consumeItemQuantity(Player player, Item item, int amountToRemove) {
        int remaining = amountToRemove;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                int toRemove = Math.min(remaining, stack.getCount());
                stack.shrink(toRemove);
                remaining -= toRemove;
                if (remaining <= 0) break;
            }
        }
    }

    public static int countItem(net.minecraft.world.Container inventory, Item targetItem) {
        int count = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(targetItem)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static void clearInventory(net.minecraft.world.Container inventory) {
        inventory.clearContent();
    }

    public static void giveItemToPlayerByName(ServerLevel level, String playerName, ItemStack stack) {
        ServerPlayer player = level.getServer().getPlayerList().getPlayerByName(playerName);
        if (player != null) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }
    }

    public static void GivePlayerFlorin(ServerLevel level, String playerName, int amount) {
        Item florin = BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath("paulsmod", "florin")).map(net.minecraft.core.Holder::value).orElse(Items.AIR);
        if (florin == Items.AIR) return;

        ItemStack stack = new ItemStack(florin, amount);
        giveItemToPlayerByName(level, playerName, stack);
    }

    public static @Nullable net.minecraft.world.phys.EntityHitResult getEntityHitResult(Player player, double range) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0f);
        Vec3 end = start.add(look.scale(range));
        AABB aabb = player.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0d);
        return net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(
                player,
                start,
                end,
                aabb,
                entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable(),
                range * range
        );
    }

    // --- 5. Villager Specific Actions ---
    public static void PutItemFromHandInBlock(ServerLevel level, Villager villager, InteractionHand hand, BlockPos pos, Optional<Integer> slot) {
        ItemStack stack = villager.getItemInHand(hand);
        if (stack.isEmpty()) return;

        if (addItemStackToBlockInventory(level, pos, stack, slot)) {
            if (stack.isEmpty()) {
                villager.setItemInHand(hand, ItemStack.EMPTY);
            }
        } else {
            DropItemFromHand(villager, hand);
        }
    }

    public static void DropItemFromHand(Villager villager, InteractionHand hand) {
        ItemStack stack = villager.getItemInHand(hand);
        if (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(villager.level(), villager.getX(), villager.getY(), villager.getZ(), stack.copy());
            villager.level().addFreshEntity(itemEntity);
            villager.setItemInHand(hand, ItemStack.EMPTY);
        }
    }

    public static void PickUpItemWithHand(ServerLevel level, Villager villager, InteractionHand hand, BlockPos pos, Optional<Integer> slot, Optional<Item> itemType) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof net.minecraft.world.Container container) {
            int targetSlot = slot.orElse(-1);
            if (targetSlot == -1) {
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack s = container.getItem(i);
                    if (!s.isEmpty() && (itemType.isEmpty() || s.is(itemType.get()))) {
                        targetSlot = i;
                        break;
                    }
                }
            }

            if (targetSlot != -1) {
                ItemStack stack = container.removeItem(targetSlot, 1);
                villager.setItemInHand(hand, stack);
                be.setChanged();
            }
        }
    }


    public static boolean hasThreeEmptyBottleSlots(BrewingStandBlockEntity brewingStand) {
        return brewingStand.getItem(0).isEmpty() && brewingStand.getItem(1).isEmpty() && brewingStand.getItem(2).isEmpty();
    }

    public static void transferPotions(BrewingStandBlockEntity brewingStand, net.minecraft.world.Container sourceInventory, int sourceSlot) {
        ItemStack stack = sourceInventory.getItem(sourceSlot);
        if (stack.isEmpty()) return;

        for (int i = 0; i < 3; i++) {
            if (brewingStand.getItem(i).isEmpty()) {
                brewingStand.setItem(i, stack.split(1));
                brewingStand.setChanged();
                if (stack.isEmpty()) break;
            }
        }
    }

    // --- 7. Furnace Support ---
    public static boolean FurnaceNeedsFuel(BlockPos blockPos, ServerLevel level) {
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof FurnaceBlockEntity furnace) {
            return furnace.getItem(1).isEmpty();
        }
        return false;
    }

    public static boolean FurnaceNeedsEmptying(BlockPos blockPos, ServerLevel level) {
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof FurnaceBlockEntity furnace) {
            return !furnace.getItem(2).isEmpty();
        }
        return false;
    }

    public static boolean FurnaceNeedsCleaning(BlockPos blockPos, ServerLevel level) {
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof FurnaceBlockEntity furnace) {
            return furnace.getItem(1).is(Items.BUCKET);
        }
        return false;
    }

    // --- 8. Math & Logic ---
    public static double getGaussianProbability(BlockPos pos1, BlockPos pos2, double maxProb, double sigma) {
        double distSq = pos1.distSqr(pos2);
        return maxProb * Math.exp(-distSq / (2 * sigma * sigma));
    }

    public static Direction getLeastDistanceDirection(BlockPos pos1, BlockPos pos2) {
        int dx = pos2.getX() - pos1.getX();
        int dy = pos2.getY() - pos1.getY();
        int dz = pos2.getZ() - pos1.getZ();

        int ax = Math.abs(dx);
        int ay = Math.abs(dy);
        int az = Math.abs(dz);

        if (ax != 0 && (ay == 0 || ax <= ay) && (az == 0 || ax <= az)) return dx > 0 ? Direction.EAST : Direction.WEST;
        if (ay != 0 && (ax == 0 || ay <= ax) && (az == 0 || ay <= az)) return dy > 0 ? Direction.UP : Direction.DOWN;
        if (az != 0 && (ax == 0 || az <= ax) && (ay == 0 || az <= ay)) return dz > 0 ? Direction.SOUTH : Direction.NORTH;

        return Direction.NORTH;
    }

    public static double getSimilarityScore(String s1, String s2) {
        int distance = levenshteinDistance(SimplifyString(s1), SimplifyString(s2));
        return 1.0 - ((double) distance / Math.max(s1.length(), s2.length()));
    }

    private static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[s1.length()][s2.length()];
    }

    public static String SimplifyString(String input) {
        return input.toLowerCase().replaceAll("[^a-z]", "");
    }

    // --- 9. Structure & WorldGen ---
    public static <T extends BlockEntity> List<BlockPos> BlockPosForType(ServerLevel level, BlockPos pos, int SEARCH_RANGE, Class<T> type) {
        List<BlockPos> positions = new ArrayList<>();
        for (int x = -SEARCH_RANGE; x <= SEARCH_RANGE; x++) {
            for (int y = -SEARCH_RANGE; y <= SEARCH_RANGE; y++) {
                for (int z = -SEARCH_RANGE; z <= SEARCH_RANGE; z++) {
                    BlockPos p = pos.offset(x, y, z);
                    BlockEntity be = level.getBlockEntity(p);
                    if (type.isInstance(be)) {
                        positions.add(p);
                    }
                }
            }
        }
        return positions;
    }

    // --- 10. Player Utility ---
    public static CompoundTag getExtraCustomData(Player player) {
        // Implement a way to get extra custom data, perhaps by casting to a mixin interface 
        // or using Cardinal Components. For now, we'll use a placeholder.
        return new CompoundTag();
    }

    @Environment(EnvType.CLIENT)
    public static void placeTextOnBlock(PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                                        Font font, int color, float scale, String text,
                                        int packedLight, float x, float y, float z) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.scale(scale, -scale, scale); // Invert Y for standard text orientation

        Component component = Component.literal(text);
        FormattedCharSequence sequence = component.getVisualOrderText();

        submitNodeCollector.submitText(
                poseStack,                 // Pass the full PoseStack, not just the Matrix4f
                -font.width(text) / 2.0f,  // x-coordinate (center alignment offset)
                0.0f,                      // y-coordinate
                sequence, // The FormattedCharSequence to render
                false,                     // Drop shadow
                Font.DisplayMode.NORMAL,   // Display mode
                packedLight,               // Light coordinates
                color,                     // Text color (ARGB)
                0,                         // Background color
                0                          // Outline color
        );

        poseStack.popPose();
    }

    public static String[] splitByLength(String str, int size) {
        if (str == null || str.isEmpty() || size <= 0) {
            return new String[0];
        }

        int length = str.length();
        int numOfChunks = (int) Math.ceil((double) length / size);
        String[] chunks = new String[numOfChunks];

        for (int i = 0; i < numOfChunks; i++) {
            int start = i * size;
            int end = Math.min(length, start + size);
            chunks[i] = str.substring(start, end);
        }

        return chunks;
    }


    // --- 4. Inventory & Item Management ---
    public enum ItemTypes {
        Metal,
        Crops,
        Food,
        Tools,
        Armor,
        Alchemy,
        Wood,
        Stone,
        Redstone,
        Books,
        Misc,
        Gems,
        Dyes,
        All,
        FUEL,
        Terracotta,
        Furnace,
        BlastFurnace,
        Smoker
    }

    public static ItemTypes getItemType(ItemStack stack) {
        if (stack.isEmpty()) return ItemTypes.Misc;

        Item item = stack.getItem();

        // Crops
        if (stack.is(ConventionalItemTags.CROPS) ||
                stack.is(ConventionalItemTags.SEEDS) ||
                stack.is(Items.CHORUS_FRUIT) ||
                stack.is(Items.CHORUS_FLOWER) ||
                stack.is(Items.SUGAR_CANE) ||
                stack.is(Items.BONE) ||
                item instanceof BoneMealItem

        ) {
            return ItemTypes.Crops;
        }

        // Gems
        if (stack.is(ConventionalItemTags.GEMS) ||
                (stack.is(Items.ENDER_EYE)) ||
                (stack.is(Items.ENDER_PEARL)) ||
                (stack.is(ModItems.TRAVELING_STONE)) ||
                (stack.is(Items.EMERALD)) ||
                (stack.is(Items.EMERALD_BLOCK)) ||
                (stack.is(Items.DIAMOND)) ||
                (stack.is(Items.AMETHYST_BLOCK)) ||
                (stack.is(Items.AMETHYST_SHARD)) ||
                (stack.is(Items.AMETHYST_CLUSTER)) ||
                (stack.is(Items.QUARTZ))
        )  {
            return ItemTypes.Gems;
        }

        // Tools
        if (stack.is(ConventionalItemTags.TOOLS) ||
                item instanceof ProjectileWeaponItem ||
                item instanceof FishingRodItem ||
                item == Items.BUCKET ||
                item == Items.STRING ||
                item == Items.FLINT_AND_STEEL ||
                item == Items.STICK ||
                item == Items.GOAT_HORN ||
                item == Items.TORCH ||
                item == Items.FEATHER ||
                item == Items.FLINT ||
                item == Items.SHEARS ||
                item == Items.ANVIL ||
                item instanceof ShearsItem) {
            return ItemTypes.Tools;
        }

        // Armor
        if (stack.is(ConventionalItemTags.ARMORS) || item == Items.ELYTRA) {
            return ItemTypes.Armor;
        }

        // Alchemy
        if (item instanceof PotionItem ||
                item instanceof BottleItem ||
                item == Items.GLOWSTONE_DUST ||
                item == Items.NETHER_WART ||
                item == Items.GLASS_BOTTLE ||
                item == Items.POTION ||
                item == Items.SPLASH_POTION ||
                item == ModItems.COPPER_POWDER ||
                item == ModItems.IRON_POWDER||
                item == ModItems.UNSTABLE_POWDER||
                item == ModItems.NETHER_POWDER ||
                item == ModItems.LAVA_POWDER ||
                item == Items.LINGERING_POTION) {
            // Note: Redstone is also a separate category, but in alchemy it's an ingredient.
            // Let's check Redstone specifically after Alchemy if needed.
            return ItemTypes.Alchemy;
        }

        if (stack.is(Items.ROTTEN_FLESH) ||
                stack.is(Items.CHICKEN) ||
                stack.is(Items.PORKCHOP) ||
                stack.is(Items.BEEF) ||
                stack.is(Items.MUTTON) ||
                stack.is(Items.RABBIT) ||
                stack.is(Items.POTATO)
        ) {
            return ItemTypes.Smoker;
        }

        if (stack.is(ConventionalItemTags.FOODS)
                || (item == Items.SUGAR)
                || (item == Items.MILK_BUCKET)
                || (item == Items.EGG)
                || stack.getDisplayName().getString().contains("Egg")
                || (item == Items.PUMPKIN)
        )
        {
            return ItemTypes.Food;
        }

        // Redstone
        if (stack.is(ConventionalItemTags.REDSTONE_DUSTS) ||
                stack.is(ConventionalItemTags.REDSTONE_ORES) ||
                stack.is(Items.REDSTONE) ||
                stack.is(Items.REDSTONE_TORCH) ||
                stack.is(Items.REDSTONE_LAMP) ||
                stack.is(Items.REDSTONE_BLOCK) ||
                stack.is(Items.COMPARATOR) ||
                stack.is(Items.REPEATER) ||
                stack.is(Items.OBSERVER) ||
                stack.getDisplayName().getString().contains("redstone")
        ) {
            return ItemTypes.Redstone;
        }

        // FUEL
        if (stack.is(Items.COAL) ||
                stack.is(Items.COAL_BLOCK) ||
                stack.is(Items.COAL_ORE) ||
                stack.is(Items.BLAZE_POWDER) ||
                stack.is(Items.TNT) ||
                stack.is(Items.GUNPOWDER) ||
                stack.is(Items.BLAZE_ROD) ||
                stack.is(Items.LAVA_BUCKET)
        ) {
            return ItemTypes.FUEL;
        }

        // Books
        if (item == Items.BOOK ||
                item == Items.ENCHANTED_BOOK ||
                item == Items.WRITABLE_BOOK ||
                item == Items.WRITTEN_BOOK ||
                item == Items.PAPER ||
                item == Items.LEATHER ||
                item == Items.MAP ||
                item == Items.NAME_TAG ||
                item == Items.FILLED_MAP ||
                stack.getDisplayName().toString().contains("template") ||
                stack.getDisplayName().toString().contains("wool") ||
                stack.getDisplayName().toString().contains("banner"))
        {
            return ItemTypes.Books;
        }

        if (stack.is(ItemTags.PLANKS) ||
                stack.is(Items.BAMBOO) ||
                stack.is(ItemTags.LOGS) ||
                stack.is(ItemTags.LOGS_THAT_BURN) ||
                stack.is(ItemTags.WOODEN_SLABS) ||
                stack.is(ItemTags.WOODEN_STAIRS) ||
                stack.is(ItemTags.WOODEN_BUTTONS) ||
                stack.is(Items.SCAFFOLDING) ||
                stack.is(ItemTags.WOODEN_DOORS) ||
                stack.is(ItemTags.WOODEN_TRAPDOORS) ||
                stack.is(ItemTags.WOODEN_FENCES) ||
                stack.is(ItemTags.WOODEN_PRESSURE_PLATES) ||
                stack.getDisplayName().toString().contains("Sapling")
        ) {
            return ItemTypes.Wood;
        }

        if (stack.is(ItemTags.STONE_BRICKS) ||
                stack.is(ItemTags.STONE_CRAFTING_MATERIALS) ||
                stack.is(ItemTags.STONE_TOOL_MATERIALS) ||
                stack.is(Items.GRAVEL) ||
                stack.is(Items.SAND) ||
                stack.is(Items.RED_SAND) ||
                stack.is(Items.BASALT) ||
                stack.is(Items.CLAY) ||
                stack.is(Items.CLAY_BALL) ||
                stack.is(Items.NETHERRACK) ||
                stack.is(Items.NETHER_BRICK) ||
                stack.is(Items.BRICK) ||
                stack.is(Items.BRICKS) ||
                stack.is(Items.CLAY) ||
                stack.is(Items.CALCITE) ||
                stack.is(Items.GLASS) ||
                stack.is(ConventionalItemTags.GLASS_BLOCKS) ||
                stack.is(ConventionalItemTags.GLASS_PANES) ||
                stack.is(ConventionalItemTags.GLASS_PANES_COLORLESS) ||
                stack.getDisplayName().getString().contains("glass") ||
                stack.getDisplayName().getString().contains("Cobblestone") ||
                stack.getDisplayName().getString().contains("Deepslate") ||
                stack.getDisplayName().getString().contains("Granite") ||
                stack.getDisplayName().getString().contains("Brick") ||
                stack.getDisplayName().getString().contains("Diorite") ||
                stack.getDisplayName().getString().contains("Concrete") ||
                stack.getDisplayName().getString().contains("Wall") ||
                stack.getDisplayName().getString().contains("Andesite") ||
                stack.getDisplayName().getString().contains("Stone") ||
                stack.getDisplayName().getString().contains("Sand")

        ) {
            return ItemTypes.Stone;
        }

        // Dyes
        if (stack.is(ConventionalItemTags.DYES)
        ) {
            return ItemTypes.Dyes;
        }

        // Terracotta
        if (stack.getDisplayName().toString().contains("terracotta")) {
            return ItemTypes.Terracotta;
        }

        // Metals
        if (stack.is(ConventionalItemTags.NUGGETS) ||
                stack.is(ConventionalItemTags.INGOTS) ||
                stack.is(ConventionalItemTags.STORAGE_BLOCKS_GOLD) ||
                stack.is(ConventionalItemTags.STORAGE_BLOCKS_IRON) ||
                stack.is(ConventionalItemTags.STORAGE_BLOCKS_COPPER) ||
                stack.is(ConventionalItemTags.STORAGE_BLOCKS_NETHERITE) ||
                stack.getDisplayName().toString().contains("ingot"))
        {
            return ItemTypes.Metal;
        }

        if (stack.is(ConventionalItemTags.ORES) ||
                stack.is(ConventionalItemTags.RAW_MATERIALS) ||
                stack.is(ConventionalItemTags.IRON_ORES) ||
                stack.is(ConventionalItemTags.COPPER_ORES) ||
                stack.is(ConventionalItemTags.GOLD_ORES) ||
                item == Items.RAW_COPPER ||
                item == Items.RAW_IRON ||
                item == Items.RAW_GOLD
        ) {
            return ItemTypes.BlastFurnace;
        }



        if (stack.is(Items.NETHERRACK) ||
                stack.is(Items.CLAY_BALL)
        ) {
            return ItemTypes.Furnace;
        }

        return ItemTypes.Misc;
    }

    public static int get12HourTime(Level level) {
        // The total game time continuously increases. % 24000 gives us the current day's time.
        long timeOfDay = level.getOverworldClockTime() % 24000;

        // Minecraft day starts at 6:00 AM (0 ticks).
        // We add 6000 to shift the zero point to Midnight (12:00 AM) to make the math easier.
        long adjustedTime = (timeOfDay + 6000) % 24000;

        // 1000 ticks = 1 in-game hour
        long hours24 = adjustedTime / 1000;
        // Convert 24-hour time to 12-hour time
        long hours12 = hours24 % 12;
        if (hours12 == 0) {
            hours12 = 12; // Handles 12:00 AM and 12:00 PM
        }

        return (int)hours12;
    }

    public static String getRomanNumeral(int number) {
        String[] romanNumerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"};
        return romanNumerals[number - 1];
    }
}
