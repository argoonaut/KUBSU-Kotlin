package com.example.airlinesapp.data

import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

// Класс "Flight" представляет рейс.
data class Flight(
    val id : UUID = UUID.randomUUID(), // Уникальный идентификатор рейса
    var departureCity: String="", // Город вылета
    var arrivalCity: String="", // Город прибытия
    var nameOfPlane: String="", // Название самолета
    var hour: Int, // Час вылета
    var minute: Int, // Минута вылета
    var dayOfWeek: String="", // День недели вылета
    var flightTime: Int, // Время полета (в минутах)
    var planes: ArrayList<Plane> // Список самолетов, связанных с этим рейсом
)