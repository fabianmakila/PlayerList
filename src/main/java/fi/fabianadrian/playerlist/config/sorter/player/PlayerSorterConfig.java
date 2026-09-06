package fi.fabianadrian.playerlist.config.sorter.player;

import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterType;

public interface PlayerSorterConfig extends SorterConfig {
	@Override
	default SorterType type() {
		return SorterType.PLAYER;
	}

	default PlayerSorterCriteria criteria() {
		return PlayerSorterCriteria.NAME;
	}
}
