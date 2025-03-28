package net.educanet.oneslotchest.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OneSlotChestMenu extends AbstractContainerMenu {
    private final Container container;

    // Constructor for server-side
    public OneSlotChestMenu(int id, Inventory playerInv, Container container) {
        super(ModMenus.ONE_SLOT_CHEST_MENU.get(), id);
        this.container = container;

        // Add slot (x, y position)
        this.addSlot(new Slot(container, 0, 80, 18));

        // Player inventory
        addPlayerInventory(playerInv);
    }

    // Constructor for client-side (used by IForgeMenuType)
    public OneSlotChestMenu(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(1));
    }

    private void addPlayerInventory(Inventory playerInv) {
        // Standard player inventory layout
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 49 + i * 18)); // ORIGINAL 84, 102, 120, EDITED 49, 67, 85
            }
        }
        // Hotbar
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 107));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    // Required implementation of quickMoveStack
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();

            if (index < 1) {
                // Move from container to player inventory
                if (!this.moveItemStackTo(slotItem, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from player inventory to container
                if (!this.moveItemStackTo(slotItem, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public Container getContainer() {
        return this.container;
    }
}
