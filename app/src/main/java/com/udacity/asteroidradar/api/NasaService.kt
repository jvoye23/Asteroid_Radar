package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * API Key for joerg.voye@gmail.com
 * 7O8wgkuigf85v7bm5VRnlmTHbMBTRgG7XlhA2nFt
 *
 * Example of web request
 * https://api.nasa.gov/planetary/apod?api_key=7O8wgkuigf85v7bm5VRnlmTHbMBTRgG7XlhA2nFt
 *
 * Account Details
 * Account Email: joerg.voye@gmail.com
 * Account ID: 72c7ed16-2f7f-403d-9e00-c4b243dfc8a3
 */

private const val BASE_URL = "https://api.nasa.gov/neo/rest/v1/"

// parameters start_date and end_date are of type YYYY-MM-DD
// by default end_date = 7 days after start_date
enum class NasaApiFilter (val value: String){
    START_DATE("2021-05-01"),
    END_DATE("end_date")
}



interface NasaApiService {
    @GET("feed")
    suspend fun getAsteroids(
       // @Query("start_date") start_date: String,
        // @Query("end_date") end_date: String,
        @Query("filter") type : String,
        @Query("api_key") api_key: String
    ): String
}

/**
 * Main entry point for network access. Call like `Network.nasaService.getAsteroids()`
 */


// A public Api object that exposes the lazy-initialized Retrofit service
object NasaApi {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(OkHttpClient().newBuilder().build())
        .build()

    val retrofitService : NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}







