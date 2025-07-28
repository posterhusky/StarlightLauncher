package net.vanolex.scenes

import jnafilechooser.api.JnaFileChooser
import net.vanolex.Panel
import net.vanolex.Window
import net.vanolex.config
import net.vanolex.fonts.titleFont
import net.vanolex.fonts.paragraphFont
import net.vanolex.graphics.*
import net.vanolex.graphics.buttons.SolidButton
import net.vanolex.graphics.elements.Text
import net.vanolex.lang
import java.awt.Color

class SelectFortniteDialogue: Scene() {

    var page = 1

    private var error = ""

    private val pageTexts = arrayOf(
        lang.findFortnite1,
        lang.findFortnite2,
        lang.findFortnite3,
        lang.findFortnite4,
        lang.findFortnite5,
    )

    private val mainComp = CompositionBuilder.stepDialogue(
        lang.fortniteNotFound,
        {pageTexts[(page-1)%5]},
        {null},
        5, this
    )

    private val errComp = Composition(
        Shade(200, 40, 600, 530, 40),
        Text(titleFont.getGlyph(45, lang.error), (1000-titleFont.getGlyph(45, lang.error).width)/2, 90, Color.WHITE),
        DynamicMultilineText({error}, paragraphFont, 540, 18, 230, 180),
        SolidButton(220, 480, 560, 70, lang.next, isPrimary = false) { error = "" },
    )

    override val composition
        get() = if (error == "") mainComp
                else errComp

    override fun update() {}

    fun openExplorer() {
        val fc = JnaFileChooser().apply {
            setTitle(lang.selectFortniteLocation)
            mode = JnaFileChooser.Mode.Files
            addFilter(lang.exeFileFilter, "exe")
        }
        if (!fc.showOpenDialog(Window)) return
        val file = fc.selectedFile ?: return
        if (!file.exists()) {
            error = lang.fileDoesntExist
            return
        }
        if (!file.absolutePath.replace("\\", "/").endsWith("FortniteLauncher.exe")) {
            error = lang.wrongFile
            return
        }
        if (!file.absolutePath.replace("\\", "/").endsWith("Fortnite/FortniteGame/Binaries/Win64/FortniteLauncher.exe")) {
            error = lang.wrongPath
            return
        }
        config.fortnitePath = file.absolutePath
        config.save()
        Panel.scene = MainMenuScene()
    }

}