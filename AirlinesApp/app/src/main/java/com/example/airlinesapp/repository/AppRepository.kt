package com.example.airlinesapp.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.airlinesapp.data.Airline
import com.example.airlinesapp.data.City
import com.example.airlinesapp.data.Flight
import com.example.airlinesapp.data.Seat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

// Название файла SharedPreferences для хранения данных приложения
const val SHARED_PREFERENCES_NAME = "UniversityAppPrefs"

class AppRepository private constructor() {
    // LiveData, хранящая список авиакомпаний
    var airlines: MutableLiveData<List<Airline>> = MutableLiveData()

    companion object {
        private var INSTANCE: AppRepository? = null

        // Инициализация экземпляра репозитория
        fun newInstance() {
            if (INSTANCE == null) {
                INSTANCE = AppRepository()
            }
        }

        // Получение экземпляра репозитория
        fun get(): AppRepository {
            return INSTANCE ?: throw IllegalAccessException("Репозиторий не инициализирован")
        }
    }

    // Сохранение текущих данных в SharedPreferences
    fun saveData(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val jsonUniversity = Gson().toJson(airlines.value)
        sharedPreferences.edit().putString(SHARED_PREFERENCES_NAME, jsonUniversity).apply()
    }

    // Загрузка сохраненных данных из SharedPreferences
    fun loadData(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(SHARED_PREFERENCES_NAME, null)
        if (jsonString != null) {
            val listType = object : TypeToken<List<Airline>>() {}.type
            val faculties = Gson().fromJson<List<Airline>>(jsonString, listType)
            airlines.value = faculties
        } else {
            airlines.value = arrayListOf()
        }
    }

    // Добавление новой авиакомпании
    fun newAirline(name: String, year: Int) {
        val airline = Airline(name = name, year = year)
        val list: ArrayList<Airline> =
            if (airlines.value != null) {
                (airlines.value as ArrayList<Airline>)
            } else
                ArrayList()
        list.add(airline)
        airlines.postValue(list)//оповещение всех пользователей
    }

    // Удаление авиакомпании
    fun deleteAirline(airline: Airline) {
        val list: ArrayList<Airline> = airlines.value as ArrayList<Airline>
        list.remove(airline)
        airlines.postValue(list)
    }

    // Редактирование информации об авиакомпании
    fun editAirline(id: UUID, name: String, year: Int){
        val list: ArrayList<Airline> = airlines.value as ArrayList<Airline>
        val _airline = list.find { it.id == id }
        if (_airline == null) {
            newAirline(name, year)
            return
        }
        val airline = Airline(id = id, name = name, year = year)
        airline.cities = _airline.cities
        val i = list.indexOf(_airline)
        list.remove(_airline)
        list.add(i, airline)
        airlines.postValue(list)
    }

    // Добавление нового города в список городов авиакомпании
    fun newCity(airlineId: UUID, name: String){
        val a = airlines.value ?: return
        val airline = a.find { it.id == airlineId } ?: return
        val city = City(name = name)
        val list: ArrayList<City> = if (airline.cities.isEmpty())
            ArrayList()
        else
            airline.cities as ArrayList<City>
        list.add(city)
        airline.cities = list
        airlines.postValue(a)
    }

    // Редактирование информации о городе
    fun editCity(airlineId: UUID, cityId: UUID, name: String) {
        val a = airlines.value ?: return
        val airline = a.find { it.id == airlineId } ?: return
        val list: ArrayList<City> = if (airline.cities.isEmpty())
            ArrayList()
        else
            airline.cities as ArrayList<City>
        val _city = list.find { it.id == cityId }
        if(_city == null){
            newCity(airlineId, name)
            return
        }
        val city = City(id = cityId, name = name)
        city.flights = _city.flights
        val i = list.indexOf(_city)
        list.remove(_city)
        list.add(i, city)
        airline.cities = list
        airlines.postValue(a)
    }

    // Удаление города из списка городов авиакомпании
    fun deleteCity(airlineId: UUID, city: City) {
        val a = airlines.value ?: return
        val airline = a.find { it.id == airlineId } ?: return
        val list: ArrayList<City> = if (airline.cities.isEmpty())
            ArrayList()
        else
            airline.cities as ArrayList<City>
        list.remove(city)
        airline.cities = list
        airlines.postValue(a)
    }

    // Добавление нового рейса в город
    fun newFlight(airlineID: UUID?, cityID: UUID?, flight: Flight){
        val a = airlines.value ?: return
        val airline = a.find { it.id == airlineID } ?: return
        val city = airline.cities.find { it.id == cityID } ?: return
        val list: ArrayList<Flight> = if (city.flights.isEmpty())
            ArrayList()
        else
            city.flights as ArrayList<Flight>
        list.add(flight)
        city.flights = list
        airlines.postValue(a)
    }

    // Удаление рейса из списка рейсов города
    fun deleteFlight(airlineID: UUID?, cityID: UUID?, flight: Flight) {
        val a = airlines.value ?: return
        val airline = a.find { it.id == airlineID } ?: return
        val city = airline.cities.find { it.id == cityID } ?: return
        val list: ArrayList<Flight> = if (city.flights.isEmpty())
            ArrayList()
        else
            city.flights as ArrayList<Flight>
        list.remove(flight)
        city.flights = list
        airlines.postValue(a)
    }

    // Редактирование информации о рейсе
    fun editFlight(airlineID: UUID?, cityID: UUID?, flightID: UUID, newFlight: Flight) {
        val a = airlines.value ?: return
        val airline = a.find { it.id == airlineID } ?: return
        val city = airline.cities.find { it.id == cityID } ?: return
        val list: ArrayList<Flight> = if (city.flights.isEmpty())
            ArrayList()
        else
            city.flights as ArrayList<Flight>
        val _flight = list.find { it.id == flightID }
        if (_flight == null) {
            newFlight(airlineID, cityID, newFlight)
            return
        }
        val i = list.indexOf(_flight)
        list.remove(_flight)
        list.add(i, newFlight)
        city.flights = list
        airlines.postValue(a)
    }

    // Оплата билета на рейс
    fun pay_ticket(
        airlineID: UUID?,
        cityID: UUID?,
        flightID: UUID,
        planeDate: String,
        seatName: String
    ) {
        val a = airlines.value ?: return
        val airline = a.find { it.id == airlineID } ?: return
        val city = airline.cities.find { it.id == cityID } ?: return
        val flight = city.flights.find { it.id == flightID } ?: return
        val plane = flight.planes.find { it.date == planeDate } ?: return
        val list: ArrayList<Seat> = if (plane.seats.isEmpty())
            ArrayList()
        else
            plane.seats
        val _seat = list.find { it.name == seatName }
        val newSeat = Seat(name = seatName, isFree = false)
        val i = list.indexOf(_seat)
        list.remove(_seat)
        list.add(i, newSeat)
        plane.seats = list
        airlines.postValue(a)
    }
}
