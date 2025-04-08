package net.vanolex


import net.vanolex.scenes.LogoScene
import net.vanolex.scenes.Scene
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JPanel

object Panel: JPanel() {
    private fun readResolve(): Any = this

    val backgroundClear: BufferedImage by lazy { ImageIO.read(javaClass.getResourceAsStream("/img/background-clear.png")) }
    val backgroundBlur: BufferedImage by lazy { ImageIO.read(javaClass.getResourceAsStream("/img/background-darken.png")) }
    val loadingSpinner: BufferedImage by lazy { ImageIO.read(javaClass.getResourceAsStream("/img/loading-spinner.png")) }

    var scene: Scene = LogoScene()

    public override fun paintComponent(g: Graphics) {
        scene.draw(g as Graphics2D)
        g.dispose()
    }

    fun tick() {
        scene.tick()
        frames += 1.toUInt()
    }
}