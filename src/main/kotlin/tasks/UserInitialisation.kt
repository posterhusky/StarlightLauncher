package net.vanolex.tasks

import com.google.gson.JsonObject
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import net.vanolex.Window
import net.vanolex.client
import net.vanolex.gson
import net.vanolex.switchAuth
import java.awt.Desktop
import java.awt.image.BufferedImage
import java.net.URI

class UserInitialisation: Task() {

    lateinit var exchangeToken: String
    lateinit var accountId: String
    lateinit var displayName: String
    lateinit var profilePicture: BufferedImage

    override suspend fun task() {
        var response: HttpResponse
        var jsonResponse: JsonObject

        // Authorising as a switch client
        response = client.post("https://account-public-service-prod.ol.epicgames.com/account/api/oauth/token") {
            headers {
                append("Content-Type", "application/x-www-form-urlencoded")
                append("Authorization", "basic $switchAuth")
            }
            setBody("grant_type=client_credentials")
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)
        val switchToken = jsonResponse["access_token"].asString

        // Request login page
        response = client.post("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/deviceAuthorization") {
            headers {
                append("Content-Type", "application/x-www-form-urlencoded")
                append("Authorization", "bearer $switchToken")
            }
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)

        val uri = jsonResponse["verification_uri_complete"].asString
        val deviceCode = jsonResponse["device_code"].asString

        withContext(Dispatchers.IO) {
            Desktop.getDesktop().browse(URI(uri))
        }

        // Wait until response is 200 or an unexpected error happens
        while (true) {
            delay(9000) // wait 8 sec
            while (!Window.isFocused) {
                delay(1000) // wait 1 sec
            }
            delay(1500) // wait 1.5 sec
            response = client.post("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token") {
                headers {
                    append("Content-Type", "application/x-www-form-urlencoded")
                    append("Authorization", "basic $switchAuth")
                }
                setBody("grant_type=device_code&device_code=$deviceCode&token_type=eg1")
            }
            jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)

            if (response.status == HttpStatusCode.OK) break

            if (jsonResponse["errorCode"].asString in mutableListOf(
                "errors.com.epicgames.account.oauth.authorization_pending",
                "errors.com.epicgames.not_found"
            )) continue

            throw EpicAPIException(response.bodyAsText())
        }

        // Response is OK
        val accessToken = jsonResponse["access_token"].asString
        accountId = jsonResponse["account_id"].asString
        displayName = jsonResponse["displayName"].asString
        profilePicture = ProfilePictureLoader.getProfileIcon(accessToken, accountId)

        response = client.get("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/exchange") {
            headers {
                append("Authorization", "bearer $accessToken")
            }
        }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)
        exchangeToken = jsonResponse["code"].asString
        status = TaskStatus.SUCCESS
    }
}
