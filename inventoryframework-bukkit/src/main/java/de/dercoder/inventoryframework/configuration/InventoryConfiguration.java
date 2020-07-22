package de.dercoder.inventoryframework.configuration;

import java.util.Map;

import com.google.common.base.Preconditions;

public final class InventoryConfiguration {
  private Map<String, String> messages;

  InventoryConfiguration() {

  }

  InventoryConfiguration(Map<String, String> messages) {
    this.messages = messages;
  }

  public void setMessages(Map<String, String> messages) {
    Preconditions.checkNotNull(messages);
    this.messages = messages;
  }

  public Map<String, String> getMessages() {
    return messages;
  }
}
