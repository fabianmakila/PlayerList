import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
	id("java")
	id("com.diffplug.spotless") version "8.8.0"
	id("com.gradleup.shadow") version "9.4.3"
	id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
}

group = "fi.fabianadrian"
version = "2.1.1-SNAPSHOT"
description = "A modern playerlist customization plugin."

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
	maven("https://repo.extendedclip.com/releases/") // PlaceholderAPI
	maven("https://repo.faststats.dev/releases") // FastStats
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")

	compileOnly("net.luckperms:api:5.5")
	compileOnly("me.clip:placeholderapi:2.11.6")
	compileOnly("io.github.miniplaceholders:miniplaceholders-api:3.2.0")

	implementation("org.spongepowered:configurate-yaml:4.2.0")
	implementation("dev.faststats.metrics:bukkit:0.27.1")
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
	languageVersion.set(JavaLanguageVersion.of(25))
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
	}
}