package net.vanolex


import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import net.vanolex.scenes.LogoScene

suspend fun main() = coroutineScope {
    client = HttpClient(OkHttp)

    Window.createBufferStrategy(3)
    var lastFrame = 0L
    while (isRunning) {
        val now = System.nanoTime()
        if (now - lastFrame >= 16666666) {
            Panel.tick()
            lastFrame = now
        }
        while (!Window.isFocused && Panel.scene !is LogoScene) {
            delay(100)
        }
    }

    Window.isVisible = false
    Window.dispose()

    jobList.forEach { it.cancel() }

    client.close()
}
