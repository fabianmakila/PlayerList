package fi.fabianadrian.playerlist.config.sorter;

public interface PlaceholderSorterConfig extends SorterConfig {
	String placeholder();

	default boolean caseSensitive() {
		return false;
	}
}
