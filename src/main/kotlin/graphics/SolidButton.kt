package net.vanolex.graphics

import net.vanolex.Window
import net.vanolex.fonts.archivoBlack
import net.vanolex.fonts.archivoBlackItalic
import net.vanolex.listeners.MouseListener
import net.vanolex.listeners.mouse.NormalMouseAction
import net.vanolex.localMousePosition
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import java.awt.geom.RoundRectangle2D
import kotlin.math.max
import kotlin.math.min

class SolidButton(val x: Int, val y: Int, val w: Int, val h: Int, val text: String, val isPrimary: Boolean, val clickAction: () -> Unit): Element() {

    val isHovered get() = localMousePosition.x in x..x+w && localMousePosition.y in y..y+h
    var hoverProgress = 0.0

    val isActive get() = isHovered && (MouseListener.action as? NormalMouseAction)?.clickEligible ?: false
    var activeProgress = 0.0

    val glyph = (if (isPrimary) archivoBlack else archivoBlackItalic).getGlyph(h.toDouble()/2, text.uppercase())

    val widthOffset get() = w*activeProgress*0.01
    val heightOffset get() = h*activeProgress*0.01

    override fun draw(g: Graphics2D) {
        g.color = if (isPrimary) Color(246, 255, 19) else Color(205, 231, 255)
        g.fill(RoundRectangle2D.Double(
            x.toDouble() + widthOffset,
            y.toDouble() + heightOffset,
            w.toDouble() - 2*widthOffset,
            h.toDouble() - 2*heightOffset,
            30.0,
            30.0
        ))

        g.color = Color(9, 8, 21)
        if (activeProgress <= 0.0) g.drawGlyphVector(glyph.coreGlyph, x + (w-glyph.width).toFloat()/2, y + (h+glyph.height).toFloat()/2)
        else {
            val resizedGlyph = (if (isPrimary) archivoBlack else archivoBlackItalic).getGlyph((h.toDouble()/2)*(1 - activeProgress*0.04), text.uppercase())
            g.drawGlyphVector(resizedGlyph.coreGlyph, x + (w-resizedGlyph.width).toFloat()/2, y + (h+resizedGlyph.height).toFloat()/2)
        }

        if (hoverProgress <= 0.0) return

        g.color = Color(255, 255, 255, (20*hoverProgress).toInt())
        g.fill(getOutlineShape(9, 7))
        g.color = Color(255, 255, 255, (20*hoverProgress).toInt())
        g.fill(getOutlineShape(8, 5))

        g.color = Color(255, 255, 255)
        g.fill(getOutlineShape(7, (3*hoverProgress).toInt()))

    }

    fun getOutlineShape(offset: Int, thickness: Int): Area {
        val baseShape = Area(
            RoundRectangle2D.Double(
                x.toDouble() - offset + widthOffset,
                y.toDouble() - offset + heightOffset,
                w.toDouble() + 2*(offset - widthOffset),
                h.toDouble() + 2*(offset - heightOffset),
                30.0 + offset,
                30.0  + offset
            )
        )
        val hollowSection = Area(
            RoundRectangle2D.Double(
                x.toDouble() - offset + thickness + widthOffset,
                y.toDouble() - offset + thickness + heightOffset,
                w.toDouble() + 2*(offset - thickness - widthOffset),
                h.toDouble() + 2*(offset - thickness - heightOffset),
                30.0 + offset - thickness,
                30.0  + offset - thickness
            )
        )
        baseShape.subtract(hollowSection)
        return baseShape
    }

    override fun tick() {
        canSkipRender = (hoverProgress == if (isHovered) 1.0 else 0.0) && (activeProgress == if (isActive) 1.0 else 0.0)

        hoverProgress =
            if (isHovered) min(hoverProgress+0.15, 1.0)
            else max(hoverProgress-0.15, 0.0)

        activeProgress =
            if (isActive) min(activeProgress+0.2, 1.0)
            else max(activeProgress-0.4, 0.0)
    }

    override fun onClick() {
        if (!isHovered) return
        clickAction()
    }
}