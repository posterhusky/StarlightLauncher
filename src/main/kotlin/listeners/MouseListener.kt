package net.vanolex.listeners

import net.vanolex.Window
import net.vanolex.listeners.mouse.*
import net.vanolex.localMousePosition
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

object MouseListener: MouseListener {

    var action: MouseAction? = null

    val inCloseArea get() = localMousePosition.y <= 22 && localMousePosition.x in Window.width-28 ..Window.width
    val inMinimizeArea get() = localMousePosition.y <= 22 && localMousePosition.x in Window.width-56 ..Window.width-29

    override fun mousePressed(e: MouseEvent) {
        if (e.button != 1) return

        action = when (true) {
            inCloseArea -> WindowCloseAction(localMousePosition)
            inMinimizeArea -> WindowMinimizeAction(localMousePosition)
            (e.point.y <= 22) -> WindowMoveAction(localMousePosition)
            else -> NormalMouseAction(localMousePosition)
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        action?.onRelease()
        action = null
    }

    override fun mouseClicked(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}

}