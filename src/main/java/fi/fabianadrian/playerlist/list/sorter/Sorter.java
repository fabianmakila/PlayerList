package fi.fabianadrian.playerlist.list.sorter;

import org.bukkit.entity.Player;

import java.util.Comparator;

public abstract class Sorter implements Comparator<Player> {
	protected final SortingOrder order;

	public Sorter(SortingOrder order) {
		this.order = order;
	}

	protected abstract Comparator<Player> comparator();

	@Override
	public int compare(Player o1, Player o2) {
		Comparator<Player> comparator = comparator();
		if (comparator == null) {
			return 0;
		}

		int result = comparator.compare(o1, o2);
		return this.order == SortingOrder.DESCENDING ? -result : result;
	}
}
