package com.example.myapplication.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Faculty
import com.example.myapplication.databinding.FragmentFacultyBinding
import com.example.myapplication.models.FacultyViewModel
import java.util.UUID

const val FACULTY_TAG = "FacultyFragment"
const  val FACULTY_TITLE="Университет"

class FacultyFragment : Fragment() {
    private lateinit var viewModel: FacultyViewModel
    private var _binding: FragmentFacultyBinding? = null
    val binding
      get() = _binding!!

    private var adapter: FacultyListAdapter = FacultyListAdapter(emptyList())

    companion object {
        fun newInstance() = FacultyFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFacultyBinding.inflate(inflater, container,false)
        //отображение по вертикали
        binding.rvFaculty.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FacultyViewModel:: class.java)
        viewModel.university.observe(viewLifecycleOwner){
            adapter=FacultyListAdapter(it)
            binding.rvFaculty.adapter=adapter
        }
        callbacks?.setTitle(FACULTY_TITLE) //безопасный вызов метода
    }

    private inner class FacultyHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener{
            lateinit var faculty: Faculty

            fun bind(faculty: Faculty){
                this.faculty=faculty
                itemView.findViewById<TextView>(R.id.tvFacultyElement).text=faculty.name
            }

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?){
            callbacks?.showFaculty (faculty.id)
        }
        }

    private inner class FacultyListAdapter(private val items: List<Faculty>)
        : RecyclerView.Adapter<FacultyHolder>(){
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FacultyHolder {
            val view = layoutInflater.inflate(R.layout.element_faculty_list,parent,false)
            return FacultyHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: FacultyHolder, position: Int) {
            holder.bind(items[position])
        }
        }
    //интерфейс для изменения title приложения на университет . Взаимодействие активити и фрагмента
    interface Callbacks{
        fun setTitle(_title: String)
        fun showFaculty (id: UUID)
    }
    var callbacks : Callbacks? =null
    override fun onAttach(context: Context){
        super.onAttach(context)
        callbacks=context as Callbacks
    }

    override fun onDetach(){
        callbacks=null
        super.onDetach()
    }
    //
}