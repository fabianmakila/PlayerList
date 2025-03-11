package fi.fabianadrian.playerlist.configuration;

import fi.fabianadrian.playerlist.list.sorting.LuckPermsSorter;
import fi.fabianadrian.playerlist.list.sorting.Sorter;
import fi.fabianadrian.playerlist.list.sorting.SortingOrder;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;

public class Configuration {
	private List<String> header = List.of(
			"<blue>This is an example",
			"<red>header with 2 lines"
	);
	private List<String> footer = List.of(
			"<rainbow>This is a footer"
	);
	@Comment("""
			The example uses MiniPlaceholders to display a prefix.
			Set to "" to use the vanilla name.
			""")
	private String playerListName = "<luckperms_prefix><player_name>";
	@Comment("Please refer to the wiki on how to use the sorters.")
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
