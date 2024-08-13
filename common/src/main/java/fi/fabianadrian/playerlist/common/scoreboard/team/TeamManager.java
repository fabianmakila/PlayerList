package fi.fabianadrian.playerlist.common.scoreboard.team;

import fi.fabianadrian.playerlist.common.PlayerList;
import fi.fabianadrian.playerlist.common.configuration.section.scoreboard.ScoreboardTeamConfigurationSection;
import fi.fabianadrian.playerlist.common.scoreboard.team.sorter.Sorter;
import fi.fabianadrian.playerlist.common.scoreboard.team.sorter.SorterFactory;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class TeamManager<P> {
	protected final MiniMessage miniMessage = MiniMessage.miniMessage();
	protected final List<Sorter> sorters = new ArrayList<>();
	protected final ScoreboardTeamConfigurationSection configuration;

	public TeamManager(PlayerList playerList) {
		this.configuration = playerList.configuration().scoreBoard().team();

		SorterFactory sorterFactory = new SorterFactory(playerList);
		this.configuration.sorters().forEach(unparsed -> {
			Sorter sorter;
			try {
				sorter = sorterFactory.sorter(unparsed);
			} catch (Exception ex) {
				playerList.platform().logger().warn("Could not parse sorter {}", unparsed, ex);
				return;
			}
			this.sorters.add(sorter);
		});
	}

	public void update(List<P> players) {
		players.forEach(this::update);
	}

	public abstract void update(P player);

	public abstract void sort();

	public abstract void unregisterAllTeams();

	public void shutdown() {
		unregisterAllTeams();
		this.sorters.clear();
	}
}
