package de.dercoder.inventoryframework.inventory;

import com.google.inject.Inject;

import de.dercoder.inventoryframework.configuration.InventoryConfigurationRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class InventoryCommand implements CommandExecutor {
  private final InventoryRegistry inventoryRegistry;
  private final InventoryConfigurationRepository configurationRepository;

  @Inject
  private InventoryCommand(
    InventoryRegistry inventoryRegistry,
    InventoryConfigurationRepository configurationRepository
  ) {
    this.inventoryRegistry = inventoryRegistry;
    this.configurationRepository = configurationRepository;
  }

  @Override
  public boolean onCommand(
    CommandSender commandSender, Command command, String s, String[] arguments
  ) {
    if (!(commandSender instanceof Player) || arguments.length != 1) {
      return false;
    }
    var player = (Player) commandSender;
    var inventoryName = arguments[0];
    inventoryRegistry.findOneByName(inventoryName)
      .ifPresentOrElse(inventory -> openInventory(inventory, player),
        () -> player.sendMessage(configurationRepository.translateMessage(
          "cant-find-inventory",
          inventoryName
        ))
      );
    return true;
  }

  private void openInventory(SimpleInventory inventory, Player player) {
    player.sendMessage(configurationRepository.translateMessage("open-inventory",
      inventory.name()
    ));
    var copiedInventory = inventory.copy(player.getUniqueId());
    inventoryRegistry.register(copiedInventory);
    var activePageInventory = copiedInventory.activePage().asInventory();
    player.openInventory(activePageInventory);
  }
}
