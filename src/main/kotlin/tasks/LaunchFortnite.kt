package net.vanolex.tasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.vanolex.config


class LaunchFortnite(private val accountId: String, private val exchangeTokenTask: BasicTask<String>): Task() {
    override suspend fun task() {
        exchangeTokenTask.launchTaskSync()
        val exchangeToken = exchangeTokenTask.result

        withContext(Dispatchers.IO) {
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
        }

        delay(1000)
        status = TaskStatus.SUCCESS
    }
}
