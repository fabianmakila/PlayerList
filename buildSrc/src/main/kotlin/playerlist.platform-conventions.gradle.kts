import org.gradle.kotlin.dsl.invoke

plugins {
	id("playerlist.java-conventions")
	id("com.gradleup.shadow")
}

dependencies {
	implementation(project(":common"))
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