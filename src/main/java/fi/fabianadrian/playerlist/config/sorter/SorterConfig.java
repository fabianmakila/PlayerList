package fi.fabianadrian.playerlist.config.sorter;

import fi.fabianadrian.playerlist.list.sorting.SortingOrder;

public interface SorterConfig {
	default SortingOrder order() {
		return SortingOrder.ASCENDING;
	}
}
