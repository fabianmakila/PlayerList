package fi.fabianadrian.playerlist.list;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.configuration.Configuration;
import fi.fabianadrian.playerlist.list.sorting.Sorter;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ListManager {
	private final MiniMessage miniMessage = MiniMessage.miniMessage();
	private final PlayerList playerList;
	private ScheduledFuture<?> scheduledFuture;
	private Comparator<Player> comparator;
	private boolean isMiniPlaceholdersAvailable = false;

	public ListManager(PlayerList playerList) {
		this.playerList = playerList;
	}

	public void reload() {
		if (this.scheduledFuture != null) {
			this.scheduledFuture.cancel(false);
		}

		this.isMiniPlaceholdersAvailable = this.playerList.getServer().getPluginManager().isPluginEnabled("MiniPlaceholders");

		int refreshInterval = this.playerList.configuration().placeholderRefreshInterval();
		this.scheduledFuture = this.playerList.executorService().scheduleAtFixedRate(
				() -> this.playerList.onlinePlayers().thenAccept(players -> {
					players.forEach(this::update);
					updateOrder(players);
				}),
				0,
				refreshInterval,
				TimeUnit.SECONDS
		);

		constructComparatorFromSorters();
	}

	public void update(Player player) {
		List<Component> headerLines = new ArrayList<>();
		List<Component> footerLines = new ArrayList<>();

		Configuration configuration = this.playerList.configuration();

		if (this.isMiniPlaceholdersAvailable) {
			TagResolver miniPlaceholdersResolver = MiniPlaceholders.getAudienceGlobalPlaceholders(player);
			configuration.header().forEach(line -> headerLines.add(this.miniMessage.deserialize(line, miniPlaceholdersResolver)));
			configuration.footer().forEach(line -> footerLines.add(this.miniMessage.deserialize(line, miniPlaceholdersResolver)));
		} else {
			configuration.header().forEach(line -> headerLines.add(this.miniMessage.deserialize(line)));
			configuration.footer().forEach(line -> footerLines.add(this.miniMessage.deserialize(line)));
		}

		player.sendPlayerListHeaderAndFooter(
				Component.join(JoinConfiguration.newlines(), headerLines),
				Component.join(JoinConfiguration.newlines(), footerLines)
		);

		updateCustomName(player);
	}

	public void updateOrder(List<Player> players) {
		if (this.comparator == null) {
			return;
		}

		players.sort(this.comparator);

		for (int i = 0; i < players.size(); i++) {
			players.get(i).setPlayerListOrder(i);
		}
	}

	private void updateCustomName(Player player) {
		String nameString = this.playerList.configuration().playerListName();

		if (nameString.isBlank()) {
			player.playerListName(null);
			return;
		}

		Component nameComponent;
		if (this.isMiniPlaceholdersAvailable) {
			nameComponent = this.miniMessage.deserialize(
					nameString,
					MiniPlaceholders.getAudienceGlobalPlaceholders(player)
			);
		} else {
			nameComponent = this.miniMessage.deserialize(nameString);
		}

		player.playerListName(nameComponent);
	}

	private void constructComparatorFromSorters() {
		Comparator<Player> finalComparator = null;

		for (Sorter sorter : this.playerList.configuration().sorters()) {
			Comparator<Player> currentComparator = sorter.comparator();
			finalComparator = finalComparator == null
					? currentComparator
					: finalComparator.thenComparing(currentComparator);
		}

		this.comparator = finalComparator;
	}
}
