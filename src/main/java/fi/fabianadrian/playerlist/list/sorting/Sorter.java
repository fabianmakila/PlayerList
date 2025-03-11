package fi.fabianadrian.playerlist.list.sorting;

import org.bukkit.entity.Player;

import java.util.Comparator;

public abstract class Sorter {
	private final SortingOrder order;
	private final SorterType type;
	protected Comparator<Player> comparator;

	public Sorter(SorterType type, SortingOrder order) {
		this.type = type;
		this.order = order;
	}

	public SorterType type() {
		return this.type;
	}

	public SortingOrder order() {
		return this.order;
	}

	public Comparator<Player> comparator() {
		if (this.order == SortingOrder.ASCENDING || comparator == null) {
			return comparator;
		} else {
			return comparator.reversed();
		}
	}
}
