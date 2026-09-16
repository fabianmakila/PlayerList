package fi.fabianadrian.playerlist.listener;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.list.ListManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public final class WorldListener implements Listener {
	private final ListManager listManager;

	public WorldListener(PlayerList plugin) {
		this.listManager = plugin.playerListManager();
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		this.listManager.loadWorld(event.getWorld());
	}

	@EventHandler
	public void onWorldUnload(WorldUnloadEvent event) {
		this.listManager.unloadWorld(event.getWorld());
	}
}
