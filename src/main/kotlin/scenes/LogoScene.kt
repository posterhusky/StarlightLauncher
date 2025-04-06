package net.vanolex.scenes

import net.vanolex.Panel
import net.vanolex.epicapi.Task
import net.vanolex.fonts.introFont
import net.vanolex.fonts.nougatFont
import net.vanolex.frames
import net.vanolex.graphics.Composition
import net.vanolex.graphics.elements.ScreenFill
import net.vanolex.graphics.elements.Text
import net.vanolex.loadAccountsTask
import net.vanolex.tasks.FindFortnite
import net.vanolex.tasks.LoadConfig
import java.awt.Color
import kotlin.math.abs

class LogoScene: Scene() {
    override val isImportant = true

    val presentsGlyph = introFont.getGlyph(50f, "PRESENTS")

    val loadConfigTask = LoadConfig()
    val findFortniteTask = FindFortnite()

    override fun update() {
        if (loadAccountsTask.status == Task.TaskStatus.WAITING) loadAccountsTask.launchTaskAsync()

        if (loadConfigTask.status == Task.TaskStatus.WAITING) {
            loadConfigTask.launchTaskAsync()
            return
        }

        if (loadConfigTask.status != Task.TaskStatus.SUCCESS) return

        if (findFortniteTask.status == Task.TaskStatus.WAITING) findFortniteTask.launchTaskAsync()
    }

    override val composition: Composition
        get() {
            // Ctrl+C Ctrl+V from Minecraft Dungeons: Battle
            val sceneFrames120 = frames.toInt()*2
            val composition = Composition(
                ScreenFill(Color(25, 26, 48))

            )

            if (sceneFrames120 > 560) {
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