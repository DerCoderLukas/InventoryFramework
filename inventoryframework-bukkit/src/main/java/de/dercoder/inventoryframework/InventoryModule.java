package de.dercoder.inventoryframework;

import java.nio.file.Path;

import com.google.common.base.Preconditions;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import de.dercoder.inventoryframework.configuration.InventoryConfigurationFile;
import de.dercoder.inventoryframework.configuration.InventoryConfigurationRepository;
import de.dercoder.inventoryframework.inventory.InventoryRegistry;
import javax.inject.Singleton;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventoryModule extends AbstractModule {
  private final JavaPlugin plugin;

  private InventoryModule(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  PluginManager providePluginManager() {
    return plugin.getServer().getPluginManager();
  }

  @Provides
  @Singleton
  YAMLFactory provideYamlFactory() {
    return YAMLFactory.builder()
      .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
      .build();
  }

  @Provides
  @Singleton
  ObjectMapper provideObjectMapper(YAMLFactory yamlFactory) {
    return new ObjectMapper(yamlFactory);
  }

  private static final String INVENTORY_CONFIGURATION_PATH = "configuration.yml";

  @Provides
  @Singleton
  @Named("configurationPath")
  Path provideConfigurationPath() {
    return Path.of(plugin.getDataFolder().getAbsolutePath(),
      INVENTORY_CONFIGURATION_PATH
    );
  }

  @Provides
  @Singleton
  InventoryConfigurationRepository provideInventoryConfigurationRepository(
    InventoryConfigurationFile configurationFile
  ) throws Exception {
    return InventoryConfigurationRepository.forFile(configurationFile);
  }

  @Provides
  @Singleton
  InventoryRegistry provideInventoryRegistry() {
    return InventoryRegistry.empty();
  }

  public static InventoryModule withPlugin(JavaPlugin plugin) {
    Preconditions.checkNotNull(plugin);
    return new InventoryModule(plugin);
  }
}
