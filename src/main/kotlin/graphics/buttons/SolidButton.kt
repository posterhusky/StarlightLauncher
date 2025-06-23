package net.vanolex.graphics.buttons

import net.vanolex.fonts.titleFont
import net.vanolex.fonts.titleFontItalic
import net.vanolex.graphics.Element
import net.vanolex.listeners.MouseListener
import net.vanolex.listeners.mouse.NormalMouseAction
import net.vanolex.localMousePosition
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Path2D
import java.awt.geom.RoundRectangle2D
import kotlin.math.max
import kotlin.math.min

class SolidButton(
    override val x: Int, override val y: Int, override val w: Int, override val h: Int,
    val text: () -> String, val style: Style, val isDisabled2: () -> Boolean = { false },
    override val clickAction: () -> Unit
): Button() {
    constructor(x: Int, y: Int, w: Int, h: Int, text: String, style: Style, isDisabled: Boolean = false,
                clickAction: () -> Unit): this(x,  y, w, h, {text}, style, { isDisabled }, clickAction)
    constructor(x: Int, y: Int, w: Int, h: Int, text: () -> String, isPrimary: Boolean, isDisabled: () -> Boolean = { false },
                clickAction: () -> Unit): this(x,  y, w, h, text, if (isPrimary) Style.PRIMARY else Style.SECONDARY, isDisabled, clickAction)
    constructor(x: Int, y: Int, w: Int, h: Int, text: String, isPrimary: Boolean, isDisabled: Boolean = false,
                clickAction: () -> Unit): this(x,  y, w, h, {text}, if (isPrimary) Style.PRIMARY else Style.SECONDARY, { isDisabled }, clickAction)

    enum class Style {
        PRIMARY, SECONDARY, OPAQUE,
    }

    override val isDisabled: Boolean
        get() = isDisabled2()

    override val bgColor: Color
        get() = if (isDisabled && style != Style.OPAQUE) Color(76, 85, 99)
                else when(style) {
                    Style.PRIMARY -> Color(246, 255, 19)
                    Style.SECONDARY -> Color(205, 231, 255)
                    Style.OPAQUE -> Color(0, 0, 0, 64)
                }

    override fun contentDraw(g: Graphics2D, buttonShape: Area) {
        g.color =   if (style == Style.OPAQUE) Color.WHITE
                    else Color(15, 13, 28)
        val glyph = (
                if (style == Style.PRIMARY) titleFont
                else titleFontItalic
                ).getGlyph((h.toDouble()/2)*(
                    if (isDisabled) 1.0
                    else 1 - activeProgress*0.04
                    ), text().uppercase(), w.toDouble() - 40 - 2*widthOffset)
        g.drawGlyphVector(glyph.coreGlyph, x + (w-glyph.width).toFloat()/2, y + (h+glyph.height).toFloat()/2)
    }
}