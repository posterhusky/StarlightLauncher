package net.vanolex.tasks

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.vanolex.Account
import net.vanolex.accounts
import net.vanolex.epicapi.Task
import net.vanolex.gson
import java.io.File

class LoadAccounts: Task() {

    override suspend fun task() {
        val accs = try {
            gson.fromJson(File("./accounts.json").readText(), JsonArray::class.java)
        } catch (e: Exception) {
            mutableListOf()
        }
        accounts = accs.map {
            try {
                if (it !is JsonObject) return@map null
                return@map Account(it["deviceId"].asString, it["accountId"].asString, it["secret"].asString).init()
            } catch (e: Exception) {
                return@map null
            }
        }.filterNotNull().toMutableList()
        Account.saveAccounts()
        status = TaskStatus.SUCCESS
    }
}