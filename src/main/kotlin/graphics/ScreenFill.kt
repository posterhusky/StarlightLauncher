package net.vanolex.graphics.elements

import net.vanolex.Window
import net.vanolex.graphics.Element
import java.awt.Color
import java.awt.Graphics2D

class ScreenFill(val color: Color): Element() {
    override fun draw(g: Graphics2D) {
        g.color = color
        g.fillRect(0, 0, Window.width, Window.height)
    }
}