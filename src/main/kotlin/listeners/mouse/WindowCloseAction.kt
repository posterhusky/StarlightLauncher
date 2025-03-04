package net.vanolex.listeners.mouse

import net.vanolex.isRunning
import net.vanolex.listeners.MouseListener
import java.awt.Point

class WindowCloseAction(relativeClickStart: Point): MouseAction(relativeClickStart) {
    override fun tick() {}

    override fun onRelease() {
        if (!MouseListener.inCloseArea) return
        isRunning = false
    }
}