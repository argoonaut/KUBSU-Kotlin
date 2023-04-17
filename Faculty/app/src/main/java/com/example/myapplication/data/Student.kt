package com.example.myapplication.data

import android.provider.ContactsContract
import java.util.*

data class Student(
    val id : UUID = UUID.randomUUID(),
    var firstName : String="",
    var lastName : String="",
    var middleName : String="",
    var phone : String = "",
    var birthDate : Date
)