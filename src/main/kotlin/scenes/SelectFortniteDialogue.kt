package net.vanolex.scenes

import jnafilechooser.api.JnaFileChooser
import net.vanolex.Panel
import net.vanolex.Window
import net.vanolex.config
import net.vanolex.fonts.archivoBlack
import net.vanolex.fonts.archivoMedium
import net.vanolex.graphics.*
import net.vanolex.graphics.elements.Text
import java.awt.Color
import java.awt.Image

class SelectFortniteDialogue: Scene() {

    var page = 1

    var error = ""

    val pageTexts = arrayOf(
        "We couldn't automatically detect the location of Fortnite. Please select the \"FortniteLauncher.exe\" file by clicking the SELECT FILE button. If you're unsure of the path, you can find instructions on the next pages.",
        "Open the Epic Games Launcher and select \"Library\" from the sidebar. Find the Fortnite card in your Library, click the three dots next to the game title, and from the popup menu, select \"Manage\".",
        "In the opened window, click the folder icon next to \"Installation\". A file explorer window will appear with the Fortnite folder open, copy the patch from the search bar on the top.",
        "Return to the Starlight Launcher and click the \"SELECT FILE\" button. In the file explorer window that appears, paste the copied path into the search bar and press enter.",
        "Go to FortniteGame > Binaries > Win64, select the \"FortniteLauncher.exe\" file and click \"Open\". After this you will be redirected to the main menu.",
    )

    val pageImages = arrayOf<Image?>(
        null, null, null, null, null
    )

    val mainComp = CompositionBuilder.stepDialogue(
        "FORTNITE NOT FOUND",
        {pageTexts[(page-1)%5]},
        {pageImages[(page-1)%5]},
        5, this
    )

    val errComp = Composition(
        Shade(200, 40, 600, 530, 40),
        Text(archivoBlack.getGlyph(45, "ERROR"), (1000-archivoBlack.getGlyph(45, "ERROR").width)/2, 90, Color.WHITE),
        DynamicMultilineText({error}, archivoMedium, 540, 18, 230, 180),
        SolidButton(220, 480, 560, 70, "CONTINUE", isPrimary = false) { error = "" },
    )

    override val composition
        get() = if (error == "") mainComp
                else errComp

    override fun update() {}

    fun openExplorer() {
        val fc = JnaFileChooser().apply {
            setTitle("Select Fortnite Location")
            mode = JnaFileChooser.Mode.Files
            addFilter("Executable Files", "exe")
        }
        if (!fc.showOpenDialog(Window)) return
        val file = fc.selectedFile ?: return
        if (!file.exists()) {
            error = "While opening a file the following error occured: The file that you selected does not exist anymore. Please try again."
            return
        }
        if (!file.absolutePath.replace("\\", "/").endsWith("FortniteLauncher.exe")) {
            error = "While opening a file the following error occured: The file that you selected doesn't seem to be the fortnite launcher. Make sure that you select the \"FortniteLauncher.exe\" file."
            return
        }
        if (!file.absolutePath.replace("\\", "/").endsWith("Fortnite/FortniteGame/Binaries/Win64/FortniteLauncher.exe")) {
            error = "While opening a file the following error occured: The file that you selected doesn't seem to be in the Fortnite folder. Make sure that the path to this file ends with \"Fortnite/FortniteGame/Binaries/Win64/FortniteLauncher.exe\"."
            return
        }
        config.fortnitePath = file.absolutePath
        config.save()
        Panel.scene = LinkAccount()
    }

}