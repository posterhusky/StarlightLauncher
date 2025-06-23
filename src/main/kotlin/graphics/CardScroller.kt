package net.vanolex.graphics

import net.vanolex.Account
import net.vanolex.accounts
import net.vanolex.graphics.buttons.cards.AccountCard
import net.vanolex.graphics.buttons.cards.Card
import net.vanolex.graphics.buttons.cards.LinkAccountCard
import net.vanolex.lang
import net.vanolex.localMousePosition
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import java.awt.geom.RoundRectangle2D
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class CardScroller(): Element() {
    var selectedCard: Card? = null
    var scrollOffset: Double = 0.0
    var scrollOffsetTarget: Int = 0

    val selText get() = (selectedCard?.selText ?: "").uppercase()
    val buttonText get() = (selectedCard?.buttonText ?: lang.launch).uppercase()

    val isHovered: Boolean
        get() = localMousePosition.x in 65..935 && localMousePosition.y in 105..435

    val cardList = accounts.mapIndexed<Account, Card> { index, account ->
        AccountCard(80 + (index%3)*285, 120 + (index/3)*185, account, this)
    }.toMutableList().also { it.add(LinkAccountCard(80 + (it.size%3)*285, 120 + (it.size/3)*185, this)) }

    val maxScroll = (-315 + ((cardList.size+2)/3)*185)

    val containerClip = Area(RoundRectangle2D.Double(
        65.0, 105.0, 870.0, 330.0, 40.0, 40.0
    ))

    override fun draw(g: Graphics2D) {
        val oldTransform = g.transform
        val oldClip = g.clip

        g.clip = containerClip
        g.color = Color(0, 0, 0, 64)
        g.fill(containerClip)

        g.transform = AffineTransform.getTranslateInstance(0.0, (-scrollOffset).roundToInt().toDouble())

        cardList.forEach { (it as Element).draw(g) }

        g.transform = oldTransform

        if (scrollOffset > 0) {
            val opMod = min(scrollOffset/30.0, 1.0)
            for (i in 0..<50) {
                g.color = Color(0, 0, 0, ((150 - i*3)*opMod).roundToInt())
                g.fill(Rectangle2D.Double(65.0, 105.0 + i, 870.0, 1.0))
            }
        }

        if (scrollOffset < maxScroll) {
            for (i in 0..<50) {
                val opMod = max(min((maxScroll-30-scrollOffset)/30.0, 1.0), 0.0)
                g.color = Color(0, 0, 0, (i*3*opMod).roundToInt())
                g.fill(Rectangle2D.Double(65.0, 385.0 + i, 870.0, 1.0))
            }
        }

        g.clip = oldClip
    }

    override fun tick() {
        cardList.forEach { (it as Element).tick() }

        val threshold = 0.2
        if (abs(scrollOffset - scrollOffsetTarget) < threshold) {
            scrollOffset = scrollOffsetTarget.toDouble()
        } else {
            val smoothingFactor = 0.2
            scrollOffset += (scrollOffsetTarget - scrollOffset) * smoothingFactor
        }
    }

    override fun onClick() {
        cardList.forEach { (it as Element).onClick() }
    }

    override fun onScroll(amount: Int) {
        if (!isHovered) return
        scrollOffsetTarget += amount*15
        scrollOffsetTarget = max(min(scrollOffsetTarget, maxScroll), 0)
    }
}