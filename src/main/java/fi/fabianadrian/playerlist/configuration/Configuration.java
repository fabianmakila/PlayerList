package fi.fabianadrian.playerlist.configuration;

import fi.fabianadrian.playerlist.list.sorting.LuckPermsSorter;
import fi.fabianadrian.playerlist.list.sorting.Sorter;
import fi.fabianadrian.playerlist.list.sorting.SortingOrder;

import java.util.List;

public class Configuration {
	private List<String> header = List.of();
	private List<String> footer = List.of();
	private String playerListName = "<luckperms_prefix><player_name>";
	private List<Sorter> sorters = List.of(
			new LuckPermsSorter(SortingOrder.DESCENDING, LuckPermsSorter.Criteria.PREFIX_WEIGHT)
	);
	private int placeholderRefreshInterval = 5;

	public List<String> header() {
		return this.header;
	}

	public List<String> footer() {
		return this.footer;
	}

	public String playerListName() {
		return this.playerListName;
	}

	public List<Sorter> sorters() {
		return this.sorters;
	}

	public int placeholderRefreshInterval() {
		return this.placeholderRefreshInterval;
	}
}
