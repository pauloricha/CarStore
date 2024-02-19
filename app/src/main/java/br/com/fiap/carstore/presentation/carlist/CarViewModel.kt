package br.com.fiap.carstore.presentation.carlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.com.fiap.carstore.dao.CarListRoomDatabase
import br.com.fiap.carstore.model.Car
import br.com.fiap.carstore.data.repositories.car.CarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val carRepository: CarRepository

    val cars: LiveData<List<Car>>

    init {
        val carDAO = CarListRoomDatabase.getDatabase(application).carDAO()
        carRepository = CarRepository(carDAO)
        cars = carRepository.cars
    }

    fun insert(car: Car) =
        viewModelScope.launch(Dispatchers.IO) {
            carRepository.insert(car)
        }

    fun update(car: Car) =
        viewModelScope.launch(Dispatchers.IO) {
            carRepository.update(car)
        }

    fun delete(car: Car) =
        viewModelScope.launch(Dispatchers.IO) {
            carRepository.delete(car)
        }
}