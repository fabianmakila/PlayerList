package fi.fabianadrian.playerlist.config.group;

import space.arim.dazzleconf.engine.Comments;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public final class ExampleGroupConfig implements GroupConfig {
	@Comments("The regex below matches every world")
	@Override
	public Pattern regex() {
		return Pattern.compile(".*");
	}

	@Override
	public Optional<List<String>> header() {
		return Optional.of(List.of("<blue>This is an example", "<red>header with 2 lines"));
	}

	@Override
	public Optional<List<String>> footer() {
		return Optional.of(List.of("<rainbow>This is a footer"));
	}

	@Override
	public Optional<String> playerListName() {
		return Optional.of("<luckperms_prefix><player_name>");
	}
}
