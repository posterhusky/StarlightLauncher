package net.vanolex.tasks

import com.google.gson.JsonObject
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import net.vanolex.*
import net.vanolex.epicapi.EpicAPIException
import net.vanolex.epicapi.Task
import java.awt.image.BufferedImage

class SaveAccount(val accountId: String, val exchangeToken: String, val profilePicture: BufferedImage, val displayName: String): Task() {
    override suspend fun task() {
        if (!isAccountsInitialized) accounts = mutableListOf()
        for (i in accounts) {
            if (i.accountId != accountId) continue
            status = TaskStatus.SUCCESS
            return
        }

        var response: HttpResponse
        var jsonResponse: JsonObject

        response = client.post("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token") {
            headers {
                append("Content-Type", "application/x-www-form-urlencoded")
                append("Authorization", "basic $androidAuth")
            }
            setBody("grant_type=exchange_code&exchange_code=$exchangeToken")
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)
        val accessToken = jsonResponse["access_token"].asString

        response = client.post("https://account-public-service-prod.ol.epicgames.com/account/api/public/account/$accountId/deviceAuth") {
            headers {
                append("Content-Type", "application/json")
                append("Authorization", "bearer $accessToken")
            }
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)
        val deviceId = jsonResponse["deviceId"].asString
        val secret = jsonResponse["secret"].asString

        val newAccount = Account(deviceId, accountId, secret)
        newAccount.profilePicture = profilePicture
        newAccount.displayName = displayName
        accounts.add(newAccount)
        Account.saveAccounts()

        status = TaskStatus.SUCCESS
    }
}