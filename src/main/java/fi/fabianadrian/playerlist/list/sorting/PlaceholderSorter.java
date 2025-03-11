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
import java.util.Locale;

public final class PlaceholderSorter extends Sorter {
	private final MiniMessage miniMessage = MiniMessage.builder().build();
	private final String placeholder;
	private final boolean caseSensitive;

	public PlaceholderSorter(PlayerList plugin, SortingOrder order, String placeholder, boolean caseSensitive) {
		super(SorterType.PLACEHOLDER, order);
		this.placeholder = placeholder;
		this.caseSensitive = caseSensitive;

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

	public boolean caseSensitive() {
		return this.caseSensitive;
	}

	private String parseMiniPlaceholders(Player player) {
		final Component deserialized = this.miniMessage.deserialize(
				this.placeholder,
				MiniPlaceholders.getAudienceGlobalPlaceholders(player)
		);
		String serialized = PlainTextComponentSerializer.plainText().serialize(deserialized);
		if (!this.caseSensitive) {
			serialized = serialized.toLowerCase(Locale.ROOT);
		}
		return serialized;
	}

	private String parsePlaceholderAPI(Player player) {
		String parsed = PlaceholderAPI.setPlaceholders(player, this.placeholder);
		if (!this.caseSensitive) {
			parsed = parsed.toLowerCase(Locale.ROOT);
		}
		return parsed;
	}
}
