package net.vanolex.graphics

import net.vanolex.ImageManager
import net.vanolex.Panel
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import kotlin.math.roundToInt


class LoadingSpinner(val centerX: Int, val centerY: Int, val size: Double = 0.8): Element() {

    override var canSkipRender = false

    val outerHollowCircle = run {
        val a = Area(Ellipse2D.Double(centerX.toDouble() - 64*size, centerY.toDouble() - 64*size, 128*size, 128*size))
        a.subtract(Area(Ellipse2D.Double(centerX.toDouble() - 40*size, centerY.toDouble() - 40*size, 80*size, 80*size)))
        a
    }

    val image = ImageManager.loadingSpinner.getScaledInstance((116*size).roundToInt(), (116*size).roundToInt(), Image.SCALE_SMOOTH)

    var angleDegrees = 0

    override fun draw(g: Graphics2D) {
        g.color = Color(0, 0, 0, 128)
        g.fill(outerHollowCircle)

        val oldTransform = g.transform
        g.rotate(Math.toRadians(angleDegrees.toDouble()), centerX.toDouble(), centerY.toDouble())
        g.drawImage(image, (centerX - 58*size).roundToInt(), (centerY - 58*size).roundToInt(), null)
        g.transform = oldTransform
    }

    override fun tick() {
        angleDegrees += 4
    }
}