package fi.fabianadrian.playerlist.listener;

import fi.fabianadrian.playerlist.PlayerList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public final class PlayerListener implements Listener {
	private final PlayerList plugin;

	public PlayerListener(PlayerList plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.plugin.playerListManager().update(event.getPlayer());
		this.plugin.playerListManager().updateOrder(new ArrayList<>(this.plugin.getServer().getOnlinePlayers()));
	}
}
