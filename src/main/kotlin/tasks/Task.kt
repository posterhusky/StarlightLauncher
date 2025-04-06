package net.vanolex.epicapi

import kotlinx.coroutines.*
import net.vanolex.jobList

abstract class Task {

    private lateinit var job: Job

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    var status: TaskStatus = TaskStatus.WAITING

    fun launchTaskAsync() {
        if (status == TaskStatus.IN_PROGRESS) return
        job = scope.launch {
            launchTaskSync()
        }
        jobList.add(job)
    }

    suspend fun launchTaskSync() {
        status = TaskStatus.IN_PROGRESS
        try {
            task()
        } catch (e: Exception) {
            e.printStackTrace()
            status = TaskStatus.FAILED
        }
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