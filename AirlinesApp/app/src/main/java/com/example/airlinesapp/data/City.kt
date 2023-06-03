package com.example.airlinesapp.data

import java.util.*

// Класс "City" представляет город.
data class City(
    val id : UUID = UUID.randomUUID(), // Уникальный идентификатор города
    var name : String="" // Название города
) {
    var flights: List<Flight> = emptyList() // Список рейсов, связанных с этим городом
}