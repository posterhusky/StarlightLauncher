package net.vanolex.listeners
import net.vanolex.Panel
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener

object MouseWheelListener: MouseWheelListener {
    override fun mouseWheelMoved(e: MouseWheelEvent) {
        Panel.scene.composition.onScroll(e.wheelRotation)
    }
}