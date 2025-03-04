package net.vanolex.epicapi

import kotlinx.coroutines.*
import net.vanolex.jobList

abstract class AsyncTask {

    private lateinit var job: Job

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    var status: TaskStatus = TaskStatus.WAITING

    fun launchTask() {
        job = scope.launch {
            status = TaskStatus.IN_PROGRESS
            try {
                task()
            } catch (e: Exception) {
                status = TaskStatus.FAILED
            }
        }

        jobList.add(job)

    }

    abstract suspend fun task()

    fun cancelTask() {
        job.cancel()
        status = TaskStatus.CANCELLED
    }

    enum class TaskStatus {
        WAITING, IN_PROGRESS, SUCCESS, FAILED, CANCELLED
    }
}