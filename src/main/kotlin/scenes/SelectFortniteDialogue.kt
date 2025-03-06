package net.vanolex.scenes

import net.vanolex.tasks.SelectFileTask

class SelectFortniteDialogue: Scene() {

    var page = 1

    val explorerTask = SelectFileTask()

    val step1Composition = CompositionBuilder.stepDialogue(
        "FORTNITE NOT FOUND",
        "We couldn't automatically detect the location of Fortnite. Please select the Fortnite folder by clicking the SELECT FOLDER button. If you're unsure of the path, you can find instructions on the next pages.",
        null, 1, 5, this)

    val step2Composition = CompositionBuilder.stepDialogue(
        "FORTNITE NOT FOUND",
        "Open the Epic Games Launcher and select \"Library\" from the sidebar.",
        null, 2, 5, this)

    val step3Composition = CompositionBuilder.stepDialogue(
        "FORTNITE NOT FOUND",
        "Find the Fortnite card in your Library, click the three dots next to the game title, and from the popup menu, select \"Manage\".",
        null, 3, 5, this)

    val step4Composition = CompositionBuilder.stepDialogue(
        "FORTNITE NOT FOUND",
        "Find the Fortnite card in your Library, click the three dots next to the game title, and from the popup menu, select \"Manage\".",
        null, 4, 5, this)

    val step5Composition = CompositionBuilder.stepDialogue(
        "FORTNITE NOT FOUND",
        "Return to the Starlight Launcher and click the \"SELECT FOLDER\" button. In the file explorer window that appears, paste the copied path into the search bar at the top and confirm your selection.",
        null, 5, 5, this)

    override val composition get() = when (page) {
        2 -> step2Composition
        3 -> step3Composition
        4 -> step4Composition
        5 -> step5Composition
        else -> step1Composition
    }

    override fun update() {}

}