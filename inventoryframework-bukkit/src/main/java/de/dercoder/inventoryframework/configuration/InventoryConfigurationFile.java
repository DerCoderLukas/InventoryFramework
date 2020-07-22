package de.dercoder.inventoryframework.configuration;

import java.nio.file.Path;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
public final class InventoryConfigurationFile {
  private final ObjectMapper objectMapper;
  private final Path configurationPath;

  @Inject
  private InventoryConfigurationFile(
    ObjectMapper objectMapper,
    @Named("configurationPath") Path configurationPath
  ) {
    this.objectMapper = objectMapper;
    this.configurationPath = configurationPath;
  }

  public InventoryConfiguration read() throws Exception {
    return objectMapper.readValue(configurationPath.toFile(),
      InventoryConfiguration.class
    );
  }
}
