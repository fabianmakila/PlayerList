package fi.fabianadrian.playerlist.list.sorting;

import fi.fabianadrian.playerlist.config.sorter.luckperms.LuckPermsSorterConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.SortedMap;

public final class LuckPermsSorter extends Sorter {
	private final Comparator<Player> comparator;
	private final PlayerAdapter<Player> adapter;
	private final GroupManager groupManager;

	public LuckPermsSorter(LuckPermsSorterConfig config) {
		super(config.order());

		LuckPerms api = LuckPermsProvider.get();
		this.adapter = api.getPlayerAdapter(Player.class);
		this.groupManager = api.getGroupManager();

		switch (config.criteria()) {
			case PREFIX_WEIGHT -> this.comparator = Comparator.comparingInt(this::highestPrefixWeight);
			case SUFFIX_WEIGHT -> this.comparator = Comparator.comparingInt(this::highestSuffixWeight);
			case GROUP_WEIGHT -> this.comparator = Comparator.comparingInt(this::primaryGroupWeight);
			default -> throw new IllegalStateException("Unknown criteria");
		}
	}

	@Override
	protected Comparator<Player> comparator() {
		return this.comparator;
	}

	private int highestPrefixWeight(Player player) {
		SortedMap<Integer, String> prefixes = this.adapter.getUser(player).getCachedData().getMetaData().getPrefixes();
		return prefixes.isEmpty() ? 0 : prefixes.firstKey();
	}

	private int highestSuffixWeight(Player player) {
		SortedMap<Integer, String> suffixes = this.adapter.getUser(player).getCachedData().getMetaData().getSuffixes();
		return suffixes.isEmpty() ? 0 : suffixes.firstKey();

	}

	private int primaryGroupWeight(Player player) {
		Group primaryGroup = this.groupManager.getGroup(this.adapter.getUser(player).getPrimaryGroup());
		return primaryGroup == null ? 0 : primaryGroup.getWeight().orElse(0);

	}
}
