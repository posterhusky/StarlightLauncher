package net.vanolex

import com.google.gson.Gson
import io.ktor.client.*
import kotlinx.coroutines.Job
import kotlinx.serialization.json.Json
import net.vanolex.tasks.LoadAccounts
import java.awt.Point
import java.io.File
import java.util.*

const val switchAuth = "OThmN2U0MmMyZTNhNGY4NmE3NGViNDNmYmI0MWVkMzk6MGEyNDQ5YTItMDAxYS00NTFlLWFmZWMtM2U4MTI5MDFjNGQ3"
const val pcAuth = "ZWM2ODRiOGM2ODdmNDc5ZmFkZWEzY2IyYWQ4M2Y1YzY6ZTFmMzFjMjExZjI4NDEzMTg2MjYyZDM3YTEzZmM4NGQ="
const val androidAuth = "M2Y2OWU1NmM3NjQ5NDkyYzhjYzI5ZjFhZjA4YThhMTI6YjUxZWU5Y2IxMjIzNGY1MGE2OWVmYTY3ZWY1MzgxMmU="
const val launcherAuth = "MzRhMDJjZjhmNDQxNGUyOWIxNTkyMTg3NmRhMzZmOWE6ZGFhZmJjY2M3Mzc3NDUwMzlkZmZlNTNkOTRmYzc2Y2Y="
val gson = Gson()
val ktJson = Json {
    ignoreUnknownKeys = true

}

lateinit var client: HttpClient
var accounts: MutableList<Account> = mutableListOf()
lateinit var lang: Lang

val loadAccountsTask = LoadAccounts()

val localMousePosition get() = Window.mousePosition ?: Point(-1, -1)

lateinit var config: Config

val appDataRoamingPath: String = System.getenv("APPDATA") ?: "${System.getProperty("user.home")}\\AppData\\Roaming"
val starlightLauncherFolder = File(appDataRoamingPath, "StarlightLauncher")

val jobList = mutableListOf<Job>()

fun loadFile(name: String): File {
    if (!starlightLauncherFolder.isDirectory) starlightLauncherFolder.delete()
    starlightLauncherFolder.mkdirs()
    return File(starlightLauncherFolder, name)
}

fun determineLang(): String {
    val langCode = Locale.getDefault().language
    return if (Panel.javaClass.getResource("/lang/${langCode}.json") != null) langCode
        else "en"
}

fun generateLang(): Lang {
    val inputStream = Panel.javaClass.getResourceAsStream("/lang/${config.langCode}.json")
        ?: Panel.javaClass.getResourceAsStream("/lang/en.json")

    return ktJson.decodeFromString<Lang>(inputStream.readAllBytes().decodeToString())
}

var frames: ULong = 0UL
var isRunning = true

operator fun Point.minus(other: Point): Point = Point(x-other.x, y-other.y)
operator fun Point.plus(other: Point): Point = Point(x+other.x, y+other.y)
operator fun Point.times(other: Point): Point = Point(x*other.x - y*other.y, x*other.y + y*other.x)
