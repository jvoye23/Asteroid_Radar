package com.udacity.asteroidradar.repository

import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

private const val API_KEY = ""

class PictureOfTheDayRepository(private val database: AsteroidsDatabase) {

    val pictureOfTheDay: LiveData<PictureOfDay> =
        Transformations.map(database.pictureDao.getPictureOfTheDay()){
            it?.asDomainModel()
        }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureResponse = NasaPictureAPI.retrofitPictureService.getPictureOfDay(
                    api_key = API_KEY
                )
                val domainPicture = pictureResponse.toDomainModel()

                if (domainPicture.mediaType == "image") {
                    database.pictureDao.clear()
                    database.pictureDao.insertAll(domainPicture.asDatabaseModel())
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                }
            }
        }
    }
 }