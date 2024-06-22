package fi.fabianadrian.playerlist.paper.listener;

import fi.fabianadrian.playerlist.paper.PlayerListPaper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerListener implements Listener {
	private final PlayerListPaper plugin;

	public PlayerListener(PlayerListPaper plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.plugin.playerListManager().update(event.getPlayer());
		this.plugin.scoreboardManager().register(event.getPlayer());
	}
}
