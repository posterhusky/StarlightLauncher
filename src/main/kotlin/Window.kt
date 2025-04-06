package net.vanolex

import net.vanolex.listeners.CloseListener
import net.vanolex.listeners.MouseListener
import net.vanolex.listeners.MouseWheelListener
import java.awt.Color
import java.awt.Dimension
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.WindowConstants

object Window: JFrame("Fortnite Launcher") {
    private fun readResolve(): Any = Window

    init {
        setResizable(true)
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        background = Color.BLACK
        isUndecorated = true

        setLocationRelativeTo(null)

        Panel.maximumSize = Dimension(1000, 600)
        Panel.preferredSize = Dimension(1000, 600)
        Panel.minimumSize = Dimension(1000, 600)
        add(Panel)
        pack()
        isResizable = false

        setLocationRelativeTo(null)
        iconImage = ImageIO.read(javaClass.getResourceAsStream("/img/icon.png"))
        isVisible = true
        addMouseListener(MouseListener)
        addWindowListener(CloseListener)
        addMouseWheelListener(MouseWheelListener)
    }
}