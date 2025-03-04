package net.vanolex

import java.io.File

data class Config(
    var fortnitePath: String,
    var closeAfterLaunch: Boolean,

) {
    fun save() {
        val jsonString = gson.toJson(this)
        val file = File("./config.json")
        file.writeText(jsonString)
    }
}