package net.vanolex.listeners.mouse

import net.vanolex.isRunning
import net.vanolex.listeners.MouseListener

class WindowCloseAction : MouseAction() {
    override fun tick() {}

    override fun onRelease() {
        if (!MouseListener.inCloseArea) return
        isRunning = false
    }
}