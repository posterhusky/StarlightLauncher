package net.vanolex.graphics

import net.vanolex.Panel
import net.vanolex.Window
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import java.awt.geom.RoundRectangle2D

class Shade(val x: Int, val y: Int, val w: Int, val h: Int, val cornerRadius: Int): Element() {
    override fun draw(g: Graphics2D) {
        val oldClip = g.clip
        g.setClip(
            RoundRectangle2D.Double(
                x.toDouble(),
                y.toDouble(),
                w.toDouble(),
                h.toDouble(),
                cornerRadius.toDouble(),
                cornerRadius.toDouble()
            )
        )

        g.drawImage(Panel.backgroundBlur, 0, 0, null)

        g.clip = oldClip
    }
}