package net.vanolex.epicapi

import com.sun.tools.javac.tree.TreeInfo.args
import kotlinx.coroutines.delay
import net.vanolex.config
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*


class LaunchFortnite(val accountId: String, val exchangeToken: String): AsyncTask() {
    override suspend fun task() {
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
