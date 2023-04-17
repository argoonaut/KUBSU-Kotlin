package com.example.myapplication.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.get
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.R
import com.example.myapplication.data.Faculty
import com.example.myapplication.data.Group
import com.example.myapplication.data.Student
import com.example.myapplication.databinding.FragmentFacultyBinding
import com.example.myapplication.databinding.FragmentGroupBinding
import com.example.myapplication.models.GroupViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.UUID

const val  GROUP_TAG = "GroupFragment"


class GroupFragment : Fragment() {
    private var _binding: FragmentGroupBinding? = null
    val binding
        get() = _binding!!


    companion object {
        private lateinit var id : UUID
        private var _groupID: Group? = null
        fun newInstance(id: UUID): GroupFragment{
            GroupFragment()
            this.id=id
            return GroupFragment()
        }
        val getFaculryID
            get() = id

        val getGroupID
            get() = _groupID
    }

    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentGroupBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider (this).get(GroupViewModel::class.java)
        viewModel.setFacultyID(getFaculryID)
        viewModel.faculty.observe(viewLifecycleOwner){
            updateUI(it)
            callbacks?.setTitle(it?.name?: "")
        }
    }
    private var tabPosition : Int =0

    private fun updateUI (faculty : Faculty?){
        binding.tabGroup.clearOnTabSelectedListeners()
        binding.tabGroup.removeAllTabs()

        binding.faBtnNewStudent.visibility = if((faculty?.groups?.size ?: 0) > 0) {
            binding.faBtnNewStudent.setOnClickListener {
                callbacks?.showStudent(faculty?.groups!!.get(tabPosition).id, null)
            }
            View.VISIBLE
        } else View.GONE
            for (i in 0 until (faculty?.groups?.size ?: 0)){
                binding.tabGroup.addTab(binding.tabGroup.newTab().apply { text= i.toString()
                })
            }

        val adapter = GroupPageAdapter(requireActivity(), faculty!!)
        binding.vpGroup.adapter=adapter
        TabLayoutMediator(binding.tabGroup, binding.vpGroup, true, true) {
            tab,pos -> tab.text = faculty?.groups?.get(pos)?.name
        }.attach()

        binding.tabGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
               tabPosition=tab?.position!!
                group = faculty.groups.get(tabPosition) // ? wtf
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private inner class GroupPageAdapter(fa: FragmentActivity, private val faculty: Faculty):
    FragmentStateAdapter(fa){
        override fun getItemCount(): Int {
            return (faculty.groups?.size ?: 0)
        }

        override fun createFragment(position: Int): Fragment {
            return  GroupListFragment(faculty.groups?.get(position)?.students)
        }
    }

    // Интерфейс для изменения title приложения на университет
    interface Callbacks{
        fun setTitle(_title: String)
        fun showStudent(groupID: UUID, student: Student?)
    }

    var callbacks : Callbacks? = null

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