package net.vanolex.tasks

import net.vanolex.epicapi.Task

class BasicTask<T: Any>(val t: suspend () -> T): Task() {
    lateinit var result: T

    override suspend fun task() {
        result = t()
        status = TaskStatus.SUCCESS
    }
}