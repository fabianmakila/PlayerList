package fi.fabianadrian.playerlist.config;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.config.liaison.PatternLiaison;
import space.arim.dazzleconf.Configuration;
import space.arim.dazzleconf.LoadResult;
import space.arim.dazzleconf.StandardErrorPrint;
import space.arim.dazzleconf.backend.Backend;
import space.arim.dazzleconf.backend.PathRoot;
import space.arim.dazzleconf.backend.toml.TomlBackend;

public final class ConfigManager {
	private final Configuration<Config> configuration;
	private final Backend backend;
	private final StandardErrorPrint errorPrint;
	private Config config;

	public ConfigManager(PlayerList plugin) {
		this.configuration = Configuration.defaultBuilder(Config.class).addTypeLiaisons(new PatternLiaison()).build();
		this.backend = new TomlBackend(new PathRoot(plugin.getDataPath().resolve("config.toml")));
		this.errorPrint = new StandardErrorPrint(printable -> plugin.getSLF4JLogger().error(printable.printString()));
	}

	public void load() {
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
