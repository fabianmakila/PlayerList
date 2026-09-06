package fi.fabianadrian.playerlist.config;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.config.liaison.PatternLiaison;
import fi.fabianadrian.playerlist.config.liaison.SorterConfigLiaison;
import org.slf4j.Logger;
import space.arim.dazzleconf.Configuration;
import space.arim.dazzleconf.LoadResult;
import space.arim.dazzleconf.StandardErrorPrint;
import space.arim.dazzleconf.backend.Backend;
import space.arim.dazzleconf.backend.PathRoot;
import space.arim.dazzleconf.backend.toml.TomlBackend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
	private final Configuration<Config> configuration;
	private final Backend backend;
	private final StandardErrorPrint errorPrint;
	private final Path dataPath;
	private final Logger logger;
	private Config config;

	public ConfigManager(PlayerList plugin) {
		this.logger = plugin.getSLF4JLogger();
		this.dataPath = plugin.getDataPath();
		this.configuration = Configuration.defaultBuilder(Config.class).addTypeLiaisons(new PatternLiaison(), new SorterConfigLiaison()).build();
		this.backend = new TomlBackend(new PathRoot(this.dataPath.resolve("config.toml")));
		this.errorPrint = new StandardErrorPrint(printable -> plugin.getSLF4JLogger().error(printable.printString()));
	}

	public void load() {
		try {
			Files.createDirectories(this.dataPath);
		} catch (IOException exception) {
			this.logger.error("Failed to create data directory", exception);
			throw new ConfigLoadException(exception.getMessage());
		}
		LoadResult<Config> result = this.configuration.configureWith(this.backend);
		if (result.isFailure()) {
			if (this.config == null) {
				this.config = this.configuration.loadDefaults();
			}
			this.errorPrint.onError(result.getErrorContexts());
			throw new ConfigLoadException();
		} else {
			this.config = result.getValue();
		}
	}

	public Config config() {
		return this.config;
	}
}
