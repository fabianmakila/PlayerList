package fi.fabianadrian.playerlist.config.sorter.player;

import fi.fabianadrian.playerlist.config.sorter.SorterConfig;

public interface PlayerSorterConfig extends SorterConfig {
	default PlayerSorterCriteria criteria() {
		return PlayerSorterCriteria.NAME;
	}

	default boolean caseSensitive() {
		return false;
	}
}
