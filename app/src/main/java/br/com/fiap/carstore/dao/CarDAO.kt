package br.com.fiap.carstore.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.fiap.carstore.model.Car

@Dao
interface CarDAO {

    @Query("SELECT * from car ORDER BY car_id ASC")
    fun getCars(): LiveData<List<Car>>

    @Query("SELECT * FROM car WHERE car_id = :carId")
    fun getCarById(carId: String): Car

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(car: Car)

    @Update
    suspend fun update(car: Car)

    @Query("DELETE FROM car")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(car: Car)

    @Query("DELETE FROM car WHERE car_id = :carId")
    suspend fun deleteByCarId(carId: String)

}