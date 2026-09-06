package fi.fabianadrian.playerlist.config.sorter;

public interface PlaceholderSorterConfig extends SorterConfig {
	@Override
	default SorterType type() {
		return SorterType.PLACEHOLDER;
	}

	String placeholder();

	default boolean caseSensitive() {
		return false;
	}
}
