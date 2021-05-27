package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class PictureOfTheDayRepository(private val database: AsteroidsDatabase) {

    /**
     * Repository responsible to fetch the Picture of the Day from the network and store it
     * in the database
     */

    val pictureOfTheDay: LiveData<PictureOfDay> =
        Transformations.map(database.pictureDao.getPictureOfTheDay()){
            it?.asDomainModel()
        }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureResponse = NasaPictureAPI.retrofitPictureService.getPictureOfDay(
                    api_key = Constants.API_KEY
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