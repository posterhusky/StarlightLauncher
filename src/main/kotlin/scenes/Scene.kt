package net.vanolex.scenes

import net.vanolex.Panel
import net.vanolex.Window
import net.vanolex.graphics.Background
import net.vanolex.graphics.Composition
import net.vanolex.graphics.CustomDraw
import net.vanolex.graphics.Shade
import net.vanolex.listeners.MouseListener
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Rectangle2D

abstract class Scene {

    open val isImportant = false
    private var sceneFrames = 0

    fun tick() {
        sceneFrames += 1
        MouseListener.action?.tick()
        update()
        composition.tick()
        Panel.repaint()
    }

    abstract val composition: Composition


    abstract fun update()

    fun draw(g: Graphics2D) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        val newComp = Composition(
            Background(),
            Shade(0, 0, Window.width, 22, 0),
            composition,
            CustomDraw({MouseListener.inCloseArea}) {
                if (MouseListener.inCloseArea) {
                    g.color = Color(242, 63, 67)
                    g.fill(Rectangle2D.Double(Window.width-28.0, 0.0, 28.0, 22.0))
                }
                g.color = Color(255, 255, 255)
                g.drawLine(Window.width-19, 6, Window.width-10, 15)
                g.drawLine(Window.width-19, 15, Window.width-10, 6)
            },
            CustomDraw({MouseListener.inMinimizeArea}) {
                if (MouseListener.inMinimizeArea) {
                    g.color = Color(255, 255, 255, 64)
                    g.fill(Rectangle2D.Double(Window.width-56.0, 0.0, 28.0, 22.0))
                }

                g.color = Color(255, 255, 255)
                g.fill(Rectangle2D.Double(Window.width-47.0, 11.0, 10.0, 1.0))
            },
        )
        if (!newComp.canSkipRender) newComp.draw(g)
    }

}
