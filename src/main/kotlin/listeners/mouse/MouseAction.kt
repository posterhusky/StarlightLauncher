package net.vanolex.listeners.mouse

import java.awt.MouseInfo
import java.awt.Point

abstract class MouseAction(val relativeClickStart: Point) {
    val globalClickStart = MouseInfo.getPointerInfo().location

    abstract fun tick()

    open fun onRelease() {}
}