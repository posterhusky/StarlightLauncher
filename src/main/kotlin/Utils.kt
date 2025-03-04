package net.vanolex

import com.google.gson.Gson
import io.ktor.client.*
import kotlinx.coroutines.Job
import net.vanolex.fonts.archivoBlack
import net.vanolex.fonts.archivoMedium
import net.vanolex.graphics.*
import net.vanolex.graphics.elements.Text
import java.awt.Color
import java.awt.MouseInfo
import java.awt.Point

val switchAuth = "OThmN2U0MmMyZTNhNGY4NmE3NGViNDNmYmI0MWVkMzk6MGEyNDQ5YTItMDAxYS00NTFlLWFmZWMtM2U4MTI5MDFjNGQ3"
val pcAuth = "ZWM2ODRiOGM2ODdmNDc5ZmFkZWEzY2IyYWQ4M2Y1YzY6ZTFmMzFjMjExZjI4NDEzMTg2MjYyZDM3YTEzZmM4NGQ="
val gson = Gson()

lateinit var client: HttpClient

lateinit var mainThread: Job

val localMousePosition get() = Window.mousePosition ?: Point(-1, -1)
val globalMousePosition get() = MouseInfo.getPointerInfo().location

lateinit var config: Config

val jobList = mutableListOf<Job>()

var frames: ULong = 0UL
var isRunning = true

fun getDialogue(
    title: String, lore: String, hasSpinner: Boolean = false,
    primaryButtonText: String? = null, primaryButtonAction: (() -> Unit)? = null,
    secondaryButtonText: String? = null, secondaryButtonAction: (() -> Unit)? = null,
): Composition {
    val titleglyph = archivoBlack.getGlyph(45, title)
    val loreElement = MultilineText(lore, archivoMedium, 540, 18, 230, 180)
    val hasPBtn = primaryButtonText != null || primaryButtonAction != null
    val hasSBtn = secondaryButtonText != null || secondaryButtonAction != null

    val composition = Composition(
        Shade(200, 40, 600, 540, 40),
        Text(titleglyph, (1000-titleglyph.width)/2, 90, Color.WHITE),
        loreElement,
    )

    if (hasSpinner) {
        composition.addElements(LoadingSpinner(500,
            (
                loreElement.y+loreElement.height +
                if (hasPBtn && hasSBtn) 415
                else 480
            )/2 - 25
        ))
    }

    if (hasPBtn) {
        composition.addElements(SolidButton(220, if (hasSBtn) 415 else 480, 560, 70,
            primaryButtonText ?: "", true, primaryButtonAction ?: {}))
    }

    if (hasSBtn) {
        composition.addElements(SolidButton(220, if (hasPBtn) 500 else 480, 560, if (hasPBtn) 50 else 70,
            secondaryButtonText ?: "", isPrimary = false, secondaryButtonAction ?: {}))
    }

    return composition
}

operator fun Point.minus(other: Point): Point = Point(x-other.x, y-other.y)
operator fun Point.plus(other: Point): Point = Point(x+other.x, y+other.y)
operator fun Point.times(other: Point): Point = Point(x*other.x - y*other.y, x*other.y + y*other.x)
