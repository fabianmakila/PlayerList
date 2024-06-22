package fi.fabianadrian.playerlist.common;

import fi.fabianadrian.playerlist.common.configuration.ConfigurationManager;
import fi.fabianadrian.playerlist.common.configuration.MainConfiguration;
import fi.fabianadrian.playerlist.common.platform.Platform;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class PlayerList {
	private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
	private final Platform<?> platform;
	private final ConfigurationManager<MainConfiguration> configurationManager;

	public PlayerList(Platform<?> platform) {
		this.platform = platform;

		this.configurationManager = ConfigurationManager.create(
				platform.dataFolder(),
				"config.yml",
				MainConfiguration.class,
				platform.logger()
		);
		this.configurationManager.reload();
	}

	public MainConfiguration configuration() {
		return this.configurationManager.configuration();
	}

	public ScheduledExecutorService scheduledExecutor() {
		return this.scheduledExecutor;
	}

	public Platform<?> platform() {
		return this.platform;
	}

	public void reload() {
		this.configurationManager.reload();
		this.platform.playerListManager().reload();
	}
}
