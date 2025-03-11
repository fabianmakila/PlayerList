import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
	id("java")
	id("com.diffplug.spotless") version "7.0.0.BETA4"
	id("com.gradleup.shadow") version "8.3.2"
	id("xyz.jpenilla.resource-factory-paper-convention") version "1.2.0"
}

group = "fi.fabianadrian"
version = "2.0.0-SNAPSHOT"
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

tasks {
	compileJava {
		options.encoding = "UTF-8"
	}
}

spotless {
	java {
		endWithNewline()
		formatAnnotations()
		indentWithTabs()
		removeUnusedImports()
		trimTrailingWhitespace()
	}
}

tasks {
	build {
		dependsOn(shadowJar)
	}
	shadowJar {
		minimize()
		sequenceOf(
			"org.spongepowered.configurate",
			"net.kyori.option",
			"io.leangen.geantyref"
		).forEach { pkg ->
			relocate(pkg, "fi.fabianadrian.playerlist.dependency.$pkg")
		}
	}
}