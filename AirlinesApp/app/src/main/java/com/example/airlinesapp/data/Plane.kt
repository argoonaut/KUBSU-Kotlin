package com.example.airlinesapp.data

import java.util.*
import kotlin.collections.ArrayList

// Класс "Plane" представляет самолет.
data class Plane(
    val id : UUID = UUID.randomUUID(), // Уникальный идентификатор самолета
    val date: String="", // Дата производства самолета
    val numberOfSeats: Int, // Количество мест в самолете
    val numberOfRows: Int, // Количество рядов в самолете
    var seats: ArrayList<Seat> // Список мест в самолете
)
