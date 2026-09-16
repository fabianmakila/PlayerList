package fi.fabianadrian.playerlist.list.sorting;

import fi.fabianadrian.playerlist.config.sorter.player.PlayerSorterConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Comparator;

public final class PlayerSorter extends Sorter {
	private final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
	private final Comparator<Player> comparator;

	public PlayerSorter(PlayerSorterConfig config) {
		super(config.order());
		switch (config.criteria()) {
			case NAME -> this.comparator = Comparator.comparing(Player::getName);
			case DISPLAYNAME ->
					this.comparator = Comparator.comparing(player -> serializer.serialize(player.displayName()));
			default -> throw new IllegalStateException("Unknown criteria");
		}
	}

	@Override
	protected Comparator<Player> comparator() {
		return this.comparator;
	}
}
