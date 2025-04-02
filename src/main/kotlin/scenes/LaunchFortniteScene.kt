package net.vanolex.scenes

import net.vanolex.Panel
import net.vanolex.epicapi.AsyncTask
import net.vanolex.epicapi.LaunchFortnite
import net.vanolex.graphics.Composition
import net.vanolex.isRunning

class LaunchFortniteScene(val accountId: String, val exchangeToken: String): Scene() {

    val launchTask = LaunchFortnite(accountId, exchangeToken)

    val loadingComposition = CompositionBuilder.buildDialogue("LAUNCHING...", "", true)
    val failComposition = CompositionBuilder.buildDialogue("UNEXPECTED ERROR",
        "An unexpected error occurred during the login process. Please try again.",
        secondaryButtonText = "CANCEL",
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    val successComposition = CompositionBuilder.buildDialogue("SUCCESS!",
        "Fortnite was launched! The window should appear in a few seconds.",
        primaryButtonText = "MAIN MENU",
        primaryButtonAction = { Panel.scene = MainMenuScene() },
        secondaryButtonText = "EXIT",
        secondaryButtonAction = { isRunning = false }
    )

    override val composition: Composition
        get() = when (launchTask.status) {
            AsyncTask.TaskStatus.FAILED -> failComposition
            AsyncTask.TaskStatus.SUCCESS -> successComposition
            else -> loadingComposition
        }

    override fun update() {
        if (launchTask.status == AsyncTask.TaskStatus.WAITING) launchTask.launchTask()
    }
}