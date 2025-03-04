package net.vanolex.listeners.mouse

import net.vanolex.Window
import net.vanolex.isRunning
import net.vanolex.listeners.MouseListener
import java.awt.Frame
import java.awt.Point

class WindowMinimizeAction(relativeClickStart: Point): MouseAction(relativeClickStart) {
    override fun tick() {}

    override fun onRelease() {
        if (!MouseListener.inMinimizeArea) return
        Window.state = Frame.ICONIFIED
    }
}