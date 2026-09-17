package fi.fabianadrian.playerlist.config;

import fi.fabianadrian.playerlist.config.group.ExampleGroupConfig;
import fi.fabianadrian.playerlist.config.group.ExampleNetherOverrideConfig;
import fi.fabianadrian.playerlist.config.group.GroupConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.luckperms.LuckPermsSorterConfig;
import fi.fabianadrian.playerlist.config.sorter.player.PlayerSorterConfig;
import space.arim.dazzleconf.engine.Comments;
import space.arim.dazzleconf.engine.liaison.SubSection;

import java.util.List;

public interface Config {
	@Comments("Groups allow you to apply same options to multiple worlds")
	@Comments("Highest group gets applied first and lowest group gets applied last")
	default List<@SubSection GroupConfig> group() {
		return List.of(
				new ExampleGroupConfig(),
				new ExampleNetherOverrideConfig()
		);
	}

	@Comments("Sorters control how the player list entries are sorted")
	@Comments("You can add as many sorters as you want")
	@Comments("Sorters defined higher in the config file have higher priority")
	default List<SorterConfig> sorter() {
		return List.of(
				new PlayerSorterConfig() {
				},
				new LuckPermsSorterConfig() {
				});
	}

	@Comments("Time in milliseconds how often the player list is updated")
	@Comments("If you have rapidly changing placeholders you might want to reduce this")
	default int updateIntervalMilliSeconds() {
		return 5000;
	}
}
