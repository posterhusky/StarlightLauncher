package net.vanolex.graphics

import net.vanolex.fonts.Glyph
import java.awt.Color
import java.awt.Graphics2D

class DynamicText(val glyph: () -> Glyph, val x: () -> Number, val y: () -> Number, val color: Color): Element() {
    override fun draw(g: Graphics2D) {
        g.color = color
        val gl = glyph()
        g.drawGlyphVector(gl.coreGlyph, x().toFloat(), y().toFloat() + gl.height)
    }
}