package net.vanolex.listeners.mouse

import net.vanolex.Window
import net.vanolex.minus
import net.vanolex.plus
import java.awt.MouseInfo

class WindowMoveAction : MouseAction() {
    val offsetVector = Window.location - globalClickStart

    override fun tick() {
        Window.location = MouseInfo.getPointerInfo().location + offsetVector
    }
}