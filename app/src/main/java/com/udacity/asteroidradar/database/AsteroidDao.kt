package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM ASTEROID_TABLE ORDER BY date(closeApproachDate) ASC")
    fun getDatabaseAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM ASTEROID_TABLE WHERE closeApproachDate= :date")
    fun getTodayAsteroids(date: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * from ASTEROID_TABLE WHERE closeApproachDate BETWEEN :startDate and :endDate ORDER BY date(closeApproachDate) ASC")
    fun getWeeklyAsteroids(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidEntity)

    @Query("DELETE FROM ASTEROID_TABLE WHERE closeApproachDate < :date")
    suspend fun deleteOldAsteroids(date: String)
}