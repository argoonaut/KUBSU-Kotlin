package com.example.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.Faculty
import com.example.myapplication.data.Group
import com.example.myapplication.data.Student
import java.util.UUID

class AppRepository private constructor() {
    var university: MutableLiveData<List<Faculty>> = MutableLiveData()

    companion object{
        private var INSTANCE: AppRepository? = null

        fun newInstance(){
            if (INSTANCE == null){
                INSTANCE = AppRepository()
            }
        }
        fun get(): AppRepository{
            return INSTANCE?:
            throw IllegalAccessException("Репозиторий не инициализирован")
            }
        }

    fun newFaculty(name: String){
        val faculty = Faculty(name=name)
        val list: MutableList<Faculty> =
            if (university.value != null)
            {
                (university.value as ArrayList<Faculty>)
            }
        else
            ArrayList<Faculty>()
        list.add(faculty)
        university.postValue(list)
    }
    fun newGroup(facultyID: UUID, name: String){
        val u = university.value?: return
        val faculty = u.find{it. id== facultyID} ?: return
        val group = Group(name=name)
        val list: ArrayList<Group> =
            if (faculty.groups.isEmpty())
            ArrayList()
            else
                faculty.groups as ArrayList<Group>
        list.add(group)
        faculty.groups=list
        university.postValue(u)
    }

    fun newStudent(groupID: UUID, student: Student) {
        val u = university.value ?: return
        val faculty = u.find {
            it.groups.find { it.id == groupID } != null
        } ?: return

        val group = faculty.groups.find {it.id == groupID}
        val list: ArrayList<Student> = if (group!!.students.isEmpty())
            ArrayList()
        else
            group.students as ArrayList<Student>
        list.add(student)
        group.students = list
        university.postValue(u)
    }

    fun deleteStudent(groupID: UUID, student: Student) {
        val u = university.value ?: return
        val faculty = u.find {
            it.groups.find { it.id == groupID } != null
        } ?: return

        val group = faculty.groups.find {it.id == groupID}
        if (group!!.students.isEmpty()) return
        val list = group.students as ArrayList<Student>
        list.remove(student)
        group.students = list
        university.postValue(u)
    }
}