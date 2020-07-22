package de.dercoder.inventoryframework.inventory;

import org.bukkit.event.inventory.InventoryCloseEvent;

public interface InventoryCloseBehaviour {
  void close(InventoryCloseEvent closeEvent);
}
