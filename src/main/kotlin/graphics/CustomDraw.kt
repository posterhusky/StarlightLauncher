package net.vanolex.graphics

import java.awt.Graphics2D

class CustomDraw<T>(val customValue: () -> T, val customDraw: (g: Graphics2D) -> Unit): Element() {
    private var lastValue = customValue()

    override var canSkipRender: Boolean
        get() = lastValue == customValue()
        set(value) {}

    override fun draw(g: Graphics2D) {
        lastValue = customValue()
        customDraw(g)
    }
}