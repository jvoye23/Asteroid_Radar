package com.udacity.asteroidradar.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "asteroid_table")
@Parcelize
data class AsteroidEntity (
        @PrimaryKey
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean
) : Parcelable

@Entity(tableName = "picture_table")
@Parcelize
data class PictureEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,
        val mediaType: String,
        val title: String,
        val url: String
) : Parcelable



