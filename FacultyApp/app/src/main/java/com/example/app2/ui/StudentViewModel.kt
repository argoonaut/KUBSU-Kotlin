package com.example.app2.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app2.data.Faculty
import com.example.app2.data.Student
import com.example.app2.repository.FacultyRepository
import java.util.*

class StudentViewModel : ViewModel() {

    fun newStudent(groupID: UUID, student: Student) =
        FacultyRepository.get().newStudent(groupID, student)
    fun editStudent(groupID: UUID, student: Student) =
        FacultyRepository.get().editStudent(groupID, student)


}