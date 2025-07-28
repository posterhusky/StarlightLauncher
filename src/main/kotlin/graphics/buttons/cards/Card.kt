package net.vanolex.graphics.buttons.cards

import net.vanolex.graphics.CardScroller
import net.vanolex.graphics.buttons.Button
import net.vanolex.localMousePosition
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.RoundRectangle2D
import kotlin.math.*

abstract class Card: Button() {
    abstract val parent: CardScroller
    abstract val selText: String
    abstract val buttonText: String
    abstract fun cardDraw(g: Graphics2D, cardShape: Area)
    abstract fun execAction()

    override var w = 270
    override var h = 170

    override val clickAction = {
        if (!isSelected) select()
        else execAction()
    }

    override val isHovered get() = localMousePosition.x in max(65, x)..<min(935, x+w) && localMousePosition.y + yOffset in max(105 + yOffset, y)..<min(435 + yOffset, y+h)

    override val yOffset
        get() = parent.scrollOffset.roundToInt()
    override val isHidden: Boolean
        get() = y - yOffset < -70 || y - yOffset > 440

    val isSelected: Boolean
        get() = parent.selectedCard == this
    fun select() { parent.selectedCard = this }

    override fun contentDraw(g: Graphics2D, buttonShape: Area) {
        cardDraw(g, buttonShape)

        if (isSelected) {
            val outline = Area(buttonShape)
            outline.subtract(
                Area(
                    RoundRectangle2D.Double(
                        x.toDouble() + 3 + widthOffset,
                        y.toDouble() + 3 + heightOffset - yOffset,
                        w.toDouble() - 6 - 2*widthOffset,
                        h.toDouble() - 6 - 2*heightOffset,
                        24.0,
                        24.0
                    ))
            )

            g.color = Color(246, 255, 19)
            g.fill(outline)
        }
    }
}