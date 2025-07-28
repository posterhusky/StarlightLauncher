package net.vanolex.scenes

import net.vanolex.Panel
import net.vanolex.Version
import net.vanolex.isRunning
import net.vanolex.lang
import net.vanolex.tasks.DownloadUpdate
import net.vanolex.tasks.Task

class LauncherUpdate(val updateLink: String, val newUpdateName: String): Scene() {

    val downloadUpdateTask = DownloadUpdate(updateLink, newUpdateName)

    val questionComposition = CompositionBuilder.buildDialogue(
        lang.updateFound,
        lang.updateFoundLore.replace("%s", Version.CURRENT).replace("%t", newUpdateName),
        false, null,
        lang.update.replace("%s", newUpdateName.uppercase()), {downloadUpdateTask.launchTaskAsync()},
        lang.skip, {Panel.scene = MainMenuScene()}
    )

    val errorComposition = CompositionBuilder.buildDialogue(
        lang.unexpectedError, lang.unexpectedErrorLore,
        secondaryButtonText = lang.exit,
        secondaryButtonAction = { isRunning = false }
    )

    val updatingComposition = CompositionBuilder.buildDialogue(
        lang.updating, lang.updatingLore, true
    )

    override val composition get() = when (downloadUpdateTask.status) {
        Task.TaskStatus.WAITING -> questionComposition
        Task.TaskStatus.FAILED -> errorComposition
        else -> updatingComposition
    }

    override fun update() {
        if (downloadUpdateTask.status == Task.TaskStatus.SUCCESS) isRunning = false
    }
}