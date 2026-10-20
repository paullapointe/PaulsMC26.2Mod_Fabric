package net.lapointep.paulsmod.enchantments;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;

public class EnchantmentUtils {

    /**
     * Retrieves all enchantments and their levels from an ItemStack.
     */
    public static ArrayList<Pair<Enchantment, Integer>> getEnchantmentsOnItem(ItemStack stack) {
        ArrayList<Pair<Enchantment, Integer>> enchantmentList = new ArrayList<>();

        // EnchantmentHelper returns net.minecraft.world.item.enchantment.ItemEnchantments
        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        // Iterate through all entries on the item
        for (var entry : enchantments.entrySet()) {
            Holder<Enchantment> enchantmentHolder = entry.getKey();
            Enchantment enchantment = enchantmentHolder.value();
            int level = entry.getIntValue();
            enchantmentList.add(new Pair<Enchantment, Integer>(enchantment, level));
        }

        return enchantmentList;
    }
}
