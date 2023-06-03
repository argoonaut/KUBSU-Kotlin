package com.example.airlinesapp.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.data.City
import com.example.airlinesapp.data.Flight
import com.example.airlinesapp.repository.AppRepository
import java.util.*

class FlightListViewModel : ViewModel() {
    // LiveData, содержащая текущий город
    val city: MutableLiveData<City?> = MutableLiveData()

    // ID текущей авиакомпании и города
    private var airlineID: UUID? = null
    private var cityID: UUID? = null

    init {
        // Обзервер для отслеживания изменений в списке авиакомпаний в репозитории
        AppRepository.get().airlines.observeForever {
            // Поиск и обновление текущего города
            city.postValue(it.find { airline -> airline.id == airlineID }
                ?.cities?.find { city -> city.id == cityID })
        }
    }

    // Установка текущей авиакомпании и города по их ID
    fun setAirlineAndCityID(airlineID: UUID, cityID: UUID) {
        this.airlineID = airlineID
        this.cityID = cityID
        city.postValue(AppRepository.get().airlines.value?.find { airline -> airline.id == airlineID }
            ?.cities?.find { city -> city.id == cityID })
    }

    // Получение имени текущего города
    fun getNameOfCity(): String = city.value?.name ?: ""

    // Добавление нового рейса
    fun newFlight(flight: Flight) = AppRepository.get().newFlight(airlineID, cityID, flight)

    // Удаление рейса
    fun deleteFlight(flight: Flight) = AppRepository.get().deleteFlight(airlineID, cityID, flight)

    // Редактирование рейса
    fun editFlight(flightID: UUID, newFlight: Flight) =
        AppRepository.get().editFlight(airlineID, cityID, flightID, newFlight)

    // Оплата билета
    fun pay_ticket(flightID: UUID, planeDate: String, seatName: String) =
        AppRepository.get().pay_ticket(airlineID, cityID, flightID, planeDate, seatName)
}
