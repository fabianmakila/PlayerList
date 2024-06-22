package fi.fabianadrian.playerlist.common.scoreboard.team.sorter;

import net.kyori.adventure.audience.Audience;

public abstract class Sorter {
	protected int baseInteger = Integer.parseInt("fff", 16) / 2;
	protected SorterOrder order;

	public Sorter(SorterOrder order) {
		this.order = order;
	}

	public abstract String output(Audience player);
}
