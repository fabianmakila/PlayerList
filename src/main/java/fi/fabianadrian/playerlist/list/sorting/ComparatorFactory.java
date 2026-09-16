package fi.fabianadrian.playerlist.list.sorting;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.config.sorter.PlaceholderSorterConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.luckperms.LuckPermsSorterConfig;
import fi.fabianadrian.playerlist.config.sorter.player.PlayerSorterConfig;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class ComparatorFactory {
	private final PlayerList plugin;

	public ComparatorFactory(PlayerList plugin) {
		this.plugin = plugin;
	}

	public Comparator<Player> comparator(List<SorterConfig> configs) {
		return configs.stream()
				.map(this::comparator)
				.filter(Objects::nonNull)
				.reduce(Comparator::thenComparing)
				.orElse((_, _) -> 0);
	}

	private Comparator<Player> comparator(SorterConfig config) {
		if (config instanceof PlaceholderSorterConfig placeholderSorterConfig) {
			return new PlaceholderSorter(this.plugin, placeholderSorterConfig).build();
		}
		if (config instanceof LuckPermsSorterConfig luckPermsSorterConfig) {
			return new LuckPermsSorter(luckPermsSorterConfig).build();
		}
		if (config instanceof PlayerSorterConfig playerSorterConfig) {
			return new PlayerSorter(playerSorterConfig).build();
		}
		throw new IllegalStateException("Unknown sorter config type");
	}
}
