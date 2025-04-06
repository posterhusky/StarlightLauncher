package net.vanolex.scenes

import com.sun.net.httpserver.Authenticator.Success
import net.vanolex.Panel
import net.vanolex.epicapi.Task
import net.vanolex.epicapi.UserInitialisation
import net.vanolex.fonts.archivoBlack
import net.vanolex.fonts.archivoMedium
import net.vanolex.graphics.*
import net.vanolex.graphics.elements.Text
import net.vanolex.tasks.SaveAccount
import java.awt.Color

class LinkAccount: Scene() {

    private var state = State.INFO
    val userInitialisationTask = UserInitialisation()
    var saveAccountTask: SaveAccount? = null

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
                val titleglyph = archivoBlack.getGlyph(45, "HI, ${userInitialisationTask.displayName.uppercase()}!")
                saveAccountTask = SaveAccount(userInitialisationTask.accountId, userInitialisationTask.exchangeToken, userInitialisationTask.profilePicture, userInitialisationTask.displayName)
                successComposition = Composition(
                    Shade(200, 40, 600, 530, 40),
                    Text(titleglyph, (1000-titleglyph.width)/2, 265, Color.WHITE),
                    MultilineText("Login successful! Your account is now linked. You can save it to skip authentication next time or launch Fortnite without saving.", archivoMedium, 540, 18, 230, 340),
                    ProfilePicture(500, 155, userInitialisationTask.profilePicture),
                    SolidButton(220, 415, 560, 70, "SAVE", true) {saveAccountTask?.launchTaskAsync()},
                    SolidButton(220, 500, 560, 50, "LAUNCH", isPrimary = false) { Panel.scene = LaunchFortniteScene(userInitialisationTask.accountId, userInitialisationTask.exchangeToken) },
                )
                state = State.SUCCESS
            }
        }
    }

    val infoComposition = CompositionBuilder.buildDialogue("LINK YOUR ACCOUNT",
        "To sign into Fortnite, you'll need to link your Epic account. Continuing will open a browser window " +
            "directing you to the official Epic Games website where you can complete the sign-in process.",
        primaryButtonText = "CONTINUE",
        primaryButtonAction = {state = State.FETCHING},
        secondaryButtonText = "CANCEL",
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    val fetchingComposition = CompositionBuilder.buildDialogue("WAITING FOR LOGIN...",
        "The Epic Games login page was opened in your browser. Once you've signed in, this window will refresh automatically.",
        hasSpinner = true,
        secondaryButtonText = "CANCEL",
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    val savingingComposition = CompositionBuilder.buildDialogue("SAVING ACCOUNT...",
        "Your account is being saved, please wait a few seconds...",
        hasSpinner = true
    )

    val savedComposition = CompositionBuilder.buildDialogue("ACCOUNT SAVED",
        "Your account has been saved successfully! You can now go to the main menu and launch the game from there.",
        primaryButtonText = "CONTINUE",
        primaryButtonAction = {Panel.scene = MainMenuScene()}
    )

    val failComposition = CompositionBuilder.buildDialogue("UNEXPECTED ERROR",
        "An unexpected error occurred during the process. Please try again.",
        secondaryButtonText = "CANCEL",
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    lateinit var successComposition: Composition

    override val composition get() = when(state) {
        State.INFO -> infoComposition
        State.FETCHING -> fetchingComposition
        State.SUCCESS -> when (saveAccountTask?.status) {
            Task.TaskStatus.IN_PROGRESS -> savingingComposition
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