package fi.fabianadrian.playerlist.common.configuration.section;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.util.List;

public interface PlayerListConfigurationSection {
	@AnnotationBasedSorter.Order(0)
	@ConfDefault.DefaultStrings({})
	List<String> header();

	@AnnotationBasedSorter.Order(1)
	@ConfDefault.DefaultStrings({})
	List<String> footer();

	@AnnotationBasedSorter.Order(2)
	@ConfDefault.DefaultString("")
	String playerListName();
}
