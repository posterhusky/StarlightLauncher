package net.vanolex.listeners

import net.vanolex.isRunning
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

object CloseListener: WindowListener {
    override fun windowOpened(e: WindowEvent?) {}

    override fun windowClosing(e: WindowEvent?) {
        isRunning = false
    }

    override fun windowClosed(e: WindowEvent?) {}

    override fun windowIconified(e: WindowEvent?) {}

    override fun windowDeiconified(e: WindowEvent?) {}

    override fun windowActivated(e: WindowEvent?) {}

    override fun windowDeactivated(e: WindowEvent?) {}
}