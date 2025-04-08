package net.vanolex

data class Config(
    var fortnitePath: String,
    var closeAfterLaunch: Boolean,

) {
    fun save() {
        val jsonString = gson.toJson(this)
        val file = loadFile("config.json")
        file.writeText(jsonString)
    }
}