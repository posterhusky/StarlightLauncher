package net.vanolex.tasks

import net.vanolex.config
import net.vanolex.Config
import net.vanolex.epicapi.AsyncTask
import net.vanolex.gson
import java.io.File

class LoadConfig : AsyncTask() {
    override suspend fun task() {
        config = try {
            gson.fromJson(File("./config.json").readText(), Config::class.java)
        } catch (e: Exception) {
            Config(
                "C:/Program Files/Epic Games/Fortnite/FortniteGame/Binaries/Win64/FortniteLauncher.exe",
                true,
            )
        }
        config.save()
        status = TaskStatus.SUCCESS
    }
}
