package fi.fabianadrian.playerlist.config.group;

import space.arim.dazzleconf.engine.Comments;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public final class ExampleNetherOverrideConfig implements GroupConfig {
	@Override
	@Comments("This group only applies to a world named \"world_nether\"")
	public Pattern regex() {
		return Pattern.compile("world_nether");
	}

	@Override
	public Optional<List<String>> header() {
		return Optional.of(List.of(
				"<red>This header is only visible in the Nether",
				"<red>and overrides the header configured above"
		));
	}

	@Override
	public Optional<List<String>> footer() {
		return Optional.empty();
	}

	@Override
	public Optional<String> playerListName() {
		return Optional.empty();
	}
}
