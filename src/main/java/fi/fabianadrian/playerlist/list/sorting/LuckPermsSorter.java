package fi.fabianadrian.playerlist.list.sorting;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Iterator;

public final class LuckPermsSorter extends Sorter {
	private final Criteria criteria;
	private LuckPerms api = null;

	public LuckPermsSorter(SortingOrder order, Criteria criteria) {
		super(SorterType.LUCKPERMS, order);
		super.comparator = Comparator.comparingInt(this::weight);
		this.criteria = criteria;

		try {
			this.api = LuckPermsProvider.get();
		} catch (IllegalStateException | NoClassDefFoundError ignored) {
		}
	}

	public Criteria criteria() {
		return this.criteria;
	}

	private int weight(Player player) {
		if (this.api == null) {
			return 0;
		}

		int weight;
		switch (this.criteria) {
			case PREFIX_WEIGHT -> weight = highestPrefixWeight(player);
			case SUFFIX_WEIGHT -> weight = highestSuffixWeight(player);
			case GROUP_WEIGHT -> weight = primaryGroupWeight(player);
			default -> throw new IllegalStateException("Unknown criteria");
		}

		return weight;
	}

	private int highestPrefixWeight(Player player) {
		User user = user(player);

		Iterator<Integer> prefixWeights = user.getCachedData().getMetaData().getPrefixes().keySet().iterator();

		int highestWeight = 0;
		if (prefixWeights.hasNext()) {
			highestWeight = prefixWeights.next();
		}

		return highestWeight;
	}

	private int highestSuffixWeight(Player player) {
		User user = user(player);

		Iterator<Integer> weights = user.getCachedData().getMetaData().getSuffixes().keySet().iterator();

		int highestWeight = 0;
		if (weights.hasNext()) {
			highestWeight = weights.next();
		}

		return highestWeight;
	}

	private int primaryGroupWeight(Player player) {
		User user = user(player);

		String primaryGroupName = user.getPrimaryGroup();

		Group primaryGroup = this.api.getGroupManager().getGroup(primaryGroupName);
		if (primaryGroup == null) {
			return 0;
		}

		return primaryGroup.getWeight().orElse(0);
	}

	private User user(Player player) {
		return this.api.getPlayerAdapter(Player.class).getUser(player);
	}

	public enum Criteria {
		PREFIX_WEIGHT, SUFFIX_WEIGHT, GROUP_WEIGHT
	}
}
