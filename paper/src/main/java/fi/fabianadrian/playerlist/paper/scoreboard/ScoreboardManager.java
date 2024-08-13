package fi.fabianadrian.playerlist.paper.scoreboard;

import fi.fabianadrian.playerlist.common.configuration.section.scoreboard.ScoreboardConfigurationSection;
import fi.fabianadrian.playerlist.paper.PlayerListPaper;
import fi.fabianadrian.playerlist.paper.scoreboard.team.PaperTeamManager;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ScoreboardManager {
	private final PlayerListPaper plugin;
	private final Scoreboard scoreboard;
	private PaperTeamManager teamManager;
	private ScheduledFuture<?> updateScheduledFuture;
	private ScoreboardConfigurationSection configuration;

	public ScoreboardManager(PlayerListPaper plugin) {
		this.plugin = plugin;
		this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
	}

	public void reload() {
		this.configuration = this.plugin.playerList().configuration().scoreBoard();

		// Cancel update task
		if (this.updateScheduledFuture != null) {
			this.updateScheduledFuture.cancel(true);
		}

		// Shutdown team manager
		if (this.teamManager != null) {
			this.teamManager.shutdown();
			this.teamManager = null;
		}

		// Stop here if scoreboard is not enabled
		if (!this.configuration.enabled()) {
			return;
		}

		// Set everyone's scoreboard
		List<Player> onlinePlayers = List.copyOf(this.plugin.getServer().getOnlinePlayers());
		onlinePlayers.forEach(player -> player.setScoreboard(this.scoreboard));

		// Construct team manager and sort right away
		this.teamManager = new PaperTeamManager(this.plugin, this.scoreboard);
		this.teamManager.sort();

		// Start the update task
		int updateInterval = this.plugin.playerList().configuration().placeholderRefreshInterval();
		this.updateScheduledFuture = this.plugin.playerList().scheduledExecutor().scheduleAtFixedRate(() ->
				this.plugin.onlinePlayers().thenAcceptAsync(
						this::update,
						this.plugin.playerList().scheduledExecutor()
				), 0, updateInterval, TimeUnit.SECONDS
		);
	}

	public void register(Player player) {
		if (!this.configuration.enabled()) {
			return;
		}

		player.setScoreboard(this.scoreboard);
		this.teamManager.sort();
	}

	private void update(List<Player> players) {
		this.teamManager.update(players);
	}
}
