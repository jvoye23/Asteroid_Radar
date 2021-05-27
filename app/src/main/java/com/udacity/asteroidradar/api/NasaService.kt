package com.udacity.asteroidradar.api


import com.udacity.asteroidradar.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("feed")
    suspend fun getAsteroids(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("api_key") api_key: String
    ): String
}

// A public Api object that exposes the lazy-initialized Retrofit service
object NasaApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(OkHttpClient().newBuilder().build())
        .build()

    val retrofitService : NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}







