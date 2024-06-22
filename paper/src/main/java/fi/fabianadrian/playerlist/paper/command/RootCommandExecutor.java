package fi.fabianadrian.playerlist.paper.command;

import fi.fabianadrian.playerlist.paper.PlayerListPaper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RootCommandExecutor implements CommandExecutor, TabExecutor {
	private final PlayerListPaper plugin;

	public RootCommandExecutor(PlayerListPaper plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			return false;
		}

		if ("reload".equalsIgnoreCase(args[0])) {
			this.plugin.reload();
			sender.sendMessage(Component.text("Reload complete!", NamedTextColor.GREEN));
			return true;
		}

		return false;
	}

	@Override
	public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return List.of("reload");
	}
}
