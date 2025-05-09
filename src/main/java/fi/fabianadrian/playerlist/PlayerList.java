package fi.fabianadrian.playerlist;

import fi.fabianadrian.playerlist.command.PlayerListCommand;
import fi.fabianadrian.playerlist.configuration.Configuration;
import fi.fabianadrian.playerlist.configuration.ConfigurationManager;
import fi.fabianadrian.playerlist.list.ListManager;
import fi.fabianadrian.playerlist.listener.JoinListener;
import fi.fabianadrian.playerlist.locale.TranslationManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class PlayerList extends JavaPlugin {
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private ConfigurationManager configurationManager;
	private ListManager listManager;

	@Override
	public void onEnable() {
		new TranslationManager(this);

		this.configurationManager = new ConfigurationManager(this);
		try {
			this.configurationManager.reload();
		} catch (ConfigurateException e) {
			getSLF4JLogger().error("Couldn't load configuration", e);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		this.listManager = new ListManager(this);
		this.listManager.reload();

		registerCommands();
		registerListeners();

		new Metrics(this, 25811);
	}

	public Configuration configuration() {
		return this.configurationManager.configuration();
	}

	public void reload() throws ConfigurateException {
		this.configurationManager.reload();
		this.listManager.reload();
	}

	public ScheduledExecutorService executorService() {
		return this.executorService;
	}

	public CompletableFuture<List<Player>> onlinePlayers() {
		CompletableFuture<List<Player>> onlinePlayersFuture = new CompletableFuture<>();
		getServer().getScheduler().runTask(this, () -> onlinePlayersFuture.complete(new ArrayList<>(getServer().getOnlinePlayers())));
		return onlinePlayersFuture;
	}

	public ListManager playerListManager() {
		return this.listManager;
	}

	private void registerCommands() {
		this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
			commands.registrar().register(new PlayerListCommand(this).command());
		});
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new JoinListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));
	}
}
