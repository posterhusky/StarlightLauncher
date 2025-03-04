package net.vanolex.graphics

import net.vanolex.Panel
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Ellipse2D


class LoadingSpinner(val centerX: Int, val centerY: Int): Element() {

    override var canSkipRender = false

    val outerHollowCircle = run {
        val a = Area(Ellipse2D.Double(centerX.toDouble() - 64.0, centerY.toDouble() - 64.0, 128.0, 128.0))
        a.subtract(Area(Ellipse2D.Double(centerX.toDouble() - 40.0, centerY.toDouble() - 40.0, 80.0, 80.0)))
        a
    }

    var angleDegrees = 0

    override fun draw(g: Graphics2D) {
        g.color = Color(0, 0, 0, 128)
        g.fill(outerHollowCircle)

        val oldTransform = g.transform
        g.rotate(Math.toRadians(angleDegrees.toDouble()), centerX.toDouble(), centerY.toDouble())
        g.drawImage(Panel.loadingSpinner, centerX - 58, centerY - 58, null)
        g.transform = oldTransform
    }

    override fun tick() {
        angleDegrees += 4
    }
}