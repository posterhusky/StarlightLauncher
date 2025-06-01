package net.vanolex

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var fortnitePath: String,
    var closeAfterLaunch: Boolean,
    var langCode: String,
) {
    fun save() {
        val jsonString = ktJson.encodeToString(this)
        val file = loadFile("config.json")
        file.writeText(jsonString)
    }
}