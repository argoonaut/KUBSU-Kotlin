package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.data.Student
import com.example.myapplication.repository.AppRepository
import com.example.myapplication.ui.*
import java.util.*

class MainActivity : AppCompatActivity(), FacultyFragment.Callbacks, GroupFragment.Callbacks {
    private var miNewFaculty: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, FacultyFragment.newInstance(), FACULTY_TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                    }
                    else
                        finish()
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        miNewFaculty = menu?.findItem(R.id.miNewFacultyGroup)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miNewFacultyGroup -> {
                val myFragment = supportFragmentManager.findFragmentByTag(GROUP_TAG)
                if (myFragment == null){
                    showNameInputDialog(0)
                }
                else
                    showNameInputDialog(1
                    )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showNameInputDialog(index:Int=-1){
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.name_input, null)
        builder.setView(dialogView)
            val nameInput = dialogView.findViewById(R.id.editTextTextPersonName) as EditText
        val tvInfo = dialogView.findViewById(R.id.tvInfo) as TextView
        builder.setTitle(getString(R.string.inputTitle))
        when (index){
            0 ->{

                tvInfo.text =getString(R.string.inputFaculty)
                builder.setPositiveButton(getString(R.string.commit)){_, _ ->
                 val s = nameInput.text.toString()
                 if (s.isNotBlank()){
                     AppRepository.get().newFaculty(s)
                 }
                }
            }
            1 -> {
                tvInfo.text = getString(R.string.inputGroup)
                builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
                    val s = nameInput.text.toString()
                    if (s.isNotBlank()) {
                        AppRepository.get().newGroup(GroupFragment.getFaculryID, s)

                    }
                }
            }
        }
        builder.setNegativeButton(R.string.cancel, null)
        val alert = builder.create()
        alert.show()
    }

    override fun setTitle(_title: String) {
        title = _title
    }

    override fun showStudent(groupID: UUID, student: Student?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, StudentFragment.newInstance(groupID, student), STUDENT_TAG)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun showFaculty(id: UUID) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, GroupFragment.newInstance(id), GROUP_TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
}