package fi.fabianadrian.playerlist.configuration;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.configuration.serializer.SorterSerializer;
import fi.fabianadrian.playerlist.list.sorting.Sorter;
import org.spongepowered.configurate.ConfigurateException;


public final class ConfigurationManager {
	private final ConfigurationLoader<Configuration> configurationLoader;
	private Configuration configuration;

	public ConfigurationManager(PlayerList plugin) {
		this.configurationLoader = new ConfigurationLoader<>(
				Configuration.class,
				plugin.getDataPath().resolve("config.yml"),
				options -> options.header("PlayerList Main Configuration")
						.serializers(builder -> builder
								.register(Sorter.class, new SorterSerializer(plugin))
						)
		);
	}

	public void reload() throws ConfigurateException {
		this.configuration = this.configurationLoader.load();
		this.configurationLoader.save(this.configuration);
	}

	public Configuration configuration() {
		if (this.configuration == null) {
			throw new IllegalStateException("Main configuration isn't loaded");
		}
		return this.configuration;
	}
}
