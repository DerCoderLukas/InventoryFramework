package de.dercoder.inventoryframework.configuration;

import com.google.common.base.Preconditions;

public final class InventoryConfigurationRepository {
  private final InventoryConfiguration configuration;

  private InventoryConfigurationRepository(InventoryConfiguration configuration) {
    this.configuration = configuration;
  }

  public String translateMessage(String messageKey, Object... arguments) {
    Preconditions.checkNotNull(messageKey, arguments);
    var message = findMessage(messageKey);
    return formatMessage(message, arguments);
  }

  private String findMessage(String messageKey) {
    return configuration.getMessages().get(messageKey);
  }

  private String formatMessage(String message, Object... arguments) {
    if (arguments.length == 0) {
      return message;
    }
    return String.format(message, arguments);
  }

  public InventoryConfiguration configuration() {
    return configuration;
  }

  public static InventoryConfigurationRepository forFile(
    InventoryConfigurationFile configurationFile
  ) throws Exception {
    Preconditions.checkNotNull(configurationFile);
    var configuration = configurationFile.read();
    return new InventoryConfigurationRepository(configuration);
  }
}
