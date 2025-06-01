package net.vanolex.scenes

import net.vanolex.tasks.Task
import net.vanolex.fonts.titleFont
import net.vanolex.fonts.titleFontItalic
import net.vanolex.graphics.*
import net.vanolex.graphics.accounts.CardScroller
import net.vanolex.graphics.elements.Text
import net.vanolex.lang
import net.vanolex.loadAccountsTask
import java.awt.Color

class MainMenuScene: Scene() {
    private val actionButton = SolidButton(650, 490, 285, 70, {cardScroller?.buttonText ?: lang.launch}, true, {cardScroller?.selectedCard == null}, {cardScroller?.selectedCard?.execAction()})
    private val refreshButton = SolidButton(780, 55, 150, 40, {lang.refresh}, SolidButton.Style.OPAQUE, {cardScroller == null}, { loadAccountsTask.launchTaskAsync(); cardScroller = null})
    private var cardScroller: CardScroller? = null

    private val loadingGlyph = titleFontItalic.getGlyph(40, lang.loadingAccounts)
    private val selectAccountGlyph = titleFont.getGlyph(30, lang.selectAccount)
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
                refreshButton,
                DynamicText({ titleFontItalic.getGlyph(40, cardScroller?.selText ?: "")}, {80}, {510}, Color.WHITE)
            )
        }

    override val composition: Composition
        get() = mainComp ?: loadingComp

    override fun update() {
        if (loadAccountsTask.status == Task.TaskStatus.SUCCESS && mainComp == null) cardScroller = CardScroller()
    }
}