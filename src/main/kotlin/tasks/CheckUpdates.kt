package net.vanolex.tasks

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.vanolex.Version
import net.vanolex.client
import net.vanolex.ktJson

class CheckUpdates: Task() {
    var updateLink: String? = null
    var newVersionName = ""

    @Serializable
    data class GitHubRelease(
        @SerialName("tag_name") val tag: String,
        val assets: List<Asset>
    )

    @Serializable
    data class Asset(
        val name: String,
        @SerialName("browser_download_url") val url: String
    )

    override suspend fun task() {
        try {
            val release =
                ktJson.decodeFromString<GitHubRelease>(client.get("https://api.github.com/repos/posterhusky/StarlightLauncher/releases/latest") {
                    header(HttpHeaders.Accept, "application/vnd.github.v3+json")
                }.bodyAsText())

            for (i in release.assets) {
                if (!i.name.endsWith(".jar")) continue
                if (!isNewerVersion(i.name)) continue
                updateLink = i.url
                newVersionName = i.name.removePrefix("StarlightLauncher-").removeSuffix(".jar")
                status = TaskStatus.SUCCESS
                return
            }

            status = TaskStatus.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            status = TaskStatus.FAILED
        }
    }
    fun isNewerVersion(file: String): Boolean {
        val curVerSplit = Version.CURRENT.split(".", "-rc")

        val candidateVerSplit = file.removePrefix("StarlightLauncher-").removeSuffix(".jar").split(".", "-rc")

        if (curVerSplit.size != 3) return false
        if (candidateVerSplit.size != 3) return false

        for (i in 0..2) {
            val a = curVerSplit.get(i).toIntOrNull() ?: return false
            val b = candidateVerSplit.get(i).toIntOrNull() ?: return false
            if (a == b) continue
            return a < b
        }
        return false
    }
}