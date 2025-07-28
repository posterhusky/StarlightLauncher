package net.vanolex.graphics

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

class ProfilePicture(val centerX: Int, val centerY: Int, image: BufferedImage, val size: Int = 140, val drawBackground: Boolean = true, var yOffset: Int = 0): Element() {
    val outerSize: Double = size*1.2
    val image = image.getScaledInstance(size, size, Image.SCALE_SMOOTH)

    val innerCircle
        get() = Area(Ellipse2D.Double(centerX.toDouble() - size/2, centerY.toDouble() - size/2 - yOffset, size.toDouble(), size.toDouble()))
    val outerCircle
        get() = Area(Ellipse2D.Double(centerX.toDouble() - outerSize/2, centerY.toDouble() - outerSize/2 - yOffset, outerSize, outerSize))

    override fun draw(g: Graphics2D) {
        if (drawBackground) {
            g.color = Color(0, 0, 0, 128)
            g.fill(outerCircle)
        }
        g.color = Color(0, 0, 0, 64)
        g.fill(innerCircle)

        val oldClip = g.clip
        val tmpClip = Area(innerCircle)
        tmpClip.intersect(Area(oldClip))
        g.clip = tmpClip

        g.drawImage(image, centerX - size/2, centerY - size/2 - yOffset, null)

        g.clip = oldClip
    }
}