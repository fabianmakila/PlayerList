plugins {
	id("playerlist.java-conventions")
	alias(libs.plugins.pluginYml)
	alias(libs.plugins.shadow)
}

dependencies {
	compileOnly(libs.platform.paper)

	implementation(project(":common"))
	compileOnly(libs.plugin.luckperms)
}

tasks {
	build {
		dependsOn(shadowJar)
	}
	shadowJar {
		minimize()

		destinationDirectory.set(file("${rootProject.rootDir}/dist"))
		archiveClassifier.set("")
		archiveBaseName.set("${rootProject.name}-${project.name.replaceFirstChar(Char::titlecase)}")

		sequenceOf(
			"space.arim",
		).forEach { pkg ->
			relocate(pkg, "fi.fabianadrian.playerlist.dependency.$pkg")
		}
	}
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