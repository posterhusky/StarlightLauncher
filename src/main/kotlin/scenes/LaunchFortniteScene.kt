package net.vanolex.scenes

import net.vanolex.Panel
import net.vanolex.config
import net.vanolex.tasks.Task
import net.vanolex.tasks.LaunchFortnite
import net.vanolex.graphics.Composition
import net.vanolex.isRunning
import net.vanolex.tasks.BasicTask

class LaunchFortniteScene(accountId: String, exchangeTokenTask: BasicTask<String>): Scene() {
    constructor(accountId: String, exchangeToken: String): this(accountId, BasicTask { exchangeToken })

    override val isImportant = true

    private val launchTask = LaunchFortnite(accountId, exchangeTokenTask)

    private val loadingComposition = CompositionBuilder.buildDialogue("LAUNCHING...", "", true)
    private val failComposition = CompositionBuilder.buildDialogue("UNEXPECTED ERROR",
        "An unexpected error occurred during the login process. Please try again.",
        secondaryButtonText = "CANCEL",
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    private val successComposition = CompositionBuilder.buildDialogue("SUCCESS!",
        "Fortnite was launched! The window should appear in a few seconds.",
        primaryButtonText = "MAIN MENU",
        primaryButtonAction = { Panel.scene = MainMenuScene() },
        secondaryButtonText = "EXIT",
        secondaryButtonAction = { isRunning = false }
    )

    override val composition: Composition
        get() = when (launchTask.status) {
            Task.TaskStatus.FAILED -> failComposition
            Task.TaskStatus.SUCCESS -> successComposition
            else -> loadingComposition
        }

    override fun update() {
        if (launchTask.status == Task.TaskStatus.WAITING) launchTask.launchTaskAsync()
        if (launchTask.status == Task.TaskStatus.SUCCESS && config.closeAfterLaunch) isRunning = false
    }
}