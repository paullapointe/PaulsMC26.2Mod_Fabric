package net.lapointep.paulsmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lapointep.paulsmod.block.ModBlocks;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.potions.ModPotions;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    private final CompletableFuture<HolderLookup.Provider> registriesFuture;
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
        this.registriesFuture = registriesFuture;
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {

                shapeless(RecipeCategory.MISC, ModItems.AM_STAFF, 1)
                        .requires(ModItems.STAFF,1)
                        .requires(Items.AMETHYST_SHARD,1)
                        .unlockedBy(getHasName(ModItems.STAFF), has(ModItems.STAFF))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.EM_STAFF, 1)
                        .requires(ModItems.STAFF,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(ModItems.STAFF), has(ModItems.STAFF))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.DI_STAFF, 1)
                        .requires(ModItems.STAFF,1)
                        .requires(Items.DIAMOND,1)
                        .unlockedBy(getHasName(ModItems.STAFF), has(ModItems.STAFF))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.END_STAFF, 1)
                        .requires(ModItems.STAFF,1)
                        .requires(Items.END_CRYSTAL,1)
                        .unlockedBy(getHasName(ModItems.STAFF), has(ModItems.STAFF))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.COPPER_POWDER, 2)
                        .requires(Items.COPPER_NUGGET,1)
                        .unlockedBy(getHasName(Items.COPPER_NUGGET), has(Items.COPPER_NUGGET))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.IRON_POWDER, 2)
                        .requires(Items.IRON_NUGGET,1)
                        .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.UNSTABLE_POWDER, 3)
                        .requires(ModItems.COPPER_POWDER,1)
                        .requires(ModItems.IRON_POWDER,1)
                        .requires(Items.REDSTONE,1)
                        .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.NETHER_POWDER, 2)
                        .requires(ModItems.UNSTABLE_POWDER,1)
                        .requires(Blocks.NETHERRACK,1)
                        .unlockedBy(getHasName(ModItems.UNSTABLE_POWDER), has(ModItems.UNSTABLE_POWDER))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.LAVA_POWDER, 2)
                        .requires(ModItems.UNSTABLE_POWDER,1)
                        .requires(Blocks.MAGMA_BLOCK,1)
                        .unlockedBy(getHasName(ModItems.UNSTABLE_POWDER), has(ModItems.UNSTABLE_POWDER))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.WATER_POWDER, 2)
                        .requires(ModItems.UNSTABLE_POWDER,1)
                        .requires(Blocks.ICE,1)
                        .unlockedBy(getHasName(ModItems.UNSTABLE_POWDER), has(ModItems.UNSTABLE_POWDER))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.STONE_POWDER, 2)
                        .requires(ModItems.UNSTABLE_POWDER,1)
                        .requires(Blocks.STONE,1)
                        .unlockedBy(getHasName(ModItems.UNSTABLE_POWDER), has(ModItems.UNSTABLE_POWDER))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.DIRT_POWDER, 2)
                        .requires(ModItems.UNSTABLE_POWDER,1)
                        .requires(Blocks.STONE,1)
                        .unlockedBy(getHasName(ModItems.UNSTABLE_POWDER), has(ModItems.UNSTABLE_POWDER))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.SCULK_POWDER, 2)
                        .requires(Items.CALCITE,1)
                        .requires(Items.REDSTONE,1)
                        .unlockedBy(getHasName(Items.CALCITE), has(Items.CALCITE))
                        .group("potions").save(output);

                shapeless(RecipeCategory.MISC, ModItems.FLOUR, 2)
                        .requires(Items.WHEAT,1)
                        .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                        .group("food").save(output);
                HolderLookup.Provider registries = registriesFuture.join();
                Holder<Potion> waterPotion = registries
                        .lookupOrThrow(Registries.POTION)
                        .getOrThrow(Potions.WATER.unwrapKey().orElseThrow());

                shapeless(RecipeCategory.MISC, ModItems.WAFFLE_MIX, 2)
                        .requires(ModItems.FLOUR,2)
                        .requires(Items.EGG,1)
                        .unlockedBy(getHasName(ModItems.FLOUR), has(ModItems.FLOUR))
                        .group("food").save(output);

                // Tools -------------------------------------------
                shapeless(RecipeCategory.MISC, ModItems.STICKYNOTE, 1)
                        .requires(Items.PAPER,1)
                        .requires(ModItems.GLUE,1)
                        .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                        .group("items").save(output);

                shapeless(RecipeCategory.MISC, ModItems.WRAPPING_PAPER, 1)
                        .requires(Items.PAPER,4)
                        .requires(ModItems.GLUE,1)
                        .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                        .group("items").save(output);

                shapeless(RecipeCategory.MISC, ModItems.GUIDE_STONE, 1)
                        .requires(Items.EMERALD,2)
                        .requires(Items.COMPASS,1)
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                        .group("items").save(output);

                shapeless(RecipeCategory.MISC, ModItems.WRENCH, 1)
                        .requires(Items.IRON_INGOT,2)
                        .requires(Items.COPPER_NUGGET,1)
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                        .group("items").save(output);

                shapeless(RecipeCategory.MISC, ModItems.BANDAGE, 1)
                        .requires(Items.WOOL.white(),2)
                        .requires(ModItems.GLUE,1)
                        .unlockedBy(getHasName(Items.WOOL.white()), has(Items.WOOL.white()))
                        .group("items").save(output);

                shapeless(RecipeCategory.MISC, ModItems.TRAVELING_STONE, 1)
                        .requires(Items.ENDER_PEARL,1)
                        .requires(Items.DIAMOND,1)
                        .requires(Items.GOLD_INGOT,1)
                        .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
                        .group("items").save(output);

                shapeless(RecipeCategory.MISC, ModItems.TELEPORTER_ARROW, 1)
                        .requires(ModItems.TRAVELING_STONE,1)
                        .requires(Items.ARROW,1)
                        .unlockedBy(getHasName(ModItems.TRAVELING_STONE), has(ModItems.TRAVELING_STONE))
                        .group("items").save(output);

                shapeless(RecipeCategory.MISC, ModItems.CLAY_PLATE, 2)
                        .requires(Items.CLAY_BALL,1)
                        .requires(Items.DYE.white(),1)
                        .unlockedBy(getHasName(ModItems.UNSTABLE_POWDER), has(ModItems.UNSTABLE_POWDER))
                        .group("items").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MIXER, 1)
                        .requires(Blocks.FURNACE,1)
                        .requires(Items.BUCKET,1)
                        .unlockedBy(getHasName(Blocks.FURNACE), has(Blocks.FURNACE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.LAVA_CORE, 1)
                        .requires(ModBlocks.MIXER,1)
                        .requires(Blocks.OBSIDIAN,1)
                        .unlockedBy(getHasName(ModBlocks.MIXER), has(ModBlocks.MIXER))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.DRYER, 1)
                        .requires(ModBlocks.MIXER,1)
                        .requires(Blocks.MAGMA_BLOCK,1)
                        .unlockedBy(getHasName(ModBlocks.MIXER), has(ModBlocks.MIXER))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.BRICKMAKER, 1)
                        .requires(ModBlocks.MIXER,1)
                        .requires(Blocks.OAK_PLANKS,1)
                        .unlockedBy(getHasName(ModBlocks.MIXER), has(ModBlocks.MIXER))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.SORTER, 1)
                        .requires(ModBlocks.MIXER,1)
                        .requires(Items.ENDER_PEARL,1)
                        .requires(Blocks.COMPARATOR,1)
                        .unlockedBy(getHasName(ModBlocks.MIXER), has(ModBlocks.MIXER))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.BREWER, 1)
                        .requires(ModBlocks.STORESHELF,1)
                        .requires(Blocks.COMPARATOR,1)
                        .unlockedBy(getHasName(ModBlocks.MIXER), has(ModBlocks.MIXER))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.CENSUS, 1)
                        .requires(Blocks.POLISHED_DIORITE,1)
                        .requires(Blocks.CAMPFIRE,1)
                        .unlockedBy(getHasName(Blocks.POLISHED_DIORITE), has(Blocks.POLISHED_DIORITE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.HEARTH, 1)
                        .requires(Blocks.BRICKS,1)
                        .requires(Blocks.CAMPFIRE,1)
                        .unlockedBy(getHasName(Blocks.BRICKS), has(Blocks.BRICKS))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.PEDESTAL, 1)
                        .requires(Blocks.POLISHED_DIORITE,1)
                        .requires(Items.LAPIS_LAZULI,1)
                        .unlockedBy(getHasName(Blocks.POLISHED_DIORITE), has(Blocks.POLISHED_DIORITE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.ALTER, 1)
                        .requires(ModBlocks.PEDESTAL,1)
                        .requires(Items.GOLD_INGOT,1)
                        .unlockedBy(getHasName(ModBlocks.PEDESTAL), has(ModBlocks.PEDESTAL))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.CLOCK, 1)
                        .requires(Blocks.OAK_PLANKS,1)
                        .requires(Items.QUARTZ,1)
                        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.ENDERLAMP, 1)
                        .requires(Items.ENDER_PEARL,1)
                        .requires(Items.QUARTZ,1)
                        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.ATM, 1)
                        .requires(Items.POLISHED_DIORITE,1)
                        .requires(ModItems.FLORIN,1)
                        .unlockedBy(getHasName(ModItems.FLORIN), has(ModItems.FLORIN))
                        .group("tools").save(output);


                shapeless(RecipeCategory.MISC, ModBlocks.HOOK, 1)
                        .requires(Items.IRON_INGOT,1)
                        .requires(ModBlocks.PLATE,1)
                        .unlockedBy(getHasName(ModBlocks.PLATE), has(ModBlocks.PLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.STORESHELF, 1)
                        .requires(Blocks.STONE_SLAB,3)
                        .unlockedBy(getHasName(Blocks.STONE_SLAB), has(Blocks.STONE_SLAB))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.LOCKING_DOOR, 1)
                        .requires(Blocks.OAK_DOOR,1)
                        .requires(Items.GOLD_INGOT,1)
                        .unlockedBy(getHasName(Blocks.OAK_DOOR), has(Blocks.OAK_DOOR))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.OSC, 1)
                        .requires(Items.REDSTONE,1)
                        .requires(Items.QUARTZ,1)
                        .requires(Items.COPPER_INGOT,1)
                        .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.RECIEVER, 1)
                        .requires(Blocks.LIGHTNING_ROD.weathering().unaffected(),1)
                        .requires(ModBlocks.OSC,1)
                        .unlockedBy(getHasName(ModBlocks.OSC), has(ModBlocks.OSC))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.TELEPORTER, 1)
                        .requires(ModBlocks.RECIEVER,1)
                        .requires(ModItems.TRAVELING_STONE,1)
                        .unlockedBy(getHasName(ModBlocks.RECIEVER), has(ModBlocks.RECIEVER))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.FUELER, 1)
                        .requires(ModBlocks.RECIEVER,1)
                        .requires(Blocks.CHEST,1)
                        .unlockedBy(getHasName(ModBlocks.RECIEVER), has(ModBlocks.RECIEVER))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.HELPER, 1)
                        .requires(ModBlocks.OSC,1)
                        .requires(Blocks.COMPARATOR,1)
                        .unlockedBy(getHasName(ModBlocks.OSC), has(ModBlocks.OSC))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.STAIN_GLASS, 1)
                        .requires(Blocks.GLASS,1)
                        .requires(Items.PAINTING,1)
                        .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.HEALING, 1)
                        .requires(Blocks.POLISHED_DIORITE,1)
                        .requires(Items.LAPIS_LAZULI,1)
                        .requires(Items.GOLDEN_APPLE,1)
                        .unlockedBy(getHasName(Items.GOLDEN_APPLE), has(Items.GOLDEN_APPLE))
                        .group("tools").save(output);


                SimpleCookingRecipeBuilder.blasting(
                                Ingredient.of(ModItems.CLAY_PLATE), // 1. Input ingredient
                                RecipeCategory.MISC,                    // 2. Crafting recipe category
                                CookingBookCategory.MISC,               // 3. Furnace UI tab (The 6th arg)
                                ModBlocks.PLATE,                  // 4. Output item
                                0.7F,                                   // 5. Experience dropped
                                100                                     // 6. Cooking time in ticks
                        )
                        .unlockedBy("has_raw_ore", has(ModItems.CLAY_PLATE)) // Unlocks recipe in player's recipe book
                        .save(output, "paulsmod:plate_from_clay_plate");     // Unique recipe ID


                // Materials

                shapeless(RecipeCategory.MISC, ModBlocks.RIVER_ROCK_MORTAR, 3)
                        .requires(ModItems.RIVER_ROCKS,2)
                        .requires(Blocks.CONCRETE.white(),1)
                        .unlockedBy(getHasName(ModItems.RIVER_ROCKS), has(ModItems.RIVER_ROCKS))
                        .group("tools").save(output);

                SimpleCookingRecipeBuilder.smoking(
                                Ingredient.of(Items.ROTTEN_FLESH),
                                RecipeCategory.FOOD,
                                Items.LEATHER,
                                0.35F,
                                100
                        )
                        .unlockedBy("has_raw_meat", has(Items.ROTTEN_FLESH)) // Requirement to unlock recipe in book
                        .save(output, "paulsmod:leather_from_rotten_flesh");

                SimpleCookingRecipeBuilder.smoking(
                                Ingredient.of(ModItems.WAFFLE_MIX),
                                RecipeCategory.FOOD,
                                ModItems.WAFFLES,
                                0.35F,
                                100
                        )
                        .unlockedBy("has_flour", has(ModItems.FLOUR)) // Requirement to unlock recipe in book
                        .save(output, "paulsmod:waffles_from_waffle_mix");

                SimpleCookingRecipeBuilder.smoking(
                                Ingredient.of(Items.EGG),
                                RecipeCategory.FOOD,
                                ModItems.SCAMBLED_EGGS,
                                0.35F,
                                100
                        )
                        .unlockedBy("has_egg", has(Items.EGG)) // Requirement to unlock recipe in book
                        .save(output, "paulsmod:scrambled_eggs_from_egg");

                // Decorative blocks

                shapeless(RecipeCategory.MISC, ModBlocks.SPAWNER, 1)
                        .requires(Items.COPPER_GRATE.waxed().unaffected(), 1)
                        .requires(ModItems.OSC,1)
                        .requires(Items.LAPIS_BLOCK,1)
                        .unlockedBy(getHasName(Blocks.LAPIS_BLOCK), has(Blocks.LAPIS_BLOCK))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.STATUE, 1)
                        .requires(Blocks.POLISHED_DIORITE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.POLISHED_ANDESITE), has(Blocks.POLISHED_ANDESITE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.BIG_DI_PILLAR, 1)
                        .requires(Blocks.POLISHED_DIORITE,1)
                        .unlockedBy(getHasName(Blocks.POLISHED_DIORITE), has(Blocks.POLISHED_DIORITE))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.PILLAR1, 1)
                        .requires(ModBlocks.BIG_DI_PILLAR,1)
                        .unlockedBy(getHasName(ModBlocks.BIG_DI_PILLAR), has(ModBlocks.BIG_DI_PILLAR))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.PILLAR2, 1)
                        .requires(ModBlocks.PILLAR1,1)
                        .unlockedBy(getHasName(ModBlocks.PILLAR1), has(ModBlocks.PILLAR1))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.PILLAR3, 1)
                        .requires(ModBlocks.PILLAR2,1)
                        .unlockedBy(getHasName(ModBlocks.PILLAR2), has(ModBlocks.PILLAR2))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.BIG_DS_PILLAR, 1)
                        .requires(Blocks.POLISHED_DEEPSLATE)
                        .unlockedBy(getHasName(Blocks.POLISHED_DEEPSLATE), has(Blocks.POLISHED_DEEPSLATE))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.BIG_AN_PILLAR, 1)
                        .requires(Blocks.POLISHED_ANDESITE)
                        .unlockedBy(getHasName(Blocks.POLISHED_ANDESITE), has(Blocks.POLISHED_ANDESITE))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.ANDESITE_PILLAR1, 1)
                        .requires(ModBlocks.BIG_AN_PILLAR)
                        .unlockedBy(getHasName(ModBlocks.BIG_AN_PILLAR), has(ModBlocks.BIG_AN_PILLAR))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.ANDESITE_PILLAR2, 1)
                        .requires(ModBlocks.ANDESITE_PILLAR1)
                        .unlockedBy(getHasName(ModBlocks.ANDESITE_PILLAR1), has(ModBlocks.ANDESITE_PILLAR1))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.ANDESITE_PILLAR3, 1)
                        .requires(ModBlocks.ANDESITE_PILLAR2)
                        .unlockedBy(getHasName(ModBlocks.ANDESITE_PILLAR2), has(ModBlocks.ANDESITE_PILLAR2))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.BIG_GR_PILLAR, 1)
                        .requires(Blocks.POLISHED_GRANITE)
                        .unlockedBy(getHasName(Blocks.POLISHED_GRANITE), has(Blocks.POLISHED_GRANITE))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.GRANITE_PILLAR1, 1)
                        .requires(ModBlocks.BIG_GR_PILLAR)
                        .unlockedBy(getHasName(ModBlocks.BIG_GR_PILLAR), has(ModBlocks.BIG_GR_PILLAR))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.GRANITE_PILLAR2, 1)
                        .requires(ModBlocks.GRANITE_PILLAR1)
                        .unlockedBy(getHasName(ModBlocks.GRANITE_PILLAR1), has(ModBlocks.GRANITE_PILLAR1))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.GRANITE_PILLAR3, 1)
                        .requires(ModBlocks.GRANITE_PILLAR2)
                        .unlockedBy(getHasName(ModBlocks.GRANITE_PILLAR2), has(ModBlocks.GRANITE_PILLAR2))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.DEEPSLATE_PILLAR1, 1)
                        .requires(ModBlocks.BIG_DS_PILLAR)
                        .unlockedBy(getHasName(ModBlocks.BIG_DS_PILLAR), has(ModBlocks.BIG_DS_PILLAR))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.DEEPSLATE_PILLAR2, 1)
                        .requires(ModBlocks.DEEPSLATE_PILLAR1)
                        .unlockedBy(getHasName(ModBlocks.DEEPSLATE_PILLAR1), has(ModBlocks.DEEPSLATE_PILLAR1))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.DEEPSLATE_PILLAR3, 1)
                        .requires(ModBlocks.DEEPSLATE_PILLAR2)
                        .unlockedBy(getHasName(ModBlocks.DEEPSLATE_PILLAR2), has(ModBlocks.DEEPSLATE_PILLAR2))
                        .group("blocks").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.ROUND_FRAMED_WINDOW, 1)
                        .requires(Blocks.GLASS,1)
                        .requires(Blocks.OAK_PLANKS,1)
                        .unlockedBy(getHasName(Blocks.GLASS), has(Blocks.GLASS))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.FRAMED_WINDOW, 1)
                        .requires(Blocks.GLASS,1)
                        .requires(Blocks.OAK_PLANKS,1)
                        .unlockedBy(getHasName(Blocks.GLASS), has(Blocks.GLASS))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.BARRIER, 1)
                        .requires(Blocks.STONE_BRICKS,1)
                        .requires(Blocks.MAGMA_BLOCK,1)
                        .unlockedBy(getHasName(Blocks.MAGMA_BLOCK), has(Blocks.MAGMA_BLOCK))
                        .group("tools").save(output);

                shaped(RecipeCategory.MISC, ModBlocks.LATTICE_ARCH)
                        .pattern("RRR")
                        .pattern("RR ")
                        .pattern("R  ")
                        .define('R', Items.STICK)
                        .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                        .group("blocks")
                        .save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_1_1, 1)
                        .requires(Blocks.COBBLED_DEEPSLATE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_1_2, 1)
                        .requires(Blocks.COBBLED_DEEPSLATE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_1_3, 1)
                        .requires(Blocks.COBBLED_DEEPSLATE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_1_4, 1)
                        .requires(Blocks.COBBLED_DEEPSLATE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_2_1, 1)
                        .requires(Blocks.COBBLED_DEEPSLATE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_2_2, 1)
                        .requires(Blocks.COBBLED_DEEPSLATE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_2_3, 1)
                        .requires(Blocks.COBBLED_DEEPSLATE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_2_4, 1)
                        .requires(Blocks.COBBLED_DEEPSLATE,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_3_1, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_3_2, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_3_3, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_3_4, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_4_1, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_4_2, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_4_3, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_4_4, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_5_1, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_5_2, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_5_3, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.MOSAIC_5_4, 1)
                        .requires(Blocks.STRIPPED_OAK_LOG,1)
                        .requires(Items.EMERALD,1)
                        .unlockedBy(getHasName(Blocks.STRIPPED_OAK_LOG), has(Blocks.STRIPPED_OAK_LOG))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModBlocks.CAMO, 1)
                        .requires(Blocks.SCULK,1)
                        .requires(Items.LAPIS_LAZULI,1)
                        .requires(Items.GLASS,1)
                        .unlockedBy(getHasName(Blocks.SCULK), has(Blocks.SCULK))
                        .group("tools").save(output);

                shapeless(RecipeCategory.MISC, ModItems.GLUE, 1)
                        .requires(Items.EGG,1)
                        .requires(Items.BONE_MEAL,1)
                        .unlockedBy(getHasName(Items.EGG), has(Items.EGG))
                        .group("tools").save(output);

                /*

                List<ItemLike> FLUORITE_SMELTABLES = List.of(ModItems.RAW_FLUORITE, ModBlocks.FLUORITE_ORE, ModBlocks.FLUORITE_DEEPSLATE_ORE,
                        ModBlocks.FLUORITE_NETHER_ORE, ModBlocks.FLUORITE_END_ORE);

                oreSmelting(FLUORITE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.FLUORITE, 0.25f, 200, "fluorite");
                oreBlasting(FLUORITE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.FLUORITE, 0.25f, 100, "fluorite");

                nineBlockStorageRecipes(RecipeCategory.MISC, ModItems.FLUORITE, RecipeCategory.BUILDING_BLOCKS, ModBlocks.FLUORITE_BLOCK);

                shaped(RecipeCategory.MISC, ModBlocks.RAW_FLUORITE_BLOCK)
                        .pattern("RRR")
                        .pattern("RRR")
                        .pattern("RRR")
                        .define('R', ModItems.RAW_FLUORITE)
                        .unlockedBy(getHasName(ModItems.RAW_FLUORITE), has(ModItems.RAW_FLUORITE))
                        .group("fluorite")
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.RAW_FLUORITE, 9)
                        .requires(ModBlocks.RAW_FLUORITE_BLOCK)
                        .unlockedBy(getHasName(ModBlocks.RAW_FLUORITE_BLOCK), has(ModBlocks.RAW_FLUORITE_BLOCK))
                        .group("fluorite")
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.RAW_FLUORITE, 4)
                        .requires(ModItems.FLUORITE)
                        .requires(Items.STICK)
                        .unlockedBy(getHasName(ModItems.FLUORITE), has(ModItems.FLUORITE))
                        .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                        .group("fluorite")
                        .save(output, "raw_fluorite_from_fluorite_and_stick");

                 */

            }
        };
    }

    @Override
    public String getName() {
        return "PaulsMod Recipes";
    }
}
