package fi.fabianadrian.playerlist.common.configuration;

import fi.fabianadrian.playerlist.common.configuration.section.scoreboard.ScoreboardConfigurationSection;
import fi.fabianadrian.playerlist.common.configuration.section.PlayerListConfigurationSection;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

public interface MainConfiguration {
	@SubSection
	@AnnotationBasedSorter.Order(0)
	PlayerListConfigurationSection playerList();

	@SubSection
	@AnnotationBasedSorter.Order(1)
	ScoreboardConfigurationSection scoreBoard();

	@AnnotationBasedSorter.Order(2)
	@ConfDefault.DefaultInteger(5)
	int placeholderRefreshInterval();
}
