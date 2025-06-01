package net.vanolex.fonts

import java.awt.Font
import java.awt.font.FontRenderContext
import java.io.File

class FontImplementation(name: String, val heightMultiplier: Float) {
    val coreFont = Font.createFont(Font.TRUETYPE_FONT, javaClass.getResourceAsStream("/font/${name}.ttf"))

    fun getGlyph(size: Number, text: String, maxWidth: Number? = null): Glyph {
        var glyph = Glyph(coreFont.deriveFont(size.toFloat()).createGlyphVector(FontRenderContext(null, true, true), text), this, size.toFloat())
        if (maxWidth != null && glyph.width > maxWidth.toDouble()) {
            val newSize = size.toFloat() * maxWidth.toFloat() / glyph.width.toFloat()
            glyph = Glyph(coreFont.deriveFont(newSize).createGlyphVector(FontRenderContext(null, true, true), text), this, newSize)
        }
        return glyph
    }
}