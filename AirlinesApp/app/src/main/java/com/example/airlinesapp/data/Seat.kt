package com.example.airlinesapp.data

// Класс "Seat" представляет место в самолете.
data class Seat(
    val name: String="", // Название места
    var isFree: Boolean=true // Свободно ли место
){
    override fun toString(): String { // Переопределение метода toString для представления места в виде строки
        return name
    }
}
