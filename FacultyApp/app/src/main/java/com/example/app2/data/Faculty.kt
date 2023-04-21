package com.example.app2.data

import java.util.*

data class Faculty(
    val id : UUID = UUID.randomUUID(),
    var name : String=""){
    constructor() : this(UUID.randomUUID())
    var groups : MutableList<Group>? = null
}
