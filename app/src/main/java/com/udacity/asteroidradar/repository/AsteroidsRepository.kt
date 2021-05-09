package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidsDatabase){

    /**
     * Repository responsible to fetch asteroids from the network and store it
     * in the database
      */

    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){

        }
    }
}