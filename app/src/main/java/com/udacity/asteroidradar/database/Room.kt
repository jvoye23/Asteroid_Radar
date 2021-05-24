package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*

@Database(entities = [AsteroidEntity::class, PictureEntity::class], version = 2)
abstract class AsteroidsDatabase: RoomDatabase(){
    abstract val asteroidDao: AsteroidDao
    abstract val pictureDao: PictureDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase (context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java){
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidsDatabase::class.java,
                    "asteroids")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}

