package de.dercoder.inventoryframework.inventory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class InventoryRegistry {
  private final Map<UUID, SimpleInventory> inventories;

  private InventoryRegistry(Map<UUID, SimpleInventory> inventories) {
    this.inventories = inventories;
  }

  public void register(SimpleInventory inventory) {
    Preconditions.checkNotNull(inventory);
    inventories.put(inventory.id(), inventory);
  }

  public void unregister(SimpleInventory inventory) {
    Preconditions.checkNotNull(inventory);
    inventories.remove(inventory.id());
  }

  public Optional<SimpleInventory> findById(UUID id) {
    Preconditions.checkNotNull(id);
    return Optional.ofNullable(inventories.get(id));
  }

  public Optional<SimpleInventory> findOneByName(String name) {
    Preconditions.checkNotNull(name);
    return inventories.values()
      .stream()
      .filter(inventory -> inventory.name().equalsIgnoreCase(name))
      .findFirst();
  }

  public List<SimpleInventory> findAll() {
    return List.copyOf(inventories.values());
  }

  public static InventoryRegistry empty() {
    return new InventoryRegistry(Maps.newHashMap());
  }
}
