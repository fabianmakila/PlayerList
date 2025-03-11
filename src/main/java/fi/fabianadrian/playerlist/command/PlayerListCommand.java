package fi.fabianadrian.playerlist.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fi.fabianadrian.playerlist.PlayerList;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.configurate.ConfigurateException;

public final class PlayerListCommand {
	private static final Component COMPONENT_RELOAD_FAILED = Component.translatable()
			.key("playerlist.command.reload.failed")
			.color(NamedTextColor.RED)
			.build();
	private static final Component COMPONENT_RELOAD_SUCCESS = Component.translatable()
			.key("playerlist.command.reload.success")
			.color(NamedTextColor.GREEN)
			.build();
	private final PlayerList plugin;

	public PlayerListCommand(PlayerList plugin) {
		this.plugin = plugin;
	}

	public LiteralCommandNode<CommandSourceStack> command() {
		return Commands.literal("playerlist")
				.then(Commands.literal("reload").executes(ctx -> {
					try {
						this.plugin.reload();
						ctx.getSource().getSender().sendMessage(COMPONENT_RELOAD_SUCCESS);
					} catch (ConfigurateException e) {
						this.plugin.getSLF4JLogger().error("Couldn't load configuration", e);
						ctx.getSource().getSender().sendMessage(COMPONENT_RELOAD_FAILED);
					}
					return Command.SINGLE_SUCCESS;
				}))
				.build();
	}
}
