package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.time.LocalDate


private const val API_KEY = "7O8wgkuigf85v7bm5VRnlmTHbMBTRgG7XlhA2nFt"

class AsteroidsRepository(private val database: AsteroidsDatabase){

    /**
     * Repository responsible to fetch asteroids from the network and store it
     * in the database
      */

    enum class AsteroidsFilter {WEEKLY, TODAY, SAVED}

    private val _asteroidsFilter = MutableLiveData(AsteroidsFilter.WEEKLY)
    val asteroidsFilter :  LiveData<AsteroidsFilter>
        get() = _asteroidsFilter

    @RequiresApi(Build.VERSION_CODES.O)
    private val _startDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _endDate = _startDate.plusDays(7)

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(asteroidsFilter){
        when(it){

            AsteroidsFilter.WEEKLY ->
                Transformations.map(database.asteroidDao.getWeeklyAsteroids(_startDate.toString(), _endDate.toString())){
                    it.asDomainModel()
                }
            AsteroidsFilter.TODAY ->
                Transformations.map(database.asteroidDao.getTodayAsteroids(_startDate.toString())){
                    it.asDomainModel()
                }
            AsteroidsFilter.SAVED ->
                Transformations.map(database.asteroidDao.getDatabaseAsteroids()){
                    it.asDomainModel()
                }
            else ->
                throw IllegalArgumentException("")
        }
    }

    fun applyFilter(asteroidsFilter: AsteroidsFilter){
        _asteroidsFilter.value = asteroidsFilter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {

                //val startDate = getTodayDateFormatted()
                val startDate = getTodayDateFormatted()
                val endDate = getOneWeekAheadDateFormatted()

                val networkAsteroidsResponse = NasaApi.retrofitService.getAsteroids(
                    start_date = startDate,
                    end_date = endDate,
                    api_key = API_KEY
                )

                val asteroidList = parseAsteroidsJsonResult(JSONObject(networkAsteroidsResponse))

                database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun removeOldAsteroids(){
        withContext(Dispatchers.IO){
            database.asteroidDao.deleteOldAsteroids(getTodayDateFormatted())
        }
    }
}