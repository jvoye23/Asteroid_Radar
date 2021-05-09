package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*

import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "7O8wgkuigf85v7bm5VRnlmTHbMBTRgG7XlhA2nFt"

class MainViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent Json response
    private val _asteroidJsonResponse = MutableLiveData<ArrayList<Asteroid>>()

    // The external immutable LiveData for the response String
    val asteroidJsonResponse: LiveData<ArrayList<Asteroid>>
        get() = _asteroidJsonResponse

    // The internal MutableLiveData String that stores the picture of the day
    private val _pictureResponse = MutableLiveData<PictureOfDay>()

    // The external immutable LiveData for the picture response string
    val pictureResponse: LiveData<PictureOfDay>
        get() = _pictureResponse


    // For navigating from the Asteroid List to the Asteroid Detail fragment
    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetail.value = null
    }

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getNasaPicture()
        getAsteroidsData(NasaApiFilter.START_DATE)

    }

    /**
     * Sets the value of the response LiveData to the Asteroid API status or the successful number of
     * Asteroids retrieved.
     */

      private fun getAsteroidsData(filter: NasaApiFilter) {
        viewModelScope.launch {
            try {
                val response = NasaApi.retrofitService.getAsteroids(
                    //start_date = "2021-04-25",
                    //end_date = "2021-05-01",
                    filter.value,
                    api_key =  API_KEY)

                val asteroidList = parseAsteroidsJsonResult(JSONObject(response))

                _asteroidJsonResponse.value = asteroidList
                Log.d("Success", "Json downloaded")

            } catch (e: Exception){
                Log.d("Failure","${e.message}" )
            }
        }
    }

    fun updateFilter(filter: NasaApiFilter){
        getAsteroidsData(filter)
    }

    private fun getNasaPicture(){
        viewModelScope.launch {
            try {
                val response = NasaPictureAPI.retrofitPictureService.getPictureOfDay(
                    api_key = API_KEY)
                _pictureResponse.value = response

            } catch (e: Exception){
                Log.d("Failure of Picture API","${e.message}" )
            }
        }
    }
}