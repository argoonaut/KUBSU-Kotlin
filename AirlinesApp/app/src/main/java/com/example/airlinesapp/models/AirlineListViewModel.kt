package com.example.airlinesapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.airlinesapp.data.Airline
import com.example.airlinesapp.repository.AppRepository
import java.util.*

class AirlineListViewModel : ViewModel() {
    // LiveData, которая содержит список всех авиакомпаний
    var airlines: MutableLiveData<List<Airline>> = MutableLiveData()

    init {
        // Инициализация viewModel, подписываемся на airlines из репозитория
        // чтобы обновлять список авиакомпаний в этой ViewModel каждый раз, когда он меняется в репозитории
        AppRepository.get().airlines.observeForever {
            airlines.postValue(it)
        }
    }

    // Метод для удаления авиакомпании. Взаимодействует с репозиторием, чтобы выполнить удаление.
    fun deleteAirline(airline: Airline) = AppRepository.get().deleteAirline(airline)

    // Метод для редактирования информации об авиакомпании. Взаимодействует с репозиторием, чтобы выполнить обновление.
    fun editAirline(id: UUID, name: String, year: Int) = AppRepository.get().editAirline(id, name, year)

    // Метод для создания новой авиакомпании. Взаимодействует с репозиторием, чтобы выполнить добавление.
    fun newAirline(name: String, year: Int) = AppRepository.get().newAirline(name, year)
}