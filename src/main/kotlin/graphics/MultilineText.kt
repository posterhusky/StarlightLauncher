package net.vanolex.graphics

import net.vanolex.fonts.FontImplementation
import net.vanolex.fonts.Glyph
import java.awt.Graphics2D

class MultilineText(text: String, fontImplementation: FontImplementation, val maxWith: Int, val size: Number, val x: Int, val y: Int): Element() {
    val glyphList = mutableListOf<Glyph>()
    val height get() = (glyphList.size * size.toDouble() * 1.2).toInt()

    init {
        var currentLine = ""

        for (i in text.split(" ")) {
            val tempGlyph = fontImplementation.getGlyph(size, ("$currentLine $i").removePrefix(" "))
            if (tempGlyph.width <= maxWith) {
                currentLine += " $i"
                continue
            }
            glyphList.add(fontImplementation.getGlyph(size, currentLine))
            currentLine = i
        }
        glyphList.add(fontImplementation.getGlyph(size, currentLine))
    }

    override fun draw(g: Graphics2D) {
        glyphList.forEachIndexed { index, glyph ->
            g.drawGlyphVector(glyph.coreGlyph, (x + (maxWith - glyph.width)/2).toFloat(), y + size.toFloat()*1.2f*index)
        }
    }


}