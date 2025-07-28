package net.vanolex.scenes

import net.vanolex.*
import net.vanolex.tasks.Task
import net.vanolex.fonts.introFont
import net.vanolex.fonts.nougatFont
import net.vanolex.fonts.paragraphFont
import net.vanolex.graphics.Composition
import net.vanolex.graphics.elements.ScreenFill
import net.vanolex.graphics.elements.Text
import net.vanolex.tasks.FindFortnite
import net.vanolex.tasks.LoadConfig
import net.vanolex.tasks.CheckUpdates
import java.awt.Color
import kotlin.math.abs

class LogoScene: Scene() {
    override val isImportant = true

    private val presentsGlyph = introFont.getGlyph(50f, "PRESENTS")

    private val loadConfigTask = LoadConfig()
    private val findFortniteTask = FindFortnite()
    private val checkUpdatesTask = CheckUpdates()

    override fun update() {
        if (loadConfigTask.status == Task.TaskStatus.WAITING) {
            loadConfigTask.launchTaskAsync()
            return
        }

        if (loadConfigTask.status != Task.TaskStatus.SUCCESS) return

        if (loadAccountsTask.status == Task.TaskStatus.WAITING) loadAccountsTask.launchTaskAsync()
        if (findFortniteTask.status == Task.TaskStatus.WAITING) findFortniteTask.launchTaskAsync()
        if (config.checkUpdates && checkUpdatesTask.status == Task.TaskStatus.WAITING) checkUpdatesTask.launchTaskAsync()
    }

    override val composition: Composition
        get() {
            // Ctrl+C Ctrl+V from Minecraft Dungeons: Battle
            val sceneFrames120 = frames.toInt()*2
            val composition = Composition(
                ScreenFill(Color(25, 26, 48)),
                Text(paragraphFont.getGlyph(12, "v${Version.CURRENT}"), 5, 585, Color.WHITE),
            )

            if (sceneFrames120 > 560) {
                if (checkUpdatesTask.status == Task.TaskStatus.IN_PROGRESS) return composition

                val linkCandidate = checkUpdatesTask.updateLink
                if (linkCandidate != null) Panel.scene = LauncherUpdate(linkCandidate, checkUpdatesTask.newVersionName)
                if (findFortniteTask.status == Task.TaskStatus.SUCCESS) Panel.scene = MainMenuScene()
                if (findFortniteTask.status == Task.TaskStatus.FAILED) Panel.scene = SelectFortniteDialogue()

//                Panel.scene = SelectFortniteDialogue()
                return composition
            }

            if (sceneFrames120 > 500) return composition
            if (sceneFrames120 < 60) return composition

            val color = if (sceneFrames120 < 220)
                Color(255,255,255, ((sceneFrames120-60)*255)/160)
            else if (sceneFrames120 > 320)
                Color(255,255,255, abs(255 - ((sceneFrames120-320)*255)/180))
            else
                Color.WHITE

            val vanolexGlyph = nougatFont.getGlyph((40+sceneFrames120/40f)* 2.5, "VANOLEX")

            composition.addElements(
                Text(vanolexGlyph, 500 - vanolexGlyph.width/2.0, 300 - vanolexGlyph.height + 25, color),
                Text(presentsGlyph, 500 - presentsGlyph.width/2.0, 300 + 50, color)
            )

            return composition
        }

}