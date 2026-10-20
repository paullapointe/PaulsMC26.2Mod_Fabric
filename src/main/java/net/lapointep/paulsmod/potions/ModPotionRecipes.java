package net.lapointep.paulsmod.potions;

import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.lapointep.paulsmod.item.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

public class ModPotionRecipes {
    public static void registerPotionRecipes() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ModItems.NETHER_POWDER), ModPotions.NETHERRACK_POTION);
            builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ModItems.LAVA_POWDER), ModPotions.LAVA_POTION);
            builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ModItems.WATER_POWDER), ModPotions.WATER_POTION);
            builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ModItems.STONE_POWDER), ModPotions.STONE_POTION);
            builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ModItems.DIRT_POWDER), ModPotions.DIRT_POTION);

            builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ModItems.SCULK_POWDER), ModPotions.SCULKICIDE_POTION);

            builder.registerPotionRecipe(Potions.WATER, Ingredient.of(Items.SUGAR_CANE), ModPotions.SUGAR_WATER);
            builder.registerPotionRecipe(ModPotions.SUGAR_WATER, Ingredient.of(Items.LAPIS_LAZULI), ModPotions.LAPIS_COLA);
        });
    }
}