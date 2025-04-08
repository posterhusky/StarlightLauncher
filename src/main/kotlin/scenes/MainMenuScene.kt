package net.vanolex.scenes

import net.vanolex.tasks.Task
import net.vanolex.fonts.archivoBlack
import net.vanolex.fonts.archivoBlackItalic
import net.vanolex.graphics.*
import net.vanolex.graphics.accounts.CardScroller
import net.vanolex.graphics.elements.Text
import net.vanolex.loadAccountsTask
import java.awt.Color

class MainMenuScene: Scene() {
    private val actionButton = SolidButton(650, 490, 285, 70, {cardScroller?.buttonText ?: "LAUNCH"}, true, {cardScroller?.selectedCard == null}, {cardScroller?.selectedCard?.execAction()})
    private var cardScroller: CardScroller? = null

    private val loadingGlyph = archivoBlackItalic.getGlyph(40, "LOADING ACCOUNTS...")
    private val selectAccountGlyph = archivoBlack.getGlyph(30, "SELECT ACCOUNT:")
    private val loadingComp = Composition(
        Shade(50, 40, 900, 410, 40),
        Shade(50, 475, 900, 100, 40),
        actionButton,
        LoadingSpinner(500, 200),
        Text(loadingGlyph, (1000 - loadingGlyph.width) / 2, 300, Color.WHITE)
    )

    private val mainComp: Composition?
        get() {
            return Composition(
                Shade(50, 40, 900, 410, 40),
                Shade(50, 475, 900, 100, 40),
                cardScroller ?: return null,
                Text(selectAccountGlyph, 70, 65, Color.WHITE),
                actionButton,
                DynamicText({ archivoBlackItalic.getGlyph(40, cardScroller?.selText ?: "")}, {80}, {510}, Color.WHITE)
            )
        }

    override val composition: Composition
        get() = mainComp ?: loadingComp

    override fun update() {
        if (loadAccountsTask.status == Task.TaskStatus.SUCCESS && mainComp == null) cardScroller = CardScroller()
    }
}