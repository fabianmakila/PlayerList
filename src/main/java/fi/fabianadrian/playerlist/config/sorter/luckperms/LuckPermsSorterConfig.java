package fi.fabianadrian.playerlist.config.sorter.luckperms;

import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterType;

public interface LuckPermsSorterConfig extends SorterConfig {
	@Override
	default SorterType type() {
		return SorterType.LUCKPERMS;
	}

	default LuckPermsSorterCriteria criteria() {
		return LuckPermsSorterCriteria.PREFIX_WEIGHT;
	}
}
