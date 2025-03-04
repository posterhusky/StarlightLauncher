package net.vanolex.tasks

import jdk.internal.org.jline.utils.Colors.s
import net.vanolex.config
import net.vanolex.epicapi.AsyncTask
import java.io.File

class FindFortnite: AsyncTask() {
    override suspend fun task() {
        if (testPath(config.fortnitePath)) {
            status = TaskStatus.SUCCESS
            return
        }

        try {
            definePath(extractPathFromEASLogs())
            status = TaskStatus.SUCCESS
        } catch (e: Exception) {
            status = TaskStatus.FAILED
        }
    }

    fun definePath(path: String) {
        config.fortnitePath = path
        status = TaskStatus.SUCCESS
    }

    fun extractPathFromEASLogs(): String {
        val userHome = System.getProperty("user.home")
        val easLogDir = File(userHome, "AppData\\Roaming\\EasyAntiCheat\\prod-fn\\62a9473a2dca46b29ccf17577fcf42d7")

        if (!easLogDir.exists() || !easLogDir.isDirectory) {
            throw IllegalStateException("EAS log directory not found: ${easLogDir.absolutePath}")
        }

        val prefix = "Loaded the following settings .json file: '"
        val suffix = "EasyAntiCheat\\Settings.json'"

        for (file in easLogDir.listFiles() ?: throw IllegalStateException("Could not list files in folder.")) {
            if (!file.isFile) continue

            val logText = file.readText()
            val removedPrefix = logText.split(prefix).getOrNull(1) ?: continue
            val launcherFolder = removedPrefix.split(suffix).getOrNull(0) ?: continue
            val finalPath = launcherFolder + "FortniteLauncher.exe"

            if (!File(finalPath).exists()) continue
            return finalPath
        }
        throw IllegalStateException("None of the files had the correct path.")
    }

    fun testPath(path: String): Boolean {
        val file = File(path)
        if (!file.exists()) return false
        if (!path.replace("\\", "/").endsWith("Binaries/Win64/FortniteLauncher.exe")) return false
        return true
    }
}