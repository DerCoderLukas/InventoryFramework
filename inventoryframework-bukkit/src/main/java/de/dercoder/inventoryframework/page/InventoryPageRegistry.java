package de.dercoder.inventoryframework.page;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public final class InventoryPageRegistry {
  private final List<InventoryPage> pages;

  private InventoryPageRegistry(List<InventoryPage> pages) {
    this.pages = pages;
  }

  public void register(InventoryPage page) {
    Preconditions.checkNotNull(page);
    pages.add(page);
  }

  public List<InventoryPage> findAll() {
    return List.copyOf(pages);
  }

  public static InventoryPageRegistry empty() {
    return new InventoryPageRegistry(Lists.newArrayList());
  }
}
