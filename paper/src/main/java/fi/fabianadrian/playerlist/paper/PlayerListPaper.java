package fi.fabianadrian.playerlist.paper;

import fi.fabianadrian.playerlist.common.PlayerList;
import fi.fabianadrian.playerlist.common.platform.Platform;
import fi.fabianadrian.playerlist.common.playerlist.PlayerListManager;
import fi.fabianadrian.playerlist.paper.command.RootCommandExecutor;
import fi.fabianadrian.playerlist.paper.listener.PlayerListener;
import fi.fabianadrian.playerlist.paper.playerlist.PaperPlayerListManager;
import fi.fabianadrian.playerlist.paper.scoreboard.ScoreboardManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class PlayerListPaper extends JavaPlugin implements Platform<Player> {
	private PlayerList playerList;
	private PaperPlayerListManager playerListManager;
	private ScoreboardManager scoreboardManager;
	private boolean isMiniPlaceholdersEnabled;

	@Override
	public void onEnable() {
		this.playerList = new PlayerList(this);

		this.playerListManager = new PaperPlayerListManager(this.playerList);
		this.playerListManager.reload();

		this.scoreboardManager = new ScoreboardManager(this);
		this.scoreboardManager.reload();

		registerCommands();
		registerListeners();

		this.isMiniPlaceholdersEnabled = getServer().getPluginManager().isPluginEnabled("MiniPlaceholders");
	}

	public void reload() {
		this.playerList.reload();
		this.scoreboardManager.reload();
	}

	public ScoreboardManager scoreboardManager() {
		return this.scoreboardManager;
	}

	private void registerCommands() {
		PluginCommand rootCommand = getCommand("playerlist");
		if (rootCommand == null) {
			return;
		}

		RootCommandExecutor rootCommandExecutor = new RootCommandExecutor(this);
		rootCommand.setExecutor(rootCommandExecutor);
		rootCommand.setTabCompleter(rootCommandExecutor);
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new PlayerListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));
	}

	public PlayerList playerList() {
		return this.playerList;
	}

	@Override
	public CompletableFuture<List<Player>> onlinePlayers() {
		CompletableFuture<List<Player>> onlinePlayersFuture = new CompletableFuture<>();
		getServer().getScheduler().runTask(this, () -> onlinePlayersFuture.complete(List.copyOf(getServer().getOnlinePlayers())));
		return onlinePlayersFuture;
	}

	@Override
	public Path dataFolder() {
		return getDataFolder().toPath();
	}

	@Override
	public Logger logger() {
		return getSLF4JLogger();
	}

	@Override
	public PlayerListManager playerListManager() {
		return this.playerListManager;
	}

	@Override
	public boolean isMiniplaceholdersAvailable() {
		return this.isMiniPlaceholdersEnabled;
	}
}
