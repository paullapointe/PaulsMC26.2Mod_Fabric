package net.lapointep.paulsmod.menu;
import net.lapointep.paulsmod.PaulsMod;
import net.lapointep.paulsmod.block.entity.StoreShelfBlockEntity;
import net.lapointep.paulsmod.item.ModItems;
import net.lapointep.paulsmod.util.PaulsCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StoreShelfMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData data;

    public StoreShelfMenu(int containerId, Inventory inv, BlockPos blockPos) {
        this(containerId, inv, inv.player.level().getBlockEntity(blockPos), new SimpleContainerData(2));
    }

    public StoreShelfMenu(int containerId, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(net.lapointep.paulsmod.menu.ModMenuTypes.STORESHELF_MENU, containerId);
        this.inventory = ((Container) blockEntity);
        this.data = data;

        this.addSlot(new Slot(inventory, 0, 60, 11) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                handleRemoval(player);
            }

            @Override
            public boolean mayPickup(Player player) {
                return canPlayerPickUp(player);
            }
        });
        this.addSlot(new Slot(inventory, 1, 80, 11) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                handleRemoval(player);
            }

            @Override
            public boolean mayPickup(Player player) {
                return canPlayerPickUp(player);
            }
        });
        this.addSlot(new Slot(inventory, 2, 100, 11) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                handleRemoval(player);
            }

            @Override
            public boolean mayPickup(Player player) {
                return canPlayerPickUp(player);
            }
        });
        this.addSlot(new Slot(inventory, 3, 60, 59) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                handleRemoval(player);
            }

            @Override
            public boolean mayPickup(Player player) {
                return canPlayerPickUp(player);
            }
        });
        this.addSlot(new Slot(inventory, 4, 80, 59) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                handleRemoval(player);
            }

            @Override
            public boolean mayPickup(Player player) {
                return canPlayerPickUp(player);
            }
        });
        this.addSlot(new Slot(inventory, 5, 100, 59) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                handleRemoval(player);
            }

            @Override
            public boolean mayPickup(Player player) {
                return canPlayerPickUp(player);
            }
        });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    private void handleRemoval(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        if (inventory instanceof StoreShelfBlockEntity storeShelfBlockEntity) {
            String playerName = player.getName().getString();
            PaulsMod.LOGGER.info("2:Player {} removed an item from StoreShelfBlock", playerName);
            String ownerName = storeShelfBlockEntity.getPlacerName();
            if (playerName.equals(ownerName)) {
                // Placer removed an item
                PaulsMod.LOGGER.info("3: Player {} removed an item from StoreShelfBlock owned by themself", playerName);
            } else {
                PaulsMod.LOGGER.info("4: Player {} removed an item from StoreShelfBlock owned by someone else", playerName);
                int cost = storeShelfBlockEntity.getCost();
                if (cost > 0) {
                    if (!PaulsCommon.hasItemQuantity(player, ModItems.FLORIN, cost)) {
                        PaulsMod.LOGGER.info("5: Player {} does not have enough Florins to buy this item! Cost: " + cost, playerName);
                        return;
                    }
                    PaulsCommon.consumeItemQuantity(player, ModItems.FLORIN, cost);
                    PaulsMod.Deposit(ownerName, cost);
                }
            }
        }
    }

    private boolean canPlayerPickUp(Player player) {
        if (inventory instanceof StoreShelfBlockEntity storeShelfBlockEntity) {
            String playerName = player.getName().getString();
            String ownerName = storeShelfBlockEntity.getPlacerName();
            if (playerName.equals(ownerName)) {
                return true;
            }
            int cost = storeShelfBlockEntity.getCost();
            if (cost > 0) {
                boolean hasEnough = PaulsCommon.hasItemQuantity(player, ModItems.FLORIN, cost);
                if (!hasEnough) {
                    if (!player.level().isClientSide()) {
                        player.sendSystemMessage(net.minecraft.network.chat.Component.literal("You do not have enough Florins to buy this item! Cost: " + cost));
                    }
                    return false;
                }
            }
        }
        return true;
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 5 = TileInventory slots, which map to our TileEntity slot numbers 0 - 5
    //  6 - 32 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  33 - 41 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_SLOT_COUNT = 6;
    private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int HOTBAR_FIRST_SLOT_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int HOTBAR_SLOT_COUNT = 9;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < TE_INVENTORY_SLOT_COUNT) {
            // From TE to Player Inventory
            if (!canPlayerPickUp(playerIn)) {
                return ItemStack.EMPTY;
            }
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_SLOT_COUNT, TE_INVENTORY_SLOT_COUNT + 36, true)) {
                return ItemStack.EMPTY;
            }
            handleRemoval(playerIn);
        } else {
            // From Player Inventory to TE
            if (!moveItemStackTo(sourceStack, 0, TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.inventory.stillValid(pPlayer);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
