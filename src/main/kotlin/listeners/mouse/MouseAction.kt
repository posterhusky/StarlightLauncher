package net.vanolex.listeners.mouse

import java.awt.MouseInfo
import java.awt.Point

abstract class MouseAction{
    val globalClickStart: Point = MouseInfo.getPointerInfo().location

    abstract fun tick()

    open fun onRelease() {}
}