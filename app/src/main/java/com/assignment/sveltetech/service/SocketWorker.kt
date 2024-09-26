package com.assignment.sveltetech.service

import android.content.Context
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.assignment.sveltetech.service.WorkerUtils.connectToServer

class SocketWorker(val appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        if(WorkerUtils.socketManager.getSocket()?.connected() != true){
            appContext.connectToServer()
        }
        return Result.success()
    }
}