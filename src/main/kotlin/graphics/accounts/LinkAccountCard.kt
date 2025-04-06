package net.vanolex.graphics.accounts

import net.vanolex.Panel
import net.vanolex.fonts.archivoBlack
import net.vanolex.graphics.Element
import net.vanolex.graphics.ProfilePicture
import net.vanolex.listeners.MouseListener
import net.vanolex.listeners.mouse.NormalMouseAction
import net.vanolex.localMousePosition
import net.vanolex.scenes.LaunchFortniteScene
import net.vanolex.scenes.LinkAccount
import net.vanolex.tasks.BasicTask
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import kotlin.math.max
import kotlin.math.min

class LinkAccountCard(
    val x: Int, val y: Int, override val parent: CardScroller, val offset: () -> Int
): Element(), Card {
    val w = 270
    val h = 170

    override val selText = "LINK ACCOUNT"
    override val buttonText = "LINK"


    val isHovered get() = localMousePosition.x in max(65, x)..<min(935, x+w) && localMousePosition.y + offset() in max(105, y)..<min(435, y+h)
    var hoverProgress = 0.0

    override val isSelected: Boolean
        get() = parent.selectedCard == this
    override fun select() { parent.selectedCard = this }

    val isActive get() = isHovered && (MouseListener.action as? NormalMouseAction)?.clickEligible ?: false
    var activeProgress = 0.0

    val widthOffset get() = w*activeProgress*0.01
    val heightOffset get() = h*activeProgress*0.01

    override fun draw(g: Graphics2D) {
        if (y - offset() < -70 || y - offset() > 440) return

        val cardShape = Area( RoundRectangle2D.Double(
            x + widthOffset,
            y + heightOffset,
            w - 2*widthOffset,
            h - 2*heightOffset,
            30.0,
            30.0
        ))

        val plusCircle = Area( Ellipse2D.Double(
            x + 108 + widthOffset*0.1,
            y + 43 + heightOffset * 0.2,
            54 - widthOffset*0.2,
            54 - widthOffset*0.2,
        ))
        val plus = Area( RoundRectangle2D.Double(
            x + 123.0,
            y + 66.0,
            24.0,
            8.0,
            3.0, 3.0
        ))
        plus.add(Area( RoundRectangle2D.Double(
            x + 131.0,
            y + 58.0,
            8.0,
            24.0,
            3.0, 3.0
        )))

        g.color = Color(0, 0, 0, 128)
        g.fill(cardShape)
        g.fill(plusCircle)
        g.color = Color.WHITE
        g.fill(plus)

        if (isSelected) {
            val outline = Area(cardShape)
            outline.subtract(
                Area(
                    RoundRectangle2D.Double(
                x.toDouble() + 3 + widthOffset,
                y.toDouble() + 3 + heightOffset,
                w.toDouble() - 6 - 2*widthOffset,
                h.toDouble() - 6 - 2*heightOffset,
                24.0,
                24.0
            ))
            )

            g.color = Color(246, 255, 19)
            g.fill(outline)
        }

        g.color = Color.WHITE
        val glyph = archivoBlack.getGlyph(25-widthOffset*0.4, "LINK ACCOUNT")
        g.drawGlyphVector(glyph.coreGlyph, x + 135f - glyph.width.toFloat()/2f, y + 140f - heightOffset.toFloat()*0.87f)

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
        if (!isSelected) {
            select()
            return
        }
        execAction()
    }

    override fun execAction() {
        Panel.scene = LinkAccount()
    }
}