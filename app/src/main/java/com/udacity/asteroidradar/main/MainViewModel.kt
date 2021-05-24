package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository

import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "7O8wgkuigf85v7bm5VRnlmTHbMBTRgG7XlhA2nFt"

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    /* Without database:

    **  // The internal MutableLiveData String that stores the most recent Json response
    private val _asteroidJsonResponse = MutableLiveData<ArrayList<Asteroid>>()

    // The external immutable LiveData for the response String
    val asteroidJsonResponse: LiveData<ArrayList<Asteroid>>
        get() = _asteroidJsonResponse

    // The internal MutableLiveData String that stores the picture of the day
    private val _pictureResponse = MutableLiveData<PictureOfDay>()

    // The external immutable LiveData for the picture response string
    val pictureResponse: LiveData<PictureOfDay>
        get() = _pictureResponse
     */

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidsRepository(database)
    private val pictureRepository = PictureOfTheDayRepository(database)


    // For navigating from the Asteroid List to the Asteroid Detail fragment
    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail


    init {
        viewModelScope.launch {

            //without database
            //getNasaPicture()
            asteroidRepository.refreshAsteroids()
            pictureRepository.refreshPictureOfTheDay()
        }
        //getAsteroidsData(NasaApiFilter.START_DATE)

    }

    val asteroids = asteroidRepository.asteroids
    val pictureOfTheDay = pictureRepository.pictureOfTheDay

    fun updateFilter(filter: AsteroidsRepository.AsteroidsFilter){
       asteroidRepository.applyFilter(filter)
   }

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetail.value = null
    }

    /**
     * Sets the value of the response LiveData to the Asteroid API status or the successful number of
     * Asteroids retrieved.
     */

     /* private fun getAsteroidsData(filter: NasaApiFilter) {
        viewModelScope.launch {
            try {
                val response = NasaApi.retrofitService.getAsteroids(
                    //start_date = "2021-04-25",
                    //end_date = "2021-05-01",
                    //filter.value,
                    api_key =  API_KEY)

                val asteroidList = parseAsteroidsJsonResult(JSONObject(response))

                _asteroidJsonResponse.value = asteroidList
                Log.d("Success", "Json downloaded")

            } catch (e: Exception){
                Log.d("Failure","${e.message}" )
            }
        }
    }*/



   /* private fun getNasaPicture(){
        viewModelScope.launch {
            try {
                val response = NasaPictureAPI.retrofitPictureService.getPictureOfDay(
                    api_key = API_KEY)
                _pictureResponse.value = response

            } catch (e: Exception){
                Log.d("Failure of Picture API","${e.message}" )
            }
        }
    }*/
}