package com.example.airlinesapp.data

import java.util.*

// Класс "Airline" представляет авиакомпанию.
data class Airline(
    val id : UUID = UUID.randomUUID(), // Уникальный идентификатор авиакомпании
    var name : String="", // Название авиакомпании
    var year : Int // Год основания авиакомпании
) {
    var cities: List<City> = emptyList() // Список городов, с которыми связана данная авиакомпания
}
