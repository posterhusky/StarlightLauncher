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
        val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        val point = MouseInfo.getPointerInfo().location + offsetVector
        point.x = min(max(point.x, 0), screenSize.width - Window.width)
        point.y = min(max(point.y, 0), screenSize.height - Window.height)
        Window.location = point
    }
}