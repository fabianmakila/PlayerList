package fi.fabianadrian.playerlist.list.sorter;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.config.sorter.PlaceholderSorterConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.luckperms.LuckPermsSorterConfig;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public final class ComparatorFactory {
	private final PlayerList plugin;

	public ComparatorFactory(PlayerList plugin) {
		this.plugin = plugin;
	}

	public Comparator<Player> comparator(List<SorterConfig> configs) {
		return configs.stream()
				.map(this::comparator)
				.reduce(Comparator::thenComparing)
				.orElse((a, b) -> 0);
	}

	private Comparator<Player> comparator(SorterConfig config) {
		if (config instanceof PlaceholderSorterConfig placeholderSorterConfig) {
			return new PlaceholderSorter(this.plugin, placeholderSorterConfig);
		}
		if (config instanceof LuckPermsSorterConfig luckPermsSorterConfig) {
			return new LuckPermsSorter(luckPermsSorterConfig);
		}
		throw new IllegalStateException("Unknown sorter config type");
	}
}
