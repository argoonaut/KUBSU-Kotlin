package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

private const val EXTRA_ANSWER = "com.example.myapplication"
const val EXTRA_ANSWER_SHOWN = "com.example.myapplication.answer_shown"

class CheatActivity : AppCompatActivity() {
    private var answer = false
    private  lateinit var answerTextView: TextView

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER, answerIsTrue)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        answer=intent?.getBooleanExtra(EXTRA_ANSWER, false)?:false
        setContentView(R.layout.activity_cheat)
        answerTextView=findViewById(R.id.TVCheat)
        answerTextView.visibility= View.GONE
        findViewById<Button>(R.id.buttonCheat).setOnClickListener{
            showAnswerDialog()
        }
        if (savedInstanceState != null) {
            answer = savedInstanceState.getBoolean(EXTRA_ANSWER, answer)
            val answerShown = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false)
            if (answerShown) {
                showAnswer()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EXTRA_ANSWER_SHOWN, answerTextView.visibility == View.VISIBLE)
        outState.putBoolean(EXTRA_ANSWER, answer)
    }

    private fun showAnswer(){
        answerTextView.visibility = View.VISIBLE
        answerTextView.text = when {
            answer == true -> "Правда"
            else -> "Ложь"
        }
        setAnswerShownResult(true)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun showAnswerDialog(){
        AlertDialog.Builder(this)
            .setTitle("Читерство плохо!")
            .setMessage("Вы действительно хотите увидеть ответ?")
            .setPositiveButton("ДА"){ _, _ ->
                showAnswer()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }
}