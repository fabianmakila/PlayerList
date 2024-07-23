package fi.fabianadrian.playerlist.common.playerlist;

import fi.fabianadrian.playerlist.common.PlayerList;
import fi.fabianadrian.playerlist.common.configuration.section.PlayerListConfigurationSection;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class PlayerListManager {
	protected final MiniMessage miniMessage = MiniMessage.miniMessage();
	private final PlayerList playerList;
	protected PlayerListConfigurationSection configuration;
	private ScheduledFuture<?> scheduledFuture;

	public PlayerListManager(PlayerList playerList) {
		this.playerList = playerList;
	}

	public void reload() {
		this.configuration = this.playerList.configuration().playerList();

		if (this.scheduledFuture != null) {
			this.scheduledFuture.cancel(false);
		}

		int updateInterval = this.playerList.configuration().placeholderRefreshInterval();
		this.scheduledFuture = this.playerList.scheduledExecutor().scheduleAtFixedRate(() ->
				this.playerList.platform().onlinePlayers().thenAcceptAsync(
						players -> players.forEach(this::update),
						this.playerList.scheduledExecutor()
				), 0, updateInterval, TimeUnit.SECONDS
		);
	}

	public void update(Audience player) {
		List<Component> headerLines = new ArrayList<>();
		List<Component> footerLines = new ArrayList<>();

		if (this.playerList.platform().isMiniplaceholdersAvailable()) {
			this.configuration.header().forEach(line -> headerLines.add(this.miniMessage.deserialize(line, MiniPlaceholders.getAudienceGlobalPlaceholders(player))));
			this.configuration.footer().forEach(line -> footerLines.add(this.miniMessage.deserialize(line, MiniPlaceholders.getAudienceGlobalPlaceholders(player))));
		} else {
			this.configuration.header().forEach(line -> headerLines.add(this.miniMessage.deserialize(line)));
			this.configuration.footer().forEach(line -> footerLines.add(this.miniMessage.deserialize(line)));
		}

		player.sendPlayerListHeaderAndFooter(
				Component.join(JoinConfiguration.newlines(), headerLines),
				Component.join(JoinConfiguration.newlines(), footerLines)
		);

		updateCustomName(player);
	}

	public abstract void updateCustomName(Audience player);
}
