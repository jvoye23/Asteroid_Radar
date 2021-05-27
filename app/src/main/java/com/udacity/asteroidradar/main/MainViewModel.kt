package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository
import kotlinx.coroutines.launch

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

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidsRepository(database)
    private val pictureRepository = PictureOfTheDayRepository(database)

    // For navigating from the Asteroid List to the Asteroid Detail fragment
    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            pictureRepository.refreshPictureOfTheDay()
        }
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
}