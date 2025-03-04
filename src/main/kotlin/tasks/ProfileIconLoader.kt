package net.vanolex.epicapi

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import net.vanolex.client
import net.vanolex.gson
import net.vanolex.pcAuth
import java.awt.Image
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

object ProfileIconLoader {
    suspend fun getProfileIcon(accessToken: String, accountId: String): Image {
        var response =
            client.post("https://api.epicgames.dev/auth/v1/oauth/token") {
                headers {
                    append("Content-Type", "application/x-www-form-urlencoded")
                    append("Authorization", "Basic $pcAuth")
                }
                setBody("grant_type=external_auth" +
                        "&external_auth_type=epicgames_access_token" +
                        "&external_auth_token=$accessToken" +
                        "&deployment_id=62a9473a2dca46b29ccf17577fcf42d7" +
                        "&nonce=${generateNonce()}")
            }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        var jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)

        val eosToken = jsonResponse["access_token"].asString

        response =
            client.get("https://fngw-svc-gc-livefn.ol.epicgames.com/api/locker/v4/62a9473a2dca46b29ccf17577fcf42d7/account/$accountId/items") {
                headers {
                    append("Authorization", "Bearer $eosToken")
                }
            }
        if (response.status != HttpStatusCode.OK) throw EpicAPIException(response.bodyAsText())
        jsonResponse = gson.fromJson(response.bodyAsText(), JsonObject::class.java)

        val fetchUrl = try {
            "https://fortnite-api.com/images/cosmetics/br/${extractCurrentSkinName(jsonResponse)}/icon.png"
        } catch (e: Exception) {
            e.printStackTrace()
            "https://fortnite-api.com/images/cosmetics/br/cid_883_athena_commando_m_chonejonesy/icon.png"
        }

        response =
            client.get(fetchUrl) {
                headers {
                    append("Accept", "image/png")
                }
            }

        val img = ImageIO.read(ByteArrayInputStream(response.bodyAsBytes()))

        return img.getScaledInstance(140, 140, Image.SCALE_SMOOTH)

    }

    private fun extractCurrentSkinName(json: JsonObject): String {
        val charLoadout = json.getAsJsonObject("activeLoadoutGroup")
            .getAsJsonObject("loadouts")
            .getAsJsonObject("CosmeticLoadout:LoadoutSchema_Character")

        val loadoutSlots = if (charLoadout.has("linkedPresetId")) {
            findPreset(json.getAsJsonArray("loadoutPresets"), charLoadout["linkedPresetId"].asString) ?: throw EpicAPIException("Linked preset not found!\n$json")
        } else charLoadout.getAsJsonArray("loadoutSlots")

        for (it in loadoutSlots) {
            val i = it as? JsonObject ?: continue
            if (i == null) continue
            if (i["slotTemplate"].asString != "CosmeticLoadoutSlotTemplate:LoadoutSlot_Character") continue
            return i["equippedItemId"].asString.removePrefix("AthenaCharacter:")
        }

        throw EpicAPIException("Character loadout slot not found!\n$json")
    }

    private fun findPreset(loadoutPresets: JsonArray, presetId: String): JsonArray? {
        for (i in loadoutPresets.map { it as? JsonObject }) {
            if (i == null) continue
            if (i["presetId"].asString != presetId) continue
            return i.getAsJsonArray("loadoutSlots")
        }
        return null
    }



}