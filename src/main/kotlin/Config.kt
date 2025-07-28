package net.vanolex

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var fortnitePath: String = "C:/Program Files/Epic Games/Fortnite/FortniteGame/Binaries/Win64/FortniteLauncher.exe",
    var closeAfterLaunch: Boolean = false,
    var checkUpdates: Boolean = true,
    var langCode: String = determineLang(),
) {
    fun save() {
        val jsonString = ktJson.encodeToString(this)
        val file = loadFile("config.json")
        file.writeText(jsonString)
    }
}