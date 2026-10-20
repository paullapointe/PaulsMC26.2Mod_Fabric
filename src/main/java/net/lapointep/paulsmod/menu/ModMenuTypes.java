package net.lapointep.paulsmod.menu;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {

    public static final MenuType<SorterMenu> SORTER_MENU =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "sorter_menu"),
                    new ExtendedMenuType<>(SorterMenu::new, BlockPos.STREAM_CODEC));
    public static final MenuType<BrewerMenu> BREWER_MENU =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "brewer_menu"),
                    new ExtendedMenuType<>(BrewerMenu::new, BlockPos.STREAM_CODEC));
    public static final MenuType<StoreShelfMenu> STORESHELF_MENU =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "storeshelf_menu"),
                    new ExtendedMenuType<>(StoreShelfMenu::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ProcessorMenu> PROCESSOR_MENU =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "processor_menu"),
                    new ExtendedMenuType<>(ProcessorMenu::new, BlockPos.STREAM_CODEC));

    public static final MenuType<ATMMenu> ATM_MENU =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, "atm_menu"),
                    new ExtendedMenuType<>(ATMMenu::new, BlockPos.STREAM_CODEC));

    public static void registerMenus() {
        PaulsMod.LOGGER.info("Registering Mod Menus for " + PaulsMod.MOD_ID);
    }
}
