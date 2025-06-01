package net.vanolex

import com.google.gson.JsonObject
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import net.vanolex.tasks.EpicAPIException
import net.vanolex.tasks.ProfilePictureLoader
import java.awt.image.BufferedImage

class Account(
    val deviceId: String,
    val accountId: String,
    val secret: String,
) {
    lateinit var profilePicture: BufferedImage
    lateinit var displayName: String

    suspend fun init(): Account {
        val jsonResponse = getAccessToken()

        val accessToken = jsonResponse["access_token"].asString
        displayName = jsonResponse["displayName"].asString
        profilePicture = ProfilePictureLoader.getProfileIcon(accessToken, accountId)
        return this
    }

    suspend fun getExchangeToken(): String {
        var jsonResponse = getAccessToken()
        val accessToken = jsonResponse["access_token"].asString
        val response = client.get("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/exchange") {
            headers {
                append("Authorization", "bearer $accessToken")
            }
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)
        return jsonResponse["code"].asString
    }

    private suspend fun getAccessToken(): JsonObject {
        val response = client.post("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token") {
            headers {
                append("Content-Type", "application/x-www-form-urlencoded")
                append("Authorization", "basic $androidAuth")
            }
            setBody("grant_type=device_auth&account_id=$accountId&device_id=$deviceId&secret=$secret&token_type=eg1")
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        return gson.fromJson(response.bodyAsText(), JsonObject::class.java)
    }

    companion object {
        fun saveAccounts() {
            println("save")
            val jsonString = gson.toJson(accounts.map {
                mapOf(
                    Pair("deviceId", it.deviceId),
                    Pair("accountId", it.accountId),
                    Pair("secret", it.secret)
                )
            })
            val file = loadFile("accounts.json")
            file.writeText(jsonString)
        }
    }
}