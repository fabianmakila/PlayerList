package fi.fabianadrian.playerlist;

import net.kyori.adventure.text.Component;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.permission.Permission;
import org.bukkit.command.CommandSender;
import org.spongepowered.configurate.ConfigurateException;

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
		} catch (ConfigurateException e) {
			this.plugin.getSLF4JLogger().error("Couldn't load configuration", e);
			sender.sendMessage(COMPONENT_RELOAD_FAILED);
		}
	}
}
