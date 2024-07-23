package fi.fabianadrian.playerlist.common.platform;

import fi.fabianadrian.playerlist.common.playerlist.PlayerListManager;
import net.kyori.adventure.audience.Audience;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Platform<P extends Audience> {
	CompletableFuture<List<P>> onlinePlayers();

	Path dataFolder();

	Logger logger();

	PlayerListManager playerListManager();

	boolean isMiniplaceholdersAvailable();
}
