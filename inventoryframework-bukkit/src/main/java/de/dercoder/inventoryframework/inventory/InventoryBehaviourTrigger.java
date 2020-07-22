package de.dercoder.inventoryframework.inventory;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public final class InventoryBehaviourTrigger implements Listener {
  private final InventoryRegistry inventoryRegistry;

  @Inject
  private InventoryBehaviourTrigger(InventoryRegistry inventoryRegistry) {
    this.inventoryRegistry = inventoryRegistry;
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent inventoryOpen) {
    Preconditions.checkNotNull(inventoryOpen);
    for ( var inventory : inventoryRegistry.findAll() ) {
      if (checkInventoryOpen(inventoryOpen, inventory)) {
        return;
      }
    }
  }

  private boolean checkInventoryOpen(
    InventoryOpenEvent inventoryOpen, SimpleInventory inventory
  ) {
    if (inventory.pageHistory().size() == 0) {
      return false;
    }
    var activePage = inventory.activePage();
    if (!inventoryOpen.getInventory().equals(activePage.asInventory())) {
      return false;
    }
    handleInventoryOpen(inventoryOpen, inventory);
    return true;
  }

  private void handleInventoryOpen(
    InventoryOpenEvent inventoryOpen, SimpleInventory inventory
  ) {
    if (!inventory.state().equals(SimpleInventory.InventoryState.CLOSED)) {
      return;
    }
    inventory.open(inventoryOpen);
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent inventoryClose) {
    Preconditions.checkNotNull(inventoryClose);
    for ( var inventory : inventoryRegistry.findAll() ) {
      if (checkInventoryClose(inventoryClose, inventory) || checkPageSwitch(inventoryClose,
        inventory
      )) {
        return;
      }
    }
  }

  private boolean checkInventoryClose(
    InventoryCloseEvent inventoryClose, SimpleInventory inventory
  ) {
    if (inventory.pageHistory().size() == 0) {
      return false;
    }
    var activePage = inventory.activePage();
    if (!inventoryClose.getInventory().equals(activePage.asInventory())) {
      return false;
    }
    inventory.close(inventoryClose);
    inventoryRegistry.unregister(inventory);
    return true;
  }

  private boolean checkPageSwitch(
    InventoryCloseEvent inventoryClose, SimpleInventory inventory
  ) {
    var pageHistorySize = inventory.pageHistory().size();
    if (pageHistorySize < 2) {
      return false;
    }
    var penultimatePage = inventory.pageHistory().get(pageHistorySize - 2);
    if (!inventoryClose.getInventory()
      .equals(penultimatePage.asInventory()) || !inventory.state()
      .equals(SimpleInventory.InventoryState.ON_SWITCH)) {
      return false;
    }
    inventory.switchState(SimpleInventory.InventoryState.OPEN);
    return true;
  }
}
