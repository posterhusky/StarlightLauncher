package net.vanolex

import com.google.gson.Gson
import io.ktor.client.*
import kotlinx.coroutines.Job
import net.vanolex.tasks.LoadAccounts
import java.awt.MouseInfo
import java.awt.Point

val switchAuth = "OThmN2U0MmMyZTNhNGY4NmE3NGViNDNmYmI0MWVkMzk6MGEyNDQ5YTItMDAxYS00NTFlLWFmZWMtM2U4MTI5MDFjNGQ3"
val pcAuth = "ZWM2ODRiOGM2ODdmNDc5ZmFkZWEzY2IyYWQ4M2Y1YzY6ZTFmMzFjMjExZjI4NDEzMTg2MjYyZDM3YTEzZmM4NGQ="
val androidAuth = "M2Y2OWU1NmM3NjQ5NDkyYzhjYzI5ZjFhZjA4YThhMTI6YjUxZWU5Y2IxMjIzNGY1MGE2OWVmYTY3ZWY1MzgxMmU="
val gson = Gson()

lateinit var client: HttpClient
lateinit var accounts: MutableList<Account>
val isAccountsInitialized get() = ::accounts.isInitialized

val loadAccountsTask = LoadAccounts()

val localMousePosition get() = Window.mousePosition ?: Point(-1, -1)
val globalMousePosition get() = MouseInfo.getPointerInfo().location

lateinit var config: Config

val jobList = mutableListOf<Job>()

var frames: ULong = 0UL
var isRunning = true

operator fun Point.minus(other: Point): Point = Point(x-other.x, y-other.y)
operator fun Point.plus(other: Point): Point = Point(x+other.x, y+other.y)
operator fun Point.times(other: Point): Point = Point(x*other.x - y*other.y, x*other.y + y*other.x)
