package net.vanolex.scenes

import net.vanolex.Panel
import net.vanolex.tasks.Task
import net.vanolex.tasks.UserInitialisation
import net.vanolex.fonts.titleFont
import net.vanolex.fonts.paragraphFont
import net.vanolex.graphics.*
import net.vanolex.graphics.elements.Text
import net.vanolex.lang
import net.vanolex.tasks.SaveAccount
import java.awt.Color

class LinkAccount: Scene() {

    private var state = State.INFO
    private val userInitialisationTask = UserInitialisation()
    private var saveAccountTask: SaveAccount? = null

    override fun update() {
        if (state == State.FETCHING) {
            val status = userInitialisationTask.status
            if (status == Task.TaskStatus.WAITING || status ==  Task.TaskStatus.CANCELLED) {
                userInitialisationTask.launchTaskAsync()
                return
            }
            if (status == Task.TaskStatus.IN_PROGRESS) return
            if (status == Task.TaskStatus.FAILED) state = State.FAIL
            if (status == Task.TaskStatus.SUCCESS) {
                val titleGlyph = titleFont.getGlyph(45, lang.linked.replace("%s", userInitialisationTask.displayName.uppercase()))
                saveAccountTask = SaveAccount(userInitialisationTask.accountId, userInitialisationTask.exchangeToken, userInitialisationTask.profilePicture, userInitialisationTask.displayName)
                successComposition = Composition(
                    Shade(200, 40, 600, 530, 40),
                    Text(titleGlyph, (1000-titleGlyph.width)/2, 265, Color.WHITE),
                    MultilineText(lang.linkedLore, paragraphFont, 540, 18, 230, 340),
                    ProfilePicture(500, 155, userInitialisationTask.profilePicture),
                    SolidButton(220, 415, 560, 70, lang.save, true) {saveAccountTask?.launchTaskAsync()},
                    SolidButton(220, 500, 560, 50, lang.launch, isPrimary = false) { Panel.scene = LaunchFortniteScene(userInitialisationTask.accountId, userInitialisationTask.exchangeToken) },
                )
                state = State.SUCCESS
            }
        }
    }

    private val infoComposition = CompositionBuilder.buildDialogue(lang.linkAccount,
        lang.linkAccountLore,
        primaryButtonText = lang.next,
        primaryButtonAction = {state = State.FETCHING},
        secondaryButtonText = lang.cancel,
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    private val fetchingComposition = CompositionBuilder.buildDialogue(lang.waitingLogin,
        lang.waitingLoginLore,
        hasSpinner = true,
        secondaryButtonText = lang.cancel,
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    private val savingComposition = CompositionBuilder.buildDialogue(lang.saving,
        lang.savingLore,
        hasSpinner = true
    )

    private val savedComposition = CompositionBuilder.buildDialogue(lang.saved,
        lang.savedLore,
        primaryButtonText = lang.next,
        primaryButtonAction = {Panel.scene = MainMenuScene()}
    )

    private val failComposition = CompositionBuilder.buildDialogue(lang.unexpectedError,
        lang.unexpectedErrorLore,
        secondaryButtonText = lang.cancel,
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    private lateinit var successComposition: Composition

    override val composition get() = when(state) {
        State.INFO -> infoComposition
        State.FETCHING -> fetchingComposition
        State.SUCCESS -> when (saveAccountTask?.status) {
            Task.TaskStatus.IN_PROGRESS -> savingComposition
            Task.TaskStatus.SUCCESS -> savedComposition
            Task.TaskStatus.FAILED, null -> failComposition
            else -> successComposition
        }
        State.FAIL -> failComposition
    }

    private enum class State {
        INFO, FETCHING, SUCCESS, FAIL
    }
}