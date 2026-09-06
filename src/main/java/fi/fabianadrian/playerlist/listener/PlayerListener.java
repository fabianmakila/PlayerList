package fi.fabianadrian.playerlist.listener;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.list.ListManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerListener implements Listener {
	private final ListManager listManager;

	public PlayerListener(PlayerList plugin) {
		this.listManager = plugin.playerListManager();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.listManager.sort(); //TODO Should this be delayed couple of ticks (due to placeholder sorter etc)?
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.listManager.untrack(event.getPlayer());
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		this.listManager.track(event.getPlayer());
	}
}
