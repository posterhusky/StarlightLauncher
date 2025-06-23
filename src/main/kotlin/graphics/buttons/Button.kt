package net.vanolex.graphics.buttons

import net.vanolex.composites.ShineComposite
import net.vanolex.graphics.Element
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
import kotlin.math.roundToInt
import kotlin.math.sin

abstract class Button: Element() {
    abstract val x: Int
    abstract val y: Int
    abstract val w: Int
    abstract val h: Int
    abstract val clickAction: () -> Unit
    abstract fun contentDraw(g: Graphics2D, buttonShape: Area)

    open val offset: Int = 0
    open val bgColor: Color = Color(0, 0, 0, 128)
    open val isHidden: Boolean = false
    open val isDisabled: Boolean = false

    open val isHovered get() = localMousePosition.x in x..x+w && localMousePosition.y in y..y+h
    var hoverProgress = 0.0

    val isActive get() = isHovered && (MouseListener.action as? NormalMouseAction)?.clickEligible ?: false
    var activeProgress = 0.0

    val widthOffset get() = if (isDisabled) 0.0 else w*activeProgress*0.015
    val heightOffset get() = if (isDisabled) 0.0 else h*activeProgress*0.015

    override fun draw(g: Graphics2D) {
        if (isHidden) return

        val buttonShape = Area( RoundRectangle2D.Double(
            x + widthOffset,
            y + heightOffset,
            w - 2*widthOffset,
            h - 2*heightOffset,
            30.0,
            30.0
        ))

        g.color = bgColor
        g.fill(buttonShape)

        contentDraw(g, buttonShape)

        if (hoverProgress <= 0.0 || isDisabled) return

        if (isHovered) {
            val oldComp = g.composite

            val k = (w*0.6).roundToInt()
            val dx = (hoverProgress * (w + k)).roundToInt()
            for (i in 0 until k) {
                val t = sin(i*Math.PI/k).toFloat()
                g.composite = ShineComposite((t*0.9f).coerceIn(0f..0.9f))
                val shineShape = Area(
                    Rectangle2D.Double(
                    x - k + dx + i + 0.0,
                    y.toDouble(),
                    1.0,
                    h.toDouble()
                ))
                shineShape.intersect(Area(buttonShape))
                g.fill(shineShape)
            }
            g.composite = oldComp
        }

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
            if (isHovered) min(hoverProgress+0.1, 1.0)
            else max(hoverProgress-0.15, 0.0)

        activeProgress =
            if (isActive) min(activeProgress+0.2, 1.0)
            else max(activeProgress-0.4, 0.0)
    }

    override fun onClick() {
        if (!isHovered || isDisabled) return
        clickAction()
    }

}