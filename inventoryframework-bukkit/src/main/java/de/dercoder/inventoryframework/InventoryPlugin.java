package de.dercoder.inventoryframework;

import com.google.inject.Guice;
import com.google.inject.Inject;

import de.dercoder.inventoryframework.inventory.InventoryBehaviourTrigger;
import de.dercoder.inventoryframework.inventory.InventoryCommand;
import de.dercoder.inventoryframework.inventory.InventoryRegistry;
import de.dercoder.inventoryframework.item.InventoryItemBehaviourTrigger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventoryPlugin extends JavaPlugin {
  @Inject
  private PluginManager pluginManager;
  @Inject
  private InventoryItemBehaviourTrigger itemBehaviourTrigger;
  @Inject
  private InventoryBehaviourTrigger inventoryBehaviourTrigger;
  @Inject
  private InventoryCommand inventoryCommand;
  @Inject
  private InventoryRegistry inventoryRegistry;

  @Override
  public void onEnable() {
    saveDefaultResources();
    var module = InventoryModule.withPlugin(this);
    var injector = Guice.createInjector(module);
    injector.injectMembers(this);
    registerListeners();
    registerCommands();
  }

  private void saveDefaultResources() {
    saveResource("configuration.yml", false);
  }

  private void registerListeners() {
    pluginManager.registerEvents(itemBehaviourTrigger, this);
    pluginManager.registerEvents(inventoryBehaviourTrigger, this);
  }

  private void registerCommands() {
    getCommand("inventory").setExecutor(inventoryCommand);
  }
}
