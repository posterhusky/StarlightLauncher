package net.vanolex.scenes

import net.vanolex.Panel
import net.vanolex.config
import net.vanolex.tasks.Task
import net.vanolex.tasks.LaunchFortnite
import net.vanolex.graphics.Composition
import net.vanolex.isRunning
import net.vanolex.lang
import net.vanolex.tasks.BasicTask

class LaunchFortniteScene(accountId: String, exchangeTokenTask: BasicTask<String>): Scene() {
    constructor(accountId: String, exchangeToken: String): this(accountId, BasicTask { exchangeToken })

    override val isImportant = true

    private val launchTask = LaunchFortnite(accountId, exchangeTokenTask)

    private val loadingComposition = CompositionBuilder.buildDialogue(lang.launching, "", true)
    private val failComposition = CompositionBuilder.buildDialogue(lang.unexpectedError,
        lang.unexpectedErrorLore,
        secondaryButtonText = lang.cancel,
        secondaryButtonAction = { Panel.scene = MainMenuScene() }
    )

    private val successComposition = CompositionBuilder.buildDialogue(lang.launched,
        lang.launchedLore,
        primaryButtonText = lang.mainMenu,
        primaryButtonAction = { Panel.scene = MainMenuScene() },
        secondaryButtonText = lang.exit,
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