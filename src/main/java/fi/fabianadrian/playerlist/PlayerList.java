package fi.fabianadrian.playerlist;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.bukkit.BukkitContext;
import fi.fabianadrian.playerlist.command.PlayerListCommand;
import fi.fabianadrian.playerlist.configuration.Configuration;
import fi.fabianadrian.playerlist.configuration.ConfigurationManager;
import fi.fabianadrian.playerlist.list.ListManager;
import fi.fabianadrian.playerlist.listener.JoinListener;
import fi.fabianadrian.playerlist.locale.TranslationManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
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
	public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware();
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private final BukkitContext context = new BukkitContext.Factory(this, "86b0f1e3c8330be09eb084f6d0ca782d")
			.errorTrackerService(ERROR_TRACKER)
			.metrics(Metrics.Factory::create)
			.create();
	private ConfigurationManager configurationManager;
	private ListManager listManager;

	@Override
	public void onEnable() {
		this.context.ready();

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
	}

	@Override
	public void onDisable() {
		this.context.shutdown();
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
