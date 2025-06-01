package net.vanolex.tasks

import com.google.gson.JsonObject
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import net.vanolex.*


class LaunchFortnite(private val accountId: String, private val exchangeTokenTask: BasicTask<String>): Task() {
    override suspend fun task() {
        exchangeTokenTask.launchTaskSync()
        val tempExchangeToken = exchangeTokenTask.result

        var response = client.post("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token") {
            headers {
                append("Content-Type", "application/x-www-form-urlencoded")
                append("Authorization", "basic $launcherAuth")
            }
            setBody("grant_type=exchange_code&exchange_code=$tempExchangeToken")
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        var jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)
        val accessToken = jsonResponse["access_token"].asString
        response = client.get("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/exchange") {
            headers {
                append("Authorization", "bearer $accessToken")
            }
            setBody("consumingClientId=ec684b8c687f479fadea3cb2ad83f5c6")
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)
        val exchangeToken = jsonResponse["code"].asString

        println(exchangeToken)

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
