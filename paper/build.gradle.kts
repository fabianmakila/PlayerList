plugins {
	id("playerlist.platform-conventions")
	alias(libs.plugins.pluginYml)
}

dependencies {
	compileOnly(libs.platform.paper)
	compileOnly(libs.plugin.luckperms)
}

bukkit {
	main = "fi.fabianadrian.playerlist.paper.PlayerListPaper"
	name = rootProject.name
	apiVersion = "1.17"
	authors = listOf("FabianAdrian")
	website = "https://github.com/fabianmakila/playerlist"
	softDepend = listOf("MiniPlaceholders", "LuckPerms")
	commands {
		register("playerlist") {
			description = "PlayerList main command"
			permission = "playerlist.reload"
		}
	}
	permissions {
		register("playerlist.reload") {
			description = "Allows you to run the reload command"
		}
	}
}