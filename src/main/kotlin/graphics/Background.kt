package net.vanolex.graphics

import net.vanolex.ImageManager
import java.awt.Graphics2D

class Background: Element() {
    override fun draw(g: Graphics2D) {
        ImageManager.drawBackground(g, false)
    }
}