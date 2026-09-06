package fi.fabianadrian.playerlist;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.bukkit.BukkitContext;
import fi.fabianadrian.playerlist.config.Config;
import fi.fabianadrian.playerlist.config.ConfigManager;
import fi.fabianadrian.playerlist.list.ListManager;
import fi.fabianadrian.playerlist.listener.PlayerListener;
import fi.fabianadrian.playerlist.locale.TranslationManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class PlayerList extends JavaPlugin {
	public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware();
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private final BukkitContext context = new BukkitContext.Factory(this, "86b0f1e3c8330be09eb084f6d0ca782d")
			.errorTrackerService(ERROR_TRACKER)
			.metrics(Metrics.Factory::create)
			.create();
	private final ConfigManager configManager;
	private final ListManager listManager;
	private final MiniMessage miniMessage = MiniMessage.miniMessage();

	public PlayerList() {
		new TranslationManager(this);
		this.configManager = new ConfigManager(this);
		this.listManager = new ListManager(this);
	}

	@Override
	public void onEnable() {
		this.context.ready();
		load();
		registerCommands();
		registerListeners();
	}

	@Override
	public void onDisable() {
		this.context.shutdown();
	}

	public Config config() {
		return this.configManager.config();
	}

	public void load() {
		this.configManager.load();
		this.listManager.load();
	}

	public ScheduledExecutorService executorService() {
		return this.executorService;
	}

	public ListManager playerListManager() {
		return this.listManager;
	}

	public MiniMessage miniMessage() {
		return this.miniMessage;
	}

	private void registerCommands() {
		this.getLifecycleManager().registerEventHandler(
				LifecycleEvents.COMMANDS,
				commands -> commands.registrar().register(PlayerListCommandBrigadier.create(this))
		);
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new PlayerListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));
	}
}
