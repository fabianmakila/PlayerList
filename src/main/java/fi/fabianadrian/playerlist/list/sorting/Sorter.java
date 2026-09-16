package fi.fabianadrian.playerlist.list.sorting;

import org.bukkit.entity.Player;

import java.util.Comparator;

public abstract class Sorter {
	protected final SortingOrder order;

	public Sorter(SortingOrder order) {
		this.order = order;
	}

	public Comparator<Player> build() {
		Comparator<Player> comparator = comparator();
		if (comparator == null) {
			return null;
		}
		return this.order == SortingOrder.DESCENDING ? comparator.reversed() : comparator;
	}

	protected abstract Comparator<Player> comparator();
}
