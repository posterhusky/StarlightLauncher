package net.vanolex.tasks
import net.vanolex.config
import java.io.File

class DownloadUpdate(val updateLink: String, val newUpdateName: String): Task() {

    override suspend fun task() {
        try {
            val codeSrc = javaClass.protectionDomain.codeSource.location.toURI().path
            val currentJar = File(codeSrc)
            val dir = currentJar.parentFile

            val updater = File(dir, "updater.exe")
            val proc = ProcessBuilder(
                "cmd.exe",
                "/C",
                updater.absolutePath,
                updateLink,
                "StarlightLauncher-${newUpdateName}.jar"
            ).redirectErrorStream(true).start()

            val code = proc.waitFor()
            if (code != 0) status = TaskStatus.FAILED

            ProcessBuilder(
                "cmd.exe",
                "/C",
                "start",
                "/d",
                dir.absolutePath,
                "StarlightLauncher.exe"
            ).start()

            status = TaskStatus.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            status = TaskStatus.FAILED
        }
    }
}