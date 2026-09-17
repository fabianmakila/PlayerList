package fi.fabianadrian.playerlist.config.group;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public final class ExampleNetherOverrideConfig implements GroupConfig {
	@Override
	public Pattern regex() {
		return Pattern.compile("world_nether");
	}

	@Override
	public Optional<List<String>> header() {
		return Optional.of(List.of(
				"<red>This header is only visible in the Nether!",
				"<yellow>Do note, that all the other options are still inherited from",
				"<yellow>the group above since we didn't define those here"
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
