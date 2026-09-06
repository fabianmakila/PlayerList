package fi.fabianadrian.playerlist.config.group;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public interface GroupConfig {
	Pattern regex();

	Optional<List<String>> header();

	Optional<List<String>> footer();

	Optional<String> playerListName();
}
