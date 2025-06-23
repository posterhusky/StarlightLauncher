package net.vanolex.graphics.buttons.cards

import net.vanolex.Account
import net.vanolex.Panel
import net.vanolex.fonts.archivoBlack
import net.vanolex.graphics.CardScroller
import net.vanolex.graphics.ProfilePicture
import net.vanolex.lang
import net.vanolex.scenes.LaunchFortniteScene
import net.vanolex.tasks.BasicTask
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import kotlin.math.min

class AccountCard(
    override val x: Int, override val y: Int, val account: Account, override val parent: CardScroller
): Card() {
    override val selText = account.displayName
    override val buttonText = lang.launch

    val profilePicture = ProfilePicture(x + 55, y + 65, account.profilePicture, 70, false)
    val bgCol = getAverageColor(account.profilePicture)
    val textSize = min(30.0, (230/ archivoBlack.getGlyph(30-widthOffset, account.displayName.uppercase()).width)*30)

    override fun cardDraw(g: Graphics2D, cardShape: Area) {
        val background = Area(cardShape)
        background.intersect(Area(Rectangle2D.Double(
            x.toDouble() + widthOffset,
            y.toDouble() + heightOffset,
            w.toDouble() - 2*widthOffset,
            80.0 - heightOffset*0.53
        )))
        background.subtract(profilePicture.outerCircle)

        g.color = bgCol
        g.fill(background)

        profilePicture.draw(g)

        g.color = Color.WHITE
        val glyph = archivoBlack.getGlyph(textSize-widthOffset*0.4, account.displayName.uppercase())
        g.drawGlyphVector(glyph.coreGlyph, x + 20f + widthOffset.toFloat()*0.93f, y + 135f + glyph.height/2 - heightOffset.toFloat()*0.87f)
    }

    fun getAverageColor(image: BufferedImage): Color {
        var r = 0
        var g = 0
        var b = 0
        var n = 1
        for (i in 0..<image.width) for (j in 0..<image.height) {
            val col = Color(image.getRGB(i, j))
            if (col.alpha < 64) continue
            r += col.red
            g += col.green
            b += col.blue
            n++
        }
        return Color(r/n, g/n, b/n, 255)
    }

    override fun execAction() {
        Panel.scene = LaunchFortniteScene(account.accountId, BasicTask { account.getExchangeToken() })
    }
}