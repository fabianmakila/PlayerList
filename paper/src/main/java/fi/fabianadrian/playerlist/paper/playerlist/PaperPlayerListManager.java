package fi.fabianadrian.playerlist.paper.playerlist;

import fi.fabianadrian.playerlist.common.PlayerList;
import fi.fabianadrian.playerlist.common.playerlist.PlayerListManager;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

public final class PaperPlayerListManager extends PlayerListManager {
	public PaperPlayerListManager(PlayerList playerList) {
		super(playerList);
	}

	@Override
	public void updateCustomName(Audience player) {
		String playerListName = this.configuration.playerListName();

		Player paperPlayer = (Player) player;

		if (!playerListName.isBlank()) {
			paperPlayer.playerListName(this.miniMessage.deserialize(
					playerListName,
					MiniPlaceholders.getAudienceGlobalPlaceholders(player)
			));
		} else {
			paperPlayer.playerListName(null);
		}
	}
}
