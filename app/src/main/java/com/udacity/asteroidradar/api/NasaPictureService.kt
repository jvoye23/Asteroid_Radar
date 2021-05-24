package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.udacity.asteroidradar.Constants

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface NasaPictureService {
    @GET("planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") api_key: String
    ): NetworkPicture
}

// A public Api object that exposes the lazy-initialized Retrofit service
object NasaPictureAPI {
    val retrofitPictureService : NasaPictureService by lazy {
        retrofit.create(NasaPictureService::class.java)
    }
}