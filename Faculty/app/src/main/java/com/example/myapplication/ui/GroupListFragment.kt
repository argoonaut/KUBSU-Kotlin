package com.example.myapplication.ui

import android.content.Context
import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Faculty
import com.example.myapplication.data.Student
import com.example.myapplication.databinding.FragmentGroupListBinding
import com.example.myapplication.databinding.LayoutStudentListelementBinding
import com.example.myapplication.models.FacultyViewModel
import com.example.myapplication.repository.AppRepository


class GroupListFragment (private val group: List<Student>?): Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentGroupListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding =FragmentGroupListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
        binding.rvGroupList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        binding.rvGroupList.adapter=GroupListAdapter(group?: emptyList())
    }

    private inner class GroupHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener{
        lateinit var student: Student

        fun bind(student: Student){
            this.student=student
            val s ="${student.lastName} ${student.firstName[0]}. ${student.middleName[0]}."
            itemView.findViewById<TextView>(R.id.tvElement).text=s
            itemView.findViewById<ConstraintLayout>(R.id.clButton).visibility = View.GONE

        }

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?){
            val cl = itemView.findViewById<ConstraintLayout>(R.id.clButton)
            cl.visibility = View.VISIBLE
            lastItemView?.findViewById<ConstraintLayout>(R.id.clButton)?.visibility = View.GONE
            lastItemView = if (lastItemView == itemView)
                null
            else
                itemView

            if(cl.visibility == View.VISIBLE)
                itemView.findViewById<ImageButton>(R.id.imageDelete).setOnClickListener {
                    commitDeleteDialog(student)
                }
        }
    }

    private fun commitDeleteDialog(student: Student){
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить студента ${student.firstName} ${student.lastName} ${student.middleName} из списка группы")
        builder.setTitle("zxc?")
        builder.setPositiveButton(getString(R.string.commit)) {_, _ ->

        }
        builder.setNegativeButton(R.string.cancel, null)
        val alert = builder.create()
        alert.show()
    }

    private var lastItemView : View? = null

    private inner class GroupListAdapter(private val items: List<Student>)
        : RecyclerView.Adapter<GroupHolder>(){
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): GroupHolder {
            val view = layoutInflater.inflate(R.layout.layout_student_listelement,parent,false)
            return GroupHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: GroupHolder, position: Int) {
            holder.bind(items[position])
        }
    }



}