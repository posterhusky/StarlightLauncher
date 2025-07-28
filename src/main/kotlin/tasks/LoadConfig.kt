package net.vanolex.tasks

import kotlinx.serialization.json.Json
import net.vanolex.*

class LoadConfig : Task() {
    override suspend fun task() {
        config = try {
            val jsonText = loadFile("config.json").readText()

            ktJson.decodeFromString<Config>(jsonText)
        } catch (e: Exception) {
            Config()
        }
        config.save()
        lang = generateLang()
        status = TaskStatus.SUCCESS
    }
}
