package com.example.myapplication.data


import java.util.*

data class Group(
    val id : UUID = UUID.randomUUID(),
    var name : String="") {
    constructor() : this(UUID.randomUUID())

    var students : List<Student> = emptyList()
}