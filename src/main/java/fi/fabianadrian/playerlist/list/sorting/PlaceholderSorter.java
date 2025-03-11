package fi.fabianadrian.playerlist.list.sorting;

import fi.fabianadrian.playerlist.PlayerList;
import io.github.miniplaceholders.api.MiniPlaceholders;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.Comparator;

public final class PlaceholderSorter extends Sorter {
	private final MiniMessage miniMessage = MiniMessage.builder().build();
	private final String placeholder;

	public PlaceholderSorter(PlayerList plugin, SortingOrder order, String placeholder) {
		super(SorterType.PLACEHOLDER, order);
		this.placeholder = placeholder;

		PluginManager pluginManager = plugin.getServer().getPluginManager();
		switch (placeholder.charAt(0)) {
			case '<' -> {
				if (!pluginManager.isPluginEnabled("MiniPlaceholders")) {
					plugin.getSLF4JLogger().warn("MiniPlaceholders is not enabled. Placeholder sorter for {} will be non-functional", placeholder);
					return;
				}
				this.comparator = Comparator.comparing(this::parseMiniPlaceholders);
			}
			case '%' -> {
				if (!pluginManager.isPluginEnabled("PlaceholderAPI")) {
					plugin.getSLF4JLogger().warn("PlaceholderAPI is not enabled. Placeholder sorter for {} will be non-functional", placeholder);
					return;
				}
				this.comparator = Comparator.comparing(this::parsePlaceholderAPI);
			}
			default ->
					plugin.getSLF4JLogger().warn("{} is neither MiniPlaceholders or PlaceholderAPI placeholder. Sorter will be non-functional", placeholder);
		}
	}

	public String placeholder() {
		return this.placeholder;
	}

	private String parseMiniPlaceholders(Player player) {
		final Component deserialized = this.miniMessage.deserialize(
				this.placeholder,
				MiniPlaceholders.getAudienceGlobalPlaceholders(player)
		);
		return PlainTextComponentSerializer.plainText().serialize(deserialized);
	}

	private String parsePlaceholderAPI(Player player) {
		return PlaceholderAPI.setPlaceholders(player, this.placeholder);
	}
}
