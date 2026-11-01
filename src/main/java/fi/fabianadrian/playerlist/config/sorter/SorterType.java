package fi.fabianadrian.playerlist.config.sorter;

import fi.fabianadrian.playerlist.config.sorter.luckperms.LuckPermsSorterConfig;
import fi.fabianadrian.playerlist.config.sorter.player.PlayerSorterConfig;

import java.util.Arrays;

public enum SorterType {
	PLACEHOLDER(PlaceholderSorterConfig.class),
	PLAYER(PlayerSorterConfig.class),
	LUCKPERMS(LuckPermsSorterConfig.class);

	private final Class<? extends SorterConfig> sorterClass;

	SorterType(Class<? extends SorterConfig> sorterClass) {
		this.sorterClass = sorterClass;
	}

	public static SorterType fromInstance(SorterConfig config) {
		for (SorterType type : values()) {
			if (type.sorterClass.isInstance(config)) {
				return type;
			}
		}
		throw new IllegalStateException("unknown sorter class: " + config.getClass()
				+ ", super=" + config.getClass().getSuperclass()
				+ ", interfaces=" + Arrays.toString(config.getClass().getInterfaces())
				+ ", loader=" + config.getClass().getClassLoader());
	}

	public Class<? extends SorterConfig> sorterClass() {
		return this.sorterClass;
	}
}
