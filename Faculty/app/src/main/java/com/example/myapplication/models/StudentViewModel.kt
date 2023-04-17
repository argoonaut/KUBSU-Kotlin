package com.example.myapplication.models

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Student
import com.example.myapplication.repository.AppRepository
import java.util.UUID

class StudentViewModel : ViewModel() {
    fun newStudent(groupID: UUID, student: Student) =
        AppRepository.get().newStudent(groupID, student)

}