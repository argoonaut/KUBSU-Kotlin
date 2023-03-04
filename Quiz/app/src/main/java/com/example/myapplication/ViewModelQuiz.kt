package com.example.myapplication

import androidx.lifecycle.ViewModel

class ViewModelQuiz: ViewModel() {

    private val QuestionBank = listOf(
        Question(R.string.Question1, true),
        Question(R.string.Question2, true),
        Question(R.string.Question3, true),
        Question(R.string.Question4, true),
        Question(R.string.Question5, true),
        Question(R.string.Question6, false))
    var CurrentIndex = 0

    val CurrentQuestionAnswer: Boolean
        get() = QuestionBank[CurrentIndex].answer
    val CurrentQuestionText: Int
        get() = QuestionBank[CurrentIndex].TextResId

    var isCheater
        get() = QuestionBank[CurrentIndex].isCheat
        set(value) {QuestionBank[CurrentIndex].isCheat=value}

    fun MoveToNext(){
        CurrentIndex = (CurrentIndex+1) % QuestionBank.size }

    fun MoveToPrev(){
        CurrentIndex = (QuestionBank.size+CurrentIndex  - 1) % QuestionBank.size }

}