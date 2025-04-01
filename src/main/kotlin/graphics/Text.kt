package net.vanolex.graphics.elements

import net.vanolex.fonts.Glyph
import net.vanolex.graphics.Element
import java.awt.Color
import java.awt.Graphics2D

class Text(val glyph: Glyph, val x: Number, val y: Number, val color: Color): Element() {
    override fun draw(g: Graphics2D) {
        g.color = color
        g.drawGlyphVector(glyph.coreGlyph, x.toFloat(), y.toFloat() + glyph.height)
    }
}