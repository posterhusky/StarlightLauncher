package net.vanolex.graphics

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.Ellipse2D

class ProfilePicture(val centerX: Int, val centerY: Int, val image: Image): Element() {
    override fun draw(g: Graphics2D) {
        g.color = Color(0, 0, 0, 128)
        g.fill(Ellipse2D.Double(centerX.toDouble() - 85.0, centerY.toDouble() - 85.0, 170.0, 170.0))
        g.color = Color(0, 0, 0, 64)
        g.fill(Ellipse2D.Double(centerX.toDouble() - 70.0, centerY.toDouble() - 70.0, 140.0, 140.0))

        val oldClip = g.clip
        g.setClip(Ellipse2D.Double(centerX.toDouble() - 70.0, centerY.toDouble() - 70.0, 140.0, 140.0))

        g.drawImage(image, centerX - 70, centerY - 70, null)

        g.clip = oldClip
    }
}