package net.vanolex.tasks

import net.vanolex.*

class LoadConfig : Task() {
    override suspend fun task() {
        config = try {
            val file = loadFile("config.json")
            gson.fromJson(file.readText(), Config::class.java)
        } catch (e: Exception) {
            Config(
                "C:/Program Files/Epic Games/Fortnite/FortniteGame/Binaries/Win64/FortniteLauncher.exe",
                false,
            )
        }
        config.save()
        status = TaskStatus.SUCCESS
    }
}
