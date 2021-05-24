package com.udacity.asteroidradar.work

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository
import retrofit2.HttpException

class RefreshAsteroidsWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params){


    companion object{
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val asteroidsRepository = AsteroidsRepository(database)
        val pictureOfTheDayRepository = PictureOfTheDayRepository(database)

        return try {
            asteroidsRepository.refreshAsteroids()
            pictureOfTheDayRepository.refreshPictureOfTheDay()

            asteroidsRepository.removeOldAsteroids()

            Result.success()
        }catch (exception: HttpException){
            Result.retry()
        }
    }

}
