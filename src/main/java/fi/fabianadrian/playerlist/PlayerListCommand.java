package fi.fabianadrian.playerlist;

import fi.fabianadrian.playerlist.config.ConfigLoadException;
import net.kyori.adventure.text.Component;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.permission.Permission;
import org.bukkit.command.CommandSender;

@Command("playerlist")
public final class PlayerListCommand {
	private static final Component COMPONENT_RELOAD_FAILED = Component.translatable("playerlist.command.reload.failed");
	private static final Component COMPONENT_RELOAD_SUCCESS = Component.translatable("playerlist.command.reload.success");
	private final PlayerList plugin;

	public PlayerListCommand(PlayerList plugin) {
		this.plugin = plugin;
	}

	@Executes("reload")
	@Permission("playerlist.command.reload")
	void onReload(CommandSender sender) {
		try {
			this.plugin.load();
			sender.sendMessage(COMPONENT_RELOAD_SUCCESS);
		} catch (ConfigLoadException e) {
			this.plugin.getSLF4JLogger().error("Couldn't load configuration", e);
			sender.sendMessage(COMPONENT_RELOAD_FAILED);
		}
	}
}
