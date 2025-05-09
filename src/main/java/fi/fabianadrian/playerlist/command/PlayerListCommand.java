package fi.fabianadrian.playerlist.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fi.fabianadrian.playerlist.PlayerList;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.ConfigurateException;

public final class PlayerListCommand {
	private static final Component COMPONENT_RELOAD_FAILED = Component.translatable("playerlist.command.reload.failed");
	private static final Component COMPONENT_RELOAD_SUCCESS = Component.translatable("playerlist.command.reload.success");
	private static final String PERMISSION_RELOAD = "playerlist.command.reload";
	private final PlayerList plugin;

	public PlayerListCommand(PlayerList plugin) {
		this.plugin = plugin;
	}

	public LiteralCommandNode<CommandSourceStack> command() {
		return Commands.literal("playerlist")
				.then(Commands.literal("reload")
						.requires(stack -> stack.getSender().hasPermission(PERMISSION_RELOAD))
						.executes(ctx -> {
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
