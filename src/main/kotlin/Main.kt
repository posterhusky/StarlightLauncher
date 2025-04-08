package net.vanolex


import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import net.vanolex.scenes.LogoScene
import java.awt.event.FocusEvent

suspend fun main() = coroutineScope {
    client = HttpClient(OkHttp)

    Window.createBufferStrategy(3)
    var lastFrame = 0L
    while (isRunning || Panel.scene is LogoScene) {
        val now = System.nanoTime()
        if (now - lastFrame >= 1000000000/60.0) {
            Panel.tick()
            lastFrame = now
        }
        while (!Window.isFocused && isRunning && !Panel.scene.isImportant) {
            delay(100)
        }
    }

    Window.isVisible = false
    Window.dispose()

    jobList.forEach { it.cancel() }

    client.close()
}
