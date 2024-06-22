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
	private final PaperTeamManager teamManager;
	private ScheduledFuture<?> updateScheduledFuture;
	private ScoreboardConfigurationSection configuration;

	public ScoreboardManager(PlayerListPaper plugin) {
		this.plugin = plugin;
		this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
		this.teamManager = new PaperTeamManager(this.plugin, this.scoreboard);
	}

	public void reload() {
		this.configuration = this.plugin.playerList().configuration().scoreBoard();

		if(this.updateScheduledFuture != null) {
			this.updateScheduledFuture.cancel(true);
		}

		this.teamManager.reload();
		this.teamManager.unregisterAllTeams();

		if (!this.configuration.enabled()) {
			return;
		}

		int updateInterval = this.plugin.playerList().configuration().placeholderRefreshInterval();
		this.updateScheduledFuture = this.plugin.playerList().scheduledExecutor().scheduleAtFixedRate(() ->
				this.plugin.onlinePlayers().thenAcceptAsync(
						this::update,
						this.plugin.playerList().scheduledExecutor()
				), 0, updateInterval, TimeUnit.SECONDS
		);

		List<Player> onlinePlayers = List.copyOf(this.plugin.getServer().getOnlinePlayers());
		onlinePlayers.forEach(player -> player.setScoreboard(this.scoreboard));

		this.teamManager.sort();
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
