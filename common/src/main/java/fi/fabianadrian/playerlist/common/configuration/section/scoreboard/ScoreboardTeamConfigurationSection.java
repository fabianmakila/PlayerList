package fi.fabianadrian.playerlist.common.configuration.section.scoreboard;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.util.List;

public interface ScoreboardTeamConfigurationSection {
	@AnnotationBasedSorter.Order(0)
	@ConfDefault.DefaultString("<luckperms_prefix>")
	String prefix();

	@AnnotationBasedSorter.Order(1)
	@ConfDefault.DefaultString("<luckperms_suffix>")
	String suffix();

	@AnnotationBasedSorter.Order(2)
	@ConfDefault.DefaultStrings({"LUCKPERMS:DESCENDING:PREFIX_WEIGHT"})
	List<String> sorters();

	@AnnotationBasedSorter.Order(3)
	@ConfDefault.DefaultBoolean(true)
	boolean collisions();

	@AnnotationBasedSorter.Order(4)
	@ConfDefault.DefaultBoolean(true)
	boolean deathMessages();

	@AnnotationBasedSorter.Order(5)
	@ConfDefault.DefaultBoolean(true)
	boolean nameTags();
}
