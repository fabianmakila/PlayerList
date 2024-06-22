plugins {
	id("playerlist.java-conventions")
}

dependencies {
	compileOnly(libs.adventure.api)
	compileOnly(libs.adventure.text.minimessage)
	compileOnly(libs.adventure.text.serializer.plain)

	implementation(libs.dazzleconf) {
		exclude("org.yaml")
	}
	compileOnly(libs.slf4j)
	compileOnly(libs.snakeyaml)

	compileOnlyApi(libs.plugin.miniplaceholders)
	compileOnly(libs.plugin.luckperms)
}