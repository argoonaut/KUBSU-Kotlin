package com.example.app2.data
import android.provider.ContactsContract.CommonDataKinds.Phone
import java.util.*

data class Student(
    val id : UUID = UUID.randomUUID(),
    var firstName : String="",
    var middleName : String="",
    var lastName : String="",
    var phone : String="",
    var birthDate : Date = Date()
)
