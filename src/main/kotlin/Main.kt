package net.vanolex


import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.nanoseconds

suspend fun main() = runBlocking {
    client = HttpClient(OkHttp)

    Window.createBufferStrategy(3)
    var lastFrame = 0L
    val nanosPerFrame = 1_000_000_000/60.1
    // game loop
    while (isRunning) {
        val now = System.nanoTime()
        if (now - lastFrame >= nanosPerFrame) {
            Panel.tick()
            lastFrame = now
        } else if (now - lastFrame <= nanosPerFrame*0.7) {
            delay(200000.nanoseconds)
        }
        // freeze while off focus
        while (!Window.isFocused && isRunning && !Panel.scene.isImportant) {
            delay(100)
        }
    }

    Window.isVisible = false
    Window.dispose()

    jobList.forEach { it.cancel() }

    client.close()
}
