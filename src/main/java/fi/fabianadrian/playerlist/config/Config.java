package fi.fabianadrian.playerlist.config;

import fi.fabianadrian.playerlist.config.group.ExampleDefaultGroupConfig;
import fi.fabianadrian.playerlist.config.group.ExampleNetherOverrideConfig;
import fi.fabianadrian.playerlist.config.group.GroupConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.luckperms.LuckPermsSorterConfig;
import fi.fabianadrian.playerlist.config.sorter.player.PlayerSorterConfig;
import space.arim.dazzleconf.engine.Comments;
import space.arim.dazzleconf.engine.liaison.IntegerRange;
import space.arim.dazzleconf.engine.liaison.SubSection;

import java.util.List;

public interface Config {
	@Comments("Groups let you apply the same options to multiple worlds at once")
	@Comments("These groups are applied in order from top to bottom, and each group can override the ones before it")
	@Comments("This lets you set defaults in one group, then override just a few settings for specific worlds in a group below it")
	default List<@SubSection GroupConfig> group() {
		return List.of(
				new ExampleDefaultGroupConfig(),
				new ExampleNetherOverrideConfig()
		);
	}

	@Comments("Sorters control how the player list entries are sorted, applied in order from top to bottom")
	@Comments("This works like sorting a spreadsheet by multiple columns: the first sorter sorts everything, and any players it can't separate are then sorted by the next one, and so on")
	default List<SorterConfig> sorter() {
		return List.of(
				new LuckPermsSorterConfig() {
				},
				new PlayerSorterConfig() {
				}
		);
	}

	@Comments("How often the player list is updated, in milliseconds")
	@Comments("If you have rapidly changing placeholders, you might want to set this lower")
	@IntegerRange(min = 1)
	default int updateIntervalMilliseconds() {
		return 5000;
	}
}
