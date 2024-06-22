package fi.fabianadrian.playerlist.common.configuration.section.scoreboard;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

public interface ScoreboardConfigurationSection {
	@AnnotationBasedSorter.Order(0)
	@ConfDefault.DefaultBoolean(false)
	boolean enabled();

	@AnnotationBasedSorter.Order(1)
	@SubSection
	ScoreboardTeamConfigurationSection team();
}
