package fi.fabianadrian.playerlist.common.playerlist;

import fi.fabianadrian.playerlist.common.PlayerList;
import fi.fabianadrian.playerlist.common.configuration.section.PlayerListConfigurationSection;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class PlayerListManager {
	private final PlayerList playerList;
	protected final MiniMessage miniMessage = MiniMessage.miniMessage();
	protected PlayerListConfigurationSection configuration;

	public PlayerListManager(PlayerList playerList) {
		this.playerList = playerList;
		this.playerList.scheduledExecutor().scheduleAtFixedRate(() ->
				this.playerList.platform().onlinePlayers().thenAcceptAsync(
						players -> players.forEach(this::update),
						this.playerList.scheduledExecutor()
				), 0, 5, TimeUnit.SECONDS
		);
	}

	public void reload() {
		this.configuration = this.playerList.configuration().playerList();
	}

	public void update(Audience player) {
		TagResolver miniPlaceholdersResolver = MiniPlaceholders.getAudienceGlobalPlaceholders(player);

		// Header and footer
		List<Component> headerLines = new ArrayList<>();
		this.configuration.header().forEach(line -> headerLines.add(this.miniMessage.deserialize(line, miniPlaceholdersResolver)));
		List<Component> footerLines = new ArrayList<>();
		this.configuration.footer().forEach(line -> footerLines.add(this.miniMessage.deserialize(line, miniPlaceholdersResolver)));
		player.sendPlayerListHeaderAndFooter(
				Component.join(JoinConfiguration.newlines(), headerLines),
				Component.join(JoinConfiguration.newlines(), footerLines)
		);

		updateCustomName(player);
	}

	public abstract void updateCustomName(Audience player);
}
