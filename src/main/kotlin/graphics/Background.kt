package net.vanolex.graphics

import net.vanolex.Panel
import java.awt.Graphics2D

class Background: Element() {
    override fun draw(g: Graphics2D) {
        g.drawImage(Panel.backgroundClear, 0, 0, Panel.backgroundClear.width, Panel.backgroundClear.height, null)
    }
}