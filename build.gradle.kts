import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
	id("java")
	id("com.diffplug.spotless") version "7.2.1"
	id("com.gradleup.shadow") version "9.0.0-rc2"
	id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.0"
}

group = "fi.fabianadrian"
version = "2.0.1-SNAPSHOT"
description = "A modern playerlist customization plugin."

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
	maven("https://repo.spongepowered.org/repository/maven-public/")
	maven("https://repo.extendedclip.com/releases/")
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

	compileOnly("net.luckperms:api:5.4")
	compileOnly("me.clip:placeholderapi:2.11.6")
	compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.3.0")

	implementation("org.spongepowered:configurate-yaml:4.2.0")
	implementation("org.bstats:bstats-bukkit:3.0.2")
}

paperPluginYaml {
	main = "fi.fabianadrian.playerlist.PlayerList"
	apiVersion = "1.21.4"
	authors = listOf("FabianAdrian")
	website = "https://github.com/fabianmakila/playerlist"
	dependencies {
		server {
			register("MiniPlaceholders") {
				load = PaperPluginYaml.Load.BEFORE
				required = false
			}
			register("LuckPerms") {
				load = PaperPluginYaml.Load.BEFORE
				required = false
			}
			register("PlaceholderAPI") {
				load = PaperPluginYaml.Load.BEFORE
				required = false
			}
		}
	}
	permissions {
		register("playerlist.reload") {
			description = "Allows the use of /playerlist reload"
		}
	}
}

java.toolchain {
	languageVersion.set(JavaLanguageVersion.of(21))
}

spotless {
	java {
		endWithNewline()
		formatAnnotations()
		leadingSpacesToTabs()
		removeUnusedImports()
		trimTrailingWhitespace()
	}
}

tasks {
	build {
		dependsOn(shadowJar)
	}
	compileJava {
		options.encoding = Charsets.UTF_8.name()
	}
	shadowJar {
		minimize()
		sequenceOf(
			"io.leangen.geantyref",
			"net.kyori.option",
			"org.bstats",
			"org.spongepowered.configurate"
		).forEach {
			relocate(it, "fi.fabianadrian.playerlist.dependency.$it")
		}
	}
}