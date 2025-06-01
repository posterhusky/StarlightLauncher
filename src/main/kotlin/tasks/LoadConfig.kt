package net.vanolex.tasks

import kotlinx.serialization.json.Json
import net.vanolex.*

class LoadConfig : Task() {
    override suspend fun task() {
        config = try {
            val jsonText = loadFile("config.json").readText()

            ktJson.decodeFromString<Config>(jsonText)
        } catch (e: Exception) {
            Config(
                "C:/Program Files/Epic Games/Fortnite/FortniteGame/Binaries/Win64/FortniteLauncher.exe",
                false,
                determineLang()
            )
        }
        config.save()
        lang = generateLang()
        status = TaskStatus.SUCCESS
    }
}
