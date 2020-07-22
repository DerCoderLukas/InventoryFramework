package de.dercoder.inventoryframework.page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.dercoder.inventoryframework.item.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryPage {
  private final int size;
  private final String name;
  private final Map<Integer, InventoryItem> items;
  private final ItemStack background;
  private Inventory inventory;

  protected InventoryPage(
    int size,
    String name,
    Map<Integer, InventoryItem> items,
    ItemStack background
  ) {
    this.items = items;
    this.size = size;
    this.name = name;
    this.background = background;
  }

  public Inventory asInventory() {
    return inventory;
  }

  private void build() {
    inventory = Bukkit.createInventory(null, size, name);
    setBackground(inventory);
    setItems(inventory);
  }

  private void setBackground(Inventory inventory) {
    for ( int i = 0; i < inventory.getSize(); i++ ) {
      inventory.setItem(i, background);
    }
  }

  private void setItems(Inventory inventory) {
    for ( Map.Entry<Integer, InventoryItem> itemEntry : items.entrySet() ) {
      inventory.setItem(itemEntry.getKey(), itemEntry.getValue().item());
    }
  }

  public Optional<InventoryItem> findItem(ItemStack itemStack) {
    Preconditions.checkNotNull(itemStack);
    if (itemStack.equals(background)) {
      return Optional.empty();
    }
    return items().stream()
      .filter(item -> item.item().equals(itemStack))
      .findFirst();
  }

  public InventoryPage copy() {
    var inventoryPage = new InventoryPage(size, name, items, background);
    inventoryPage.build();
    return inventoryPage;
  }

  public int size() {
    return size;
  }

  public String name() {
    return name;
  }

  public List<InventoryItem> items() {
    return List.copyOf(items.values());
  }

  public Map<Integer, InventoryItem> itemMap() {
    return Map.copyOf(items);
  }

  public ItemStack background() {
    return background;
  }

  public static Builder newBuilder() {
    return new Builder(9, Maps.newHashMap());
  }

  public static final class Builder {
    private int size;
    private String name;
    private Map<Integer, InventoryItem> items;
    private ItemStack background;

    private Builder(int size, Map<Integer, InventoryItem> items) {
      this.size = size;
      this.items = items;
    }

    public Builder withSize(int size) {
      Preconditions.checkNotNull(size);
      this.size = size;
      return this;
    }

    public Builder withName(String name) {
      Preconditions.checkNotNull(name);
      this.name = name;
      return this;
    }

    public Builder withItem(int position, InventoryItem item) {
      Preconditions.checkNotNull(item);
      items.put(position, item);
      return this;
    }

    public Builder withBackground(ItemStack background) {
      Preconditions.checkNotNull(background);
      this.background = background;
      return this;
    }

    public InventoryPage create() {
      Preconditions.checkNotNull(size);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(items);
      Preconditions.checkNotNull(background);
      var inventoryPage = new InventoryPage(size, name, items, background);
      inventoryPage.build();
      return inventoryPage;
    }
  }
}
