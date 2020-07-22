package de.dercoder.inventoryframework;

import java.util.UUID;

import com.google.common.collect.Iterables;

import de.dercoder.inventoryframework.inventory.InventoryRegistry;
import de.dercoder.inventoryframework.inventory.SimpleInventory;
import de.dercoder.inventoryframework.page.InventoryPage;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class SimpleInventoryTest {
  private InventoryRegistry inventoryRegistry;
  private SimpleInventory inventory;

  @BeforeEach
  void initialize() {
    inventoryRegistry = InventoryRegistry.empty();
    inventory = SimpleInventory.create(
      UUID.randomUUID(),
      "TestInventory",
      (inventoryOpen) -> {},
      (inventoryClose) -> {}
    );
    inventoryRegistry.register(inventory);
    createTestPages();
  }

  void createTestPages() {
    var firstTestPage = Mockito.mock(InventoryPage.class);
    inventory.pageRegistry().register(firstTestPage);
    var secondTestPage = Mockito.mock(InventoryPage.class);
    inventory.pageRegistry().register(secondTestPage);
    inventory.setActivePage(firstTestPage);
  }

  @Test
  void testInventoryRegistry() {
    var inventoryOptional = inventoryRegistry.findById(inventory.id());
    assertTrue(inventoryOptional.isPresent());
  }

  @Test
  void testInventoryOpen() {
    var inventoryOpen = Mockito.mock(InventoryOpenEvent.class);
    inventory.open(inventoryOpen);
    assertEquals(inventory.state(), SimpleInventory.InventoryState.OPEN);
  }

  @Test
  void testPageHistory() {
    var pastActivePage = inventory.activePage();
    var nextPage = Iterables.getLast(inventory.pageRegistry().findAll());
    inventory.setActivePage(nextPage);
    var pageHistory = inventory.pageHistory();
    var lastPage = pageHistory.get(pageHistory.size() - 2);
    assertEquals(pastActivePage, lastPage);
  }

  @Test
  void testInventoryClose() {
    var inventoryClose = Mockito.mock(InventoryCloseEvent.class);
    inventory.close(inventoryClose);
    assertEquals(inventory.state(), SimpleInventory.InventoryState.CLOSED);
  }
}
