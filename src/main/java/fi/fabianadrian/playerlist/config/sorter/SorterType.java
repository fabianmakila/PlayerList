package fi.fabianadrian.playerlist.config.sorter;

import fi.fabianadrian.playerlist.config.sorter.luckperms.LuckPermsSorterConfig;
import fi.fabianadrian.playerlist.config.sorter.player.PlayerSorterConfig;
import space.arim.dazzleconf.reflect.TypeToken;

public enum SorterType {
	PLACEHOLDER(new TypeToken<PlaceholderSorterConfig>() {
	}),
	PLAYER(new TypeToken<PlayerSorterConfig>() {
	}),
	LUCKPERMS(new TypeToken<LuckPermsSorterConfig>() {
	});

	private final TypeToken<? extends SorterConfig> typeToken;

	SorterType(TypeToken<? extends SorterConfig> typeToken) {
		this.typeToken = typeToken;
	}

	public TypeToken<? extends SorterConfig> typeToken() {
		return typeToken;
	}
}
