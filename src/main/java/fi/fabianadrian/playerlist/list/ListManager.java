package fi.fabianadrian.playerlist.list;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.config.Config;
import fi.fabianadrian.playerlist.config.group.GroupConfig;
import fi.fabianadrian.playerlist.list.sorter.ComparatorFactory;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ListManager {
	private final MiniMessage miniMessage;
	private final PlayerList plugin;
	private final Map<UUID, ListSettings> worldSettingsMap = new HashMap<>();
	private final Map<Player, ListSettings> playerSettingsMap = new HashMap<>();
	private final ComparatorFactory sorterFactory;
	private ScheduledFuture<?> scheduledFuture;
	private Comparator<Player> comparator;
	private boolean isMiniPlaceholdersAvailable = false;

	public ListManager(PlayerList plugin) {
		this.plugin = plugin;
		this.miniMessage = plugin.miniMessage();
		this.sorterFactory = new ComparatorFactory(plugin);
	}

	public void load() {
		if (this.scheduledFuture != null) {
			this.scheduledFuture.cancel(false);
		}

		Config config = this.plugin.config();
		this.comparator = this.sorterFactory.comparator(config.sorters());

		this.worldSettingsMap.clear();
		this.plugin.getServer().getWorlds().forEach(world -> {
			List<String> header = List.of();
			List<String> footer = List.of();
			String playerListName = null;

			for (GroupConfig group : config.groups().values()) {
				if (!group.regex().matcher(world.getName()).matches()) {
					continue;
				}
				header = group.header().orElse(header);
				footer = group.footer().orElse(footer);
				playerListName = group.playerListName().orElse(playerListName);
			}
			this.worldSettingsMap.put(world.getUID(), new ListSettings(header, footer, playerListName));
		});

		this.isMiniPlaceholdersAvailable = this.plugin.getServer().getPluginManager().isPluginEnabled("MiniPlaceholders");

		this.scheduledFuture = this.plugin.executorService().scheduleAtFixedRate(
				() -> Bukkit.getScheduler().runTask(this.plugin, () -> {
					this.playerSettingsMap.forEach(this::update);
					sort();
				}),
				0,
				config.placeholderRefreshInterval(),
				TimeUnit.SECONDS
		);
	}

	// Called when a player joins / changes world
	public void track(Player player) {
		ListSettings settings = this.worldSettingsMap.get(player.getWorld().getUID());
		if (settings == null) {
			if (this.playerSettingsMap.remove(player) != null) {
				player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty());
				player.playerListName(null);
			}
		} else {
			this.playerSettingsMap.put(player, settings);
			update(player, settings);
		}
	}

	// Called when player disconnects
	public void untrack(Player player) {
		this.playerSettingsMap.remove(player);
	}

	public void sort() {
		List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
		players.sort(this.comparator);
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setPlayerListOrder(i);
		}
	}

	private void update(Player player, ListSettings settings) {
		TagResolver miniPlaceholdersResolver = this.isMiniPlaceholdersAvailable ? MiniPlaceholders.audienceGlobalPlaceholders() : TagResolver.empty();

		Component header;
		if (settings.header().isEmpty()) {
			header = Component.empty();
		} else {
			List<Component> headerLines = new ArrayList<>();
			settings.header().forEach(line -> headerLines.add(this.miniMessage.deserialize(line, player, miniPlaceholdersResolver)));
			header = Component.join(JoinConfiguration.newlines(), headerLines);
		}

		Component footer;
		if (settings.header().isEmpty()) {
			footer = Component.empty();
		} else {
			List<Component> footerLines = new ArrayList<>();
			settings.footer().forEach(line -> footerLines.add(this.miniMessage.deserialize(line, player, miniPlaceholdersResolver)));
			footer = Component.join(JoinConfiguration.newlines(), footerLines);
		}
		player.sendPlayerListHeaderAndFooter(header, footer);

		if (settings.name() == null) {
			player.playerListName(null);
		} else {
			player.playerListName(this.miniMessage.deserialize(
					settings.name(),
					player,
					MiniPlaceholders.audienceGlobalPlaceholders()
			));
		}
	}
}
