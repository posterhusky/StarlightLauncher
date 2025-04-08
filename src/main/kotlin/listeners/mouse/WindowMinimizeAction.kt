package net.vanolex.listeners.mouse

import net.vanolex.Window
import net.vanolex.listeners.MouseListener
import java.awt.Frame

class WindowMinimizeAction : MouseAction() {
    override fun tick() {}

    override fun onRelease() {
        if (!MouseListener.inMinimizeArea) return
        Window.state = Frame.ICONIFIED
    }
}