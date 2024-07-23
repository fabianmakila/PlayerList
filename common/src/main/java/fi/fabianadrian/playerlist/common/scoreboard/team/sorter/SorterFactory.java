package fi.fabianadrian.playerlist.common.scoreboard.team.sorter;

import fi.fabianadrian.playerlist.common.PlayerList;

public final class SorterFactory {
	private PlayerList playerList;

	public SorterFactory(PlayerList playerList) {
		this.playerList = playerList;
	}

	public Sorter sorter(String serialized) {
		String[] split = serialized.split(":");
		switch (split[0].toLowerCase()) {
			case "luckperms" -> {
				return luckPermsSorter(split);
			}
			case "placeholder" -> {
				return placeholderSorter(split);
			}
			default -> throw new IllegalStateException("Unknown sorter type");
		}
	}

	private LuckPermsSorter luckPermsSorter(String[] split) {
		SorterOrder order = SorterOrder.valueOf(split[1].toUpperCase());
		LuckPermsSorter.Criteria criteria = LuckPermsSorter.Criteria.valueOf(split[2].toUpperCase());
		return new LuckPermsSorter(order, criteria);
	}

	private PlaceholderSorter placeholderSorter(String[] split) {
		SorterOrder order = SorterOrder.valueOf(split[1].toUpperCase());
		String placeholder = split[2];
		return new PlaceholderSorter(order, placeholder, this.playerList.platform().isMiniplaceholdersAvailable());
	}
}
