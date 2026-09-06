package fi.fabianadrian.playerlist.config.sorter;

import fi.fabianadrian.playerlist.list.sorter.SortingOrder;

public interface SorterConfig {
	SorterType type();

	default SortingOrder order() {
		return SortingOrder.ASCENDING;
	}
}
