package de.dercoder.inventoryframework.item;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;

import de.dercoder.inventoryframework.inventory.InventoryRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public final class InventoryItemBehaviourTrigger implements Listener {
  private final InventoryRegistry inventoryRegistry;

  @Inject
  private InventoryItemBehaviourTrigger(InventoryRegistry inventoryRegistry) {
    this.inventoryRegistry = inventoryRegistry;
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent inventoryClick) {
    Preconditions.checkNotNull(inventoryClick);
    var item = inventoryClick.getCurrentItem();
    if (item == null) {
      return;
    }
    if (checkInventoryClick(inventoryClick, item)) {
      inventoryClick.setCancelled(true);
    }
  }

  private boolean checkInventoryClick(
    InventoryClickEvent inventoryClick, ItemStack item
  ) {
    for ( var inventory : inventoryRegistry.findAll() ) {
      if (inventory.pageHistory().size() == 0) {
        continue;
      }
      var activePage = inventory.activePage();
      if (!inventoryClick.getInventory().equals(activePage.asInventory())) {
        continue;
      }
      activePage.findItem(item)
        .ifPresent(inventoryItem -> inventoryItem.triggerItemClick(
          inventoryClick));
      return true;
    }
    return false;
  }
}
