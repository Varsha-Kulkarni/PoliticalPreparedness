package com.example.android.politicalpreparedness.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.politicalpreparedness.data.repository.CivicsRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
class RefreshDataWorker(
        appContext: Context,
        parameters: WorkerParameters): CoroutineWorker(appContext, parameters), KoinComponent {

    val civicsRepository: CivicsRepository by inject()

    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            civicsRepository.refreshElectionsData()
            Result.success()

        }catch (exception: Exception){
            Timber.e("$exception")
            Result.retry()
        }
    }
}