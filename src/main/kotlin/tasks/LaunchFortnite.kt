package net.vanolex.epicapi

import kotlinx.coroutines.delay
import net.vanolex.config
import net.vanolex.tasks.BasicTask


class LaunchFortnite(val accountId: String, val exchangeTokenTask: BasicTask<String>): Task() {
    override suspend fun task() {
        exchangeTokenTask.launchTaskSync()
        val exchangeToken = exchangeTokenTask.result

        ProcessBuilder(
            "cmd.exe",
            "/C",
            "start",
            "/d",
            config.fortnitePath.removeSuffix("FortniteLauncher.exe"),
            "FortniteLauncher.exe",
            "-AUTH_LOGIN=unused",
            "-AUTH_PASSWORD=$exchangeToken",
            "-AUTH_TYPE=exchangecode",
            "-epicapp=Fortnite",
            "-epicenv=Prod",
            "-EpicPortal",
            "-epicuserid=$accountId"
        ).start()

        delay(1000)
        status = TaskStatus.SUCCESS
    }
}
