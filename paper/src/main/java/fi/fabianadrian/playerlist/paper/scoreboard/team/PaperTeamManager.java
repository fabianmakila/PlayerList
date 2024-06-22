package fi.fabianadrian.playerlist.paper.scoreboard.team;

import fi.fabianadrian.playerlist.common.scoreboard.team.TeamManager;
import fi.fabianadrian.playerlist.paper.PlayerListPaper;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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
		TagResolver miniPlaceholdersResolver = MiniPlaceholders.getAudienceGlobalPlaceholders(player);

		String prefix = this.configuration.prefix();
		if (!prefix.isBlank()) {
			team.prefix(this.miniMessage.deserialize(
					prefix,
					miniPlaceholdersResolver
			));
		} else {
			team.prefix(null);
		}

		String suffix = this.configuration.suffix();
		if (!suffix.isBlank()) {
			team.suffix(this.miniMessage.deserialize(
					suffix,
					miniPlaceholdersResolver
			));
		} else {
			team.suffix(null);
		}
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
