package net.vanolex.fonts

import java.awt.font.GlyphVector

class Glyph(
    val coreGlyph: GlyphVector,
    val font: FontImplementation,
    val size: Float
) {
    val width = coreGlyph.logicalBounds.width
    val height = (font.heightMultiplier*size).toInt()
    val topPadding = size - height
}