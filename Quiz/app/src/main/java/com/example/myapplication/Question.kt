package com.example.myapplication

import androidx.annotation.StringRes

data class Question(
    @StringRes val TextResId: Int,
    val answer: Boolean,
    var isCheat: Boolean = false
)
