package com.example.myapplication.ui

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.myapplication.R
import com.example.myapplication.data.Student
import com.example.myapplication.databinding.FragmentGroupBinding
import com.example.myapplication.databinding.FragmentStudentBinding
import com.example.myapplication.models.StudentViewModel
import java.util.GregorianCalendar
import java.util.UUID
import javax.security.auth.callback.Callback

const val STUDENT_TAG = "StudentFragment"

class StudentFragment : Fragment() {

    private var _binding: FragmentStudentBinding? = null
    val binding
        get() = _binding!!

    companion object {
        lateinit var groupID: UUID
        var student: Student? = null
        fun newInstance(groupID: UUID, student: Student?): StudentFragment {
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

    //private var selectedDate = GregorianCalendar()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        /*binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate.apply {
                set(GregorianCalendar.YEAR, year)
                set(GregorianCalendar.MONTH, month)
                set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth)
            }

        }*/
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
        builder.setMessage("Сохранить изменения ?")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            var p = true
            binding.elFirstName.text.toString().ifBlank {
                p = false
                binding.elFirstName.error = "Укажите значение"
            }
            binding.elLastName.text.toString().ifBlank {
                p = false
                binding.elLastName.error = "Укажите значение"
            }
            binding.elMiddleName.text.toString().ifBlank {
                p = false
                binding.elMiddleName.error = "Укажите значение"
            }
            binding.elPhone.text.toString().ifBlank {
                p = false
                binding.elPhone.error = "Укажите значение"
            }

            if(GregorianCalendar().get(GregorianCalendar.YEAR) - binding.dpCalendar.year < 10) {
                p = false
                Toast.makeText(context, "Укажите правильно возраст", Toast.LENGTH_LONG).show()
            }

            if(p) {
                val selectedDate = GregorianCalendar().apply {
                    set(GregorianCalendar.YEAR, binding.dpCalendar.year)
                    set(GregorianCalendar.MONTH, binding.dpCalendar.month)
                    set(GregorianCalendar.DAY_OF_MONTH, binding.dpCalendar.dayOfMonth)

                }

                val student = Student(
                    firstName = binding.elFirstName.text.toString(),
                    lastName = binding.elLastName.text.toString(),
                    middleName = binding.elMiddleName.text.toString(),
                    phone = binding.elPhone.text.toString(),
                    birthDate = selectedDate.time
                )
                viewModel.newStudent(groupID!!, student)

                backPressedCallback.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        builder.setNegativeButton(R.string.cancel) { _, _ ->
            backPressedCallback.isEnabled = false
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val alert = builder.create()
        alert.show()
    }
}