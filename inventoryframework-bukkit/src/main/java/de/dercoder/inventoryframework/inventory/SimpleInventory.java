package de.dercoder.inventoryframework.inventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.dercoder.inventoryframework.item.InventoryItem;
import de.dercoder.inventoryframework.page.InventoryPage;
import de.dercoder.inventoryframework.page.InventoryPageRegistry;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class SimpleInventory {
  private final UUID id;
  private final String name;
  private final InventoryPageRegistry pageRegistry;
  private final InventoryOpenBehaviour openBehaviour;
  private final InventoryCloseBehaviour closeBehaviour;
  private final List<InventoryPage> pageHistory = Lists.newArrayList();

  public enum InventoryState {
    OPEN, ON_SWITCH, CLOSED
  }

  private InventoryState state;

  protected SimpleInventory(
    UUID id,
    String name,
    InventoryPageRegistry pageRegistry,
    InventoryOpenBehaviour openBehaviour,
    InventoryCloseBehaviour closeBehaviour
  ) {
    this.id = id;
    this.name = name;
    this.pageRegistry = pageRegistry;
    this.openBehaviour = openBehaviour;
    this.closeBehaviour = closeBehaviour;
  }

  public void open(InventoryOpenEvent inventoryOpen) {
    Preconditions.checkNotNull(inventoryOpen);
    openBehaviour.open(inventoryOpen);
    switchState(InventoryState.OPEN);
  }

  public void close(InventoryCloseEvent inventoryClose) {
    Preconditions.checkNotNull(inventoryClose);
    closeBehaviour.close(inventoryClose);
    switchState(InventoryState.CLOSED);
  }

  public Stream<InventoryItem> findItem(ItemStack itemStack) {
    Preconditions.checkNotNull(itemStack);
    return pageRegistry.findAll()
      .stream()
      .map(inventoryPage -> inventoryPage.findItem(itemStack))
      .flatMap(Optional::stream);
  }

  public void switchPage(InventoryPage inventoryPage) {
    Preconditions.checkNotNull(inventoryPage);
    switchState(InventoryState.ON_SWITCH);
    setActivePage(inventoryPage);
  }

  public void switchState(InventoryState state) {
    Preconditions.checkNotNull(state);
    this.state = state;
  }

  public SimpleInventory copy(UUID uuid) {
    Preconditions.checkNotNull(uuid);
    var copiedInventory = create(uuid, name, openBehaviour, closeBehaviour);
    return copyInventory(copiedInventory);
  }

  protected <T extends SimpleInventory> T copyInventory(T inventory) {
    var activePage = inventory.pageRegistry().findAll().get(0);
    inventory.setActivePage(activePage);
    return inventory;
  }

  public UUID id() {
    return id;
  }

  public String name() {
    return name;
  }

  public InventoryPageRegistry pageRegistry() {
    return pageRegistry;
  }

  protected InventoryOpenBehaviour openBehaviour() {
    return openBehaviour;
  }

  protected InventoryCloseBehaviour closeBehaviour() {
    return closeBehaviour;
  }

  public List<InventoryPage> pageHistory() {
    return List.copyOf(pageHistory);
  }

  public InventoryPage activePage() {
    return Iterables.getLast(pageHistory);
  }

  public void setActivePage(InventoryPage activePage) {
    pageHistory.add(activePage);
  }

  public InventoryState state() {
    return state;
  }

  public static SimpleInventory create(
    UUID id,
    String name,
    InventoryOpenBehaviour openBehaviour,
    InventoryCloseBehaviour closeBehaviour
  ) {
    var inventory = new SimpleInventory(id,
      name,
      InventoryPageRegistry.empty(),
      openBehaviour,
      closeBehaviour
    );
    inventory.switchState(InventoryState.CLOSED);
    return inventory;
  }
}
