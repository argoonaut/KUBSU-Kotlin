package com.example.app2.ui

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.app2.R
import com.example.app2.data.Student
import com.example.app2.databinding.FragmentStudentBinding
import java.util.*

const val STUDENT_TAG = "StudentFragment"

class StudentFragment private constructor() : Fragment() {
    private var _binding: FragmentStudentBinding? = null
    private val binding get() = _binding!!
    companion object {
        private var student: Student? = null
        private lateinit var groupID: UUID
        fun newInstance(groupID: UUID, student: Student? = null): StudentFragment {
            this.student = student
            this.groupID = groupID
            return StudentFragment()
        }
    }

    private lateinit var viewModel: StudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

//    private var selectedDate: Date = Date()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (student != null) {
            binding.etName.setText(student?.firstName)
            binding.etMiddleName.setText(student?.middleName)
            binding.etLastName.setText(student?.lastName)
            binding.etPhone.setText(student?.phone.toString())
            val dt = GregorianCalendar().apply {
                time = student!!.birthDate
            }
            binding.dtpCalendar.init(dt.get(Calendar.YEAR), dt.get(Calendar.MONTH),
                dt.get(Calendar.DAY_OF_MONTH), null)
        }
//        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMounth ->
//            selectedDate = GregorianCalendar().apply {
//                set(GregorianCalendar.YEAR, year)
//                set(GregorianCalendar.MONTH, month)
//                set(GregorianCalendar.DAY_OF_MONTH, dayOfMounth)
//            }.time
//        }

        viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
    }
    val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showCommitDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }


    private fun showCommitDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(true)
        builder.setMessage("Сохранить изменения?")
        builder.setTitle("Подтвержение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            var p = true
            binding.etName.text.toString().ifBlank {
                p = false
                binding.etName.error = "Укажите значение"
            }
            binding.etLastName.text.toString().ifBlank {
                p = false
                binding.etLastName.error = "Укажите значение"
            }
            binding.etMiddleName.text.toString().ifBlank {
                p = false
                binding.etMiddleName.error = "Укажите значение"
            }
            binding.etPhone.text.toString().ifBlank {
                p = false
                binding.etPhone.error = "Укажите значение"
            }



            var cd = GregorianCalendar().get(GregorianCalendar.YEAR)
            if (cd - binding.dtpCalendar.year < 10) {
                p = false
                Toast.makeText(context, "Укажите возраст правильно", Toast.LENGTH_SHORT).show()
            }
            if(p) {
                val selectedDate = GregorianCalendar().apply {
                    set(GregorianCalendar.YEAR, binding.dtpCalendar.year)
                    set(GregorianCalendar.MONTH, binding.dtpCalendar.month)
                    set(GregorianCalendar.DAY_OF_MONTH, binding.dtpCalendar.dayOfMonth)
                }

                if (student == null) {
                    student = Student()
                    student?.apply {
                        firstName = binding.etName.text.toString()
                        middleName = binding.etLastName.text.toString()
                        lastName = binding.tvMiddleName.text.toString()
                        phone = binding.etPhone.text.toString()
                        birthDate = selectedDate.time
                    }
                    viewModel.newStudent(groupID!!, student!!)
                } else {
                    student?.apply {
                        firstName = binding.etName.text.toString()
                        middleName = binding.etLastName.text.toString()
                        lastName = binding.tvMiddleName.text.toString()
                        phone = binding.etPhone.text.toString()
                        birthDate = selectedDate.time
                    }
                    viewModel.editStudent(groupID!!, student!!)
                }
                backPressedCallback.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
            builder.setNegativeButton("Не сохранять") { _, _, ->
                backPressedCallback.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            val alert = builder.create()
            alert.show()

    }




}