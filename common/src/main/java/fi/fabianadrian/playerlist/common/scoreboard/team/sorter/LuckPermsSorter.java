package fi.fabianadrian.playerlist.common.scoreboard.team.sorter;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;

import java.util.Iterator;

public final class LuckPermsSorter extends Sorter {
	private LuckPerms api = null;
	private final Criteria criteria;

	public LuckPermsSorter(SorterOrder order, Criteria criteria) {
		super(order);
		this.criteria = criteria;

		try {
			this.api = LuckPermsProvider.get();
		} catch (IllegalStateException | NoClassDefFoundError ignored) {
		}
	}

	@Override
	public String output(Audience player) {
		if (this.api == null) {
			return String.format("%03x", this.baseInteger);
		}

		int weight;
		switch (this.criteria) {
			case PREFIX_WEIGHT -> weight = highestPrefixWeight(player);
			case SUFFIX_WEIGHT -> weight = highestSuffixWeight(player);
			case GROUP_WEIGHT -> weight = primaryGroupWeight(player);
			default -> throw new IllegalStateException("Unknown criteria");
		}

		switch (this.order) {
			case ASCENDING -> {
				return String.format("%03x", this.baseInteger + weight);
			}
			case DESCENDING -> {
				return String.format("%03x", this.baseInteger - weight);
			}
			default -> throw new IllegalStateException("Unknown order");
		}
	}

	private int highestPrefixWeight(Audience player) {
		User user = user(player);
		if (user == null) {
			return 0;
		}

		Iterator<Integer> prefixWeights = user.getCachedData().getMetaData().getPrefixes().keySet().iterator();

		int highestWeight = 0;
		if (prefixWeights.hasNext()) {
			highestWeight = prefixWeights.next();
		}

		return highestWeight;
	}

	private int highestSuffixWeight(Audience player) {
		User user = user(player);
		if (user == null) {
			return 0;
		}

		Iterator<Integer> weights = user.getCachedData().getMetaData().getSuffixes().keySet().iterator();

		int highestWeight = 0;
		if (weights.hasNext()) {
			highestWeight = weights.next();
		}

		return highestWeight;
	}

	private int primaryGroupWeight(Audience player) {
		User user = user(player);
		if (user == null) {
			return 0;
		}

		String primaryGroupName = user.getPrimaryGroup();

		Group primaryGroup = this.api.getGroupManager().getGroup(primaryGroupName);
		if (primaryGroup == null) {
			return 0;
		}

		return primaryGroup.getWeight().orElse(0);
	}

	private User user(Audience player) {
		return player.get(Identity.UUID).map(uuid -> this.api.getUserManager().getUser(uuid)).orElse(null);
	}

	public enum Criteria {
		PREFIX_WEIGHT, SUFFIX_WEIGHT, GROUP_WEIGHT
	}
}
