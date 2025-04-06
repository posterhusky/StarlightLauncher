package net.vanolex.graphics

import java.awt.Graphics2D

abstract class Element {
    abstract fun draw(g: Graphics2D)

    open fun tick() {}

    open fun onClick() {}

    open fun onScroll(amount: Int) {}

    open var canSkipRender: Boolean = true
}