package net.vanolex.graphics.buttons.cards

import net.vanolex.Panel
import net.vanolex.fonts.titleFont
import net.vanolex.graphics.CardScroller
import net.vanolex.lang
import net.vanolex.scenes.LinkAccount
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.RoundRectangle2D

class LinkAccountCard(
    override var x: Int, override var y: Int, override val parent: CardScroller
): Card() {
    override val selText = lang.linkAccountShort
    override val buttonText = lang.linkAccountAction

    override fun cardDraw(g: Graphics2D, cardShape: Area) {
        val plusCircle = Area( Ellipse2D.Double(
            x + 108 + widthOffset*0.2,
            y + 43 + heightOffset * 0.3 - yOffset,
            54 - widthOffset*0.4,
            54 - widthOffset*0.4,
        ))
        val plus = Area( RoundRectangle2D.Double(
            x + 123.0,
            y + 66.0 - yOffset,
            24.0,
            8.0,
            3.0, 3.0
        ))
        plus.add(Area( RoundRectangle2D.Double(
            x + 131.0,
            y + 58.0 - yOffset,
            8.0,
            24.0,
            3.0, 3.0
        )))
        g.fill(plusCircle)
        g.color = Color.WHITE
        g.fill(plus)

        g.color = Color.WHITE
        val glyph = titleFont.getGlyph(25-widthOffset*0.4, lang.linkAccountShort, w - 2*widthOffset - 40)
        g.drawGlyphVector(glyph.coreGlyph, x + 135f - glyph.width.toFloat()/2f, y + 140f - heightOffset.toFloat()*0.87f - yOffset)
    }

    override fun execAction() {
        Panel.scene = LinkAccount()
    }
}