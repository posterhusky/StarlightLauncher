package net.vanolex

import net.vanolex.listeners.CloseListener
import net.vanolex.listeners.MouseListener
import net.vanolex.listeners.MouseWheelListener
import java.awt.Color
import java.awt.Dimension
import java.awt.Frame
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.WindowConstants

object Window: JFrame("Starlight Launcher") {
    private fun readResolve(): Any = this

    init {
        setResizable(true)
        background = Color.BLACK
        isUndecorated = true

        setLocationRelativeTo(null)

        Panel.maximumSize = Dimension(1000, 600)
        Panel.preferredSize = Dimension(1000, 600)
        Panel.minimumSize = Dimension(1000, 600)
        add(Panel)
        maximumSize = Dimension(1000, 600)
        preferredSize = Dimension(1000, 600)
        minimumSize = Dimension(1000, 600)
        isResizable = false

        setLocationRelativeTo(null)
        iconImage = ImageIO.read(javaClass.getResourceAsStream("/img/icon.png"))
        isVisible = true

        addMouseListener(MouseListener)
        addWindowListener(CloseListener)
        addMouseWheelListener(MouseWheelListener)
    }
}