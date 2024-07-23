package fi.fabianadrian.playerlist.paper.scoreboard.team;

import fi.fabianadrian.playerlist.common.scoreboard.team.TeamManager;
import fi.fabianadrian.playerlist.paper.PlayerListPaper;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public final class PaperTeamManager extends TeamManager<Player> {
	private final Scoreboard scoreboard;
	private final PlayerListPaper plugin;

	public PaperTeamManager(PlayerListPaper plugin, Scoreboard scoreboard) {
		super(plugin.playerList());

		this.scoreboard = scoreboard;
		this.plugin = plugin;
	}

	@Override
	public void update(Player player) {
		Team team = this.scoreboard.getPlayerTeam(player);
		if (team == null) {
			return;
		}

		update(player, team);
	}

	@Override
	public void sort() {
		unregisterAllTeams();

		List<Player> onlinePlayers = List.copyOf(this.plugin.getServer().getOnlinePlayers());
		onlinePlayers.forEach(player -> {
			StringBuilder builder = new StringBuilder("playerlist-");
			this.sorters.forEach(sorter -> builder.append(sorter.output(player)));
			builder.append("-").append(player.getName());
			registerTeam(player, builder.toString());
		});
	}

	@Override
	public void unregisterAllTeams() {
		this.scoreboard.getTeams().forEach(Team::unregister);
	}

	private void update(Player player, Team team) {
		updatePrefix(player, team);
		updateSuffix(player, team);
	}

	private void updatePrefix(Player player, Team team) {
		String prefix = this.configuration.prefix();

		if (prefix.isBlank()) {
			team.prefix(null);
			return;
		}

		Component prefixComponent;
		if (this.plugin.isMiniplaceholdersAvailable()) {
			prefixComponent = this.miniMessage.deserialize(prefix, MiniPlaceholders.getAudienceGlobalPlaceholders(player));
		} else {
			prefixComponent = this.miniMessage.deserialize(prefix);
		}

		team.prefix(prefixComponent);
	}

	private void updateSuffix(Player player, Team team) {
		String suffix = this.configuration.suffix();

		if (suffix.isBlank()) {
			team.suffix(null);
			return;
		}

		Component suffixComponent;
		if (this.plugin.isMiniplaceholdersAvailable()) {
			suffixComponent = this.miniMessage.deserialize(suffix, MiniPlaceholders.getAudienceGlobalPlaceholders(player));
		} else {
			suffixComponent = this.miniMessage.deserialize(suffix);
		}

		team.suffix(suffixComponent);
	}

	private void registerTeam(Player player, String teamName) {
		Team team = this.scoreboard.registerNewTeam(teamName);

		Team.OptionStatus collisionStatus = this.configuration.collisions() ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER;
		team.setOption(Team.Option.COLLISION_RULE, collisionStatus);
		Team.OptionStatus deathMessageVisibilityStatus = this.configuration.deathMessages() ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER;
		team.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, deathMessageVisibilityStatus);
		Team.OptionStatus nameTagVisibilityStatus = this.configuration.nameTags() ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER;
		team.setOption(Team.Option.NAME_TAG_VISIBILITY, nameTagVisibilityStatus);

		team.addEntity(player);
		update(player, team);
	}
}
