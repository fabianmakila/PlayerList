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
	private final PlayerList playerList;
	protected ScoreboardTeamConfigurationSection configuration;

	public TeamManager(PlayerList playerList) {
		this.playerList = playerList;
	}

	public void reload() {
		this.configuration = this.playerList.configuration().scoreBoard().team();

		this.sorters.clear();
		SorterFactory sorterFactory = new SorterFactory(this.playerList);
		this.configuration.sorters().forEach(unparsed -> {
			Sorter sorter;
			try {
				sorter = sorterFactory.sorter(unparsed);
			} catch (Exception ex) {
				this.playerList.platform().logger().warn("Could not parse sorter {}", unparsed, ex);
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
}
