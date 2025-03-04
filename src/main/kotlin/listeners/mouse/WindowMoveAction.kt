package net.vanolex.listeners.mouse

import net.vanolex.Window
import net.vanolex.minus
import net.vanolex.plus
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Toolkit
import kotlin.math.max
import kotlin.math.min

class WindowMoveAction(relativeClickStart: Point): MouseAction(relativeClickStart) {
    val offsetVector = Window.location - globalClickStart

    override fun tick() {
        Window.location = MouseInfo.getPointerInfo().location + offsetVector
    }
}