package net.vanolex.graphics

import java.awt.Graphics2D

class Composition(vararg elements: Element): Element() {
    val elements = elements.toMutableList()

    var renderedTimes = 0

    fun addElements(vararg e: Element) = elements.addAll(e)

    override var canSkipRender: Boolean
        get() = elements.all { it.canSkipRender } && renderedTimes > 5
        set(value) {}

    override fun draw(g: Graphics2D) {
        renderedTimes++
        elements.forEach { it.draw(g) }
    }

    override fun tick() {
        elements.forEach { it.tick() }
    }

    override fun onClick() {
        elements.forEach { it.onClick() }
    }
}