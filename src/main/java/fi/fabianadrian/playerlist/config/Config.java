package fi.fabianadrian.playerlist.config;

import fi.fabianadrian.playerlist.config.group.ExampleGroupConfig;
import fi.fabianadrian.playerlist.config.group.GroupConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.player.PlayerSorterConfig;

import java.util.List;
import java.util.Map;

public interface Config {
	default Map<String, GroupConfig> groups() {
		return Map.of("example", new ExampleGroupConfig());
	}

	default List<SorterConfig> sorters() {
		return List.of(new PlayerSorterConfig() {
		});
	}

	default int placeholderRefreshInterval() {
		return 5;
	}
}
