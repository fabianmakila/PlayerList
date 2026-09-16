package fi.fabianadrian.playerlist.list.sorting;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.config.sorter.luckperms.LuckPermsSorterConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.SortedMap;

public final class LuckPermsSorter extends Sorter {
	private final Comparator<Player> comparator;
	private LuckPerms api;

	public LuckPermsSorter(PlayerList plugin, LuckPermsSorterConfig config) {
		super(config.order());
		try {
			this.api = LuckPermsProvider.get();
		} catch (IllegalStateException | NoClassDefFoundError throwable) {
			plugin.getSLF4JLogger().warn("LuckPerms API is unavailable. LuckPerms sorter will be non functional", throwable);
			this.comparator = null;
			return;
		}

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
		SortedMap<Integer, String> prefixes = user(player).getCachedData().getMetaData().getPrefixes();
		if (prefixes.isEmpty()) {
			return 0;
		}
		return prefixes.firstKey();
	}

	private int highestSuffixWeight(Player player) {
		SortedMap<Integer, String> suffixes = user(player).getCachedData().getMetaData().getSuffixes();
		if (suffixes.isEmpty()) {
			return 0;
		}

		return suffixes.firstKey();
	}

	private int primaryGroupWeight(Player player) {
		Group primaryGroup = this.api.getGroupManager().getGroup(user(player).getPrimaryGroup());
		if (primaryGroup == null) {
			return 0;
		}

		return primaryGroup.getWeight().orElse(0);
	}

	private User user(Player player) {
		return this.api.getPlayerAdapter(Player.class).getUser(player);
	}
}
