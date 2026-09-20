package fi.fabianadrian.playerlist.config.sorter.luckperms;

import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterType;
import fi.fabianadrian.playerlist.list.sorting.SortingOrder;

public interface LuckPermsSorterConfig extends SorterConfig {
	@Override
	default SorterType type() {
		return SorterType.LUCKPERMS;
	}

	@Override
	default SortingOrder order() {
		return SortingOrder.DESCENDING;
	}

	default LuckPermsSorterCriteria criteria() {
		return LuckPermsSorterCriteria.PREFIX_WEIGHT;
	}
}
