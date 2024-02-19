package br.com.fiap.carstore.data.repositories.car

import androidx.lifecycle.LiveData
import br.com.fiap.carstore.dao.CarDAO
import br.com.fiap.carstore.model.Car

class CarRepository(
    private val carDAO: CarDAO
) {

    val cars: LiveData<List<Car>> = carDAO.getCars()

    suspend fun insert(car: Car) {
        carDAO.insert(car)
    }

    suspend fun update(car: Car) {
        carDAO.update(car)
    }

    suspend fun delete(car: Car) {
        carDAO.delete(car)
    }
}