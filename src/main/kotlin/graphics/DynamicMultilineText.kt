package net.vanolex.graphics

import net.vanolex.fonts.FontImplementation
import java.awt.Graphics2D

class DynamicMultilineText(val text: () -> String, val fontImplementation: FontImplementation, val maxWith: Int, val size: Number, val x: Int, val y: Int): Element() {
    override fun draw(g: Graphics2D) {
        MultilineText(text(), fontImplementation, maxWith, size, x, y).draw(g)
    }
    val height: Int get() = MultilineText(text(), fontImplementation, maxWith, size, x, y).height
}