package de.dercoder.inventoryframework.item;

import com.google.common.base.Preconditions;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public final class InventoryItem {
  private final ItemStack item;
  private final InventoryItemBehaviour itemBehaviour;

  private InventoryItem(ItemStack item, InventoryItemBehaviour itemBehaviour) {
    this.item = item;
    this.itemBehaviour = itemBehaviour;
  }

  public void triggerItemClick(InventoryClickEvent inventoryClick) {
    Preconditions.checkNotNull(inventoryClick);
    itemBehaviour.click(inventoryClick);
  }

  public ItemStack item() {
    return item.clone();
  }

  public static InventoryItem create(ItemStack item) {
    Preconditions.checkNotNull(item);
    return new InventoryItem(item, click -> {});
  }

  public static InventoryItem withBehaviour(
    ItemStack item, InventoryItemBehaviour itemBehaviour
  ) {
    Preconditions.checkNotNull(item);
    Preconditions.checkNotNull(itemBehaviour);
    return new InventoryItem(item, itemBehaviour);
  }
}
