package com.udacity.asteroidradar.api

import android.os.Parcelable
import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.database.PictureEntity
import kotlinx.android.parcel.Parcelize

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

@Parcelize
data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
    ) : Parcelable

data class NetworkPicture(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
    )

fun ArrayList<Asteroid>.asDatabaseModel(): Array<AsteroidEntity>{
    return map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid>{
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun List<Asteroid>.asNetworkModel(): List<NetworkAsteroid>{
    return map {
        NetworkAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun List<NetworkAsteroid>.toDomainModel(): List<Asteroid>{
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun NetworkPicture.toDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}

fun PictureOfDay.toDomainModel(): NetworkPicture {
    return NetworkPicture(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}

fun PictureOfDay.asDatabaseModel(): PictureEntity{
    return PictureEntity(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}

fun PictureEntity.asDomainModel():PictureOfDay{
    return PictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}







