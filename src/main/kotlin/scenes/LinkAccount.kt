package net.vanolex.scenes

import net.vanolex.Panel
import net.vanolex.epicapi.AsyncTask
import net.vanolex.epicapi.UserInitialisation
import net.vanolex.fonts.archivoBlack
import net.vanolex.fonts.archivoMedium
import net.vanolex.graphics.*
import net.vanolex.graphics.elements.Text
import java.awt.Color

class LinkAccount: Scene() {

    private var state = State.INFO
    var networkTask = UserInitialisation()

    override fun update() {
        if (state == State.FETCHING) {
            val status = networkTask.status
            if (status == AsyncTask.TaskStatus.WAITING || status ==  AsyncTask.TaskStatus.CANCELLED) {
                networkTask.launchTask()
                return
            }
            if (status == AsyncTask.TaskStatus.IN_PROGRESS) return
            if (status == AsyncTask.TaskStatus.FAILED) state = State.FAIL
            if (status == AsyncTask.TaskStatus.SUCCESS) {
                val titleglyph = archivoBlack.getGlyph(45, "HI, ${networkTask.displayName.uppercase()}!")
                successComposition = Composition(
                    Shade(200, 40, 600, 530, 40),
                    Text(titleglyph, (1000-titleglyph.width)/2, 265, Color.WHITE),
                    MultilineText("Login successful! Your account is now linked. You can save it to skip authentication next time or launch Fortnite without saving.", archivoMedium, 540, 18, 230, 340),
                    ProfilePicture(500, 155, networkTask.profileIcon),
                    SolidButton(220, 415, 560, 70, "SAVE", true) {},
                    SolidButton(220, 500, 560, 50, "LAUNCH", isPrimary = false) { Panel.scene = LaunchFortniteScene(networkTask.accountId, networkTask.exchangeToken) },
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

    val failComposition = CompositionBuilder.buildDialogue("UNEXPECTED ERROR",
        "An unexpected error occurred during the login process. Please try again.",
        primaryButtonText = "TRY AGAIN",
        primaryButtonAction = {state = State.FETCHING},
        secondaryButtonText = "CANCEL",
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    lateinit var successComposition: Composition

    override val composition get() = when(state) {
        State.INFO -> infoComposition
        State.FETCHING -> fetchingComposition
        State.SUCCESS -> successComposition
        State.FAIL -> failComposition
    }

    private enum class State {
        INFO, FETCHING, SUCCESS, FAIL
    }
}