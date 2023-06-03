package com.example.airlinesapp.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.data.Airline
import com.example.airlinesapp.data.City
import com.example.airlinesapp.repository.AppRepository
import java.util.*

class CityListViewModel : ViewModel() {
    // LiveData, содержащая текущую авиакомпанию
    val airline: MutableLiveData<Airline?> = MutableLiveData()

    // ID текущей авиакомпании
    private var airlineID: UUID? = null

    init {
        // Обзервер для отслеживания изменений в списке авиакомпаний в репозитории
        AppRepository.get().airlines.observeForever {
            // Отправка обновлений текущей авиакомпании
            airline.postValue(it.find { airline -> airline.id == airlineID })
        }
    }

    // Установка текущей авиакомпании по ID
    fun setAirlineId(airlineID: UUID) {
        this.airlineID = airlineID
        airline.postValue(AppRepository.get().airlines.value?.find { airline -> airline.id == airlineID })
    }

    // Добавление нового города
    fun newCity(airlineId: UUID, name: String) = AppRepository.get().newCity(airlineId, name)

    // Редактирование существующего города
    fun editCity(airlineId: UUID, cityId: UUID, name: String) =
        AppRepository.get().editCity(airlineId, cityId, name)

    // Удаление города
    fun deleteCity(airlineId: UUID, city: City) = AppRepository.get().deleteCity(airlineId, city)
}
