package fi.fabianadrian.playerlist.config.group;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public final class ExampleDefaultGroupConfig implements GroupConfig {
	@Override
	public Pattern regex() {
		return Pattern.compile(".*");
	}

	@Override
	public Optional<List<String>> header() {
		return Optional.of(List.of(
				"<green>This is an example group that applies to all worlds",
				"It is useful to have a default group like this, so you don't",
				"need to repeat same settings for each world"
		));
	}

	@Override
	public Optional<List<String>> footer() {
		return Optional.of(List.of(
				"<rainbow>Every string in a group config supports MiniMessage and MiniPlaceholders",
				"<#04c2ff>The player-list-name in this example uses MiniPlaceholders to display a player's prefix from LuckPerms",
				"If you don't want a header, footer or a custom player list name, you can delete the whole option from the group"
		));
	}

	@Override
	public Optional<String> playerListName() {
		return Optional.of("<luckperms_prefix><player_name>");
	}
}
