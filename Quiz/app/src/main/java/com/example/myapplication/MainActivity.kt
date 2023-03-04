package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var btnTrue : Button
    private lateinit var btnFalse : Button
    private lateinit var nextButton : ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request code
            val data: Intent? = result.data //Bundle
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    private val quizViewModel: ViewModelQuiz by lazy {
        val provider = ViewModelProvider(this)
        provider.get(ViewModelQuiz::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quizViewModel.CurrentIndex=0
        btnTrue= findViewById(R.id.BTNYes)
        btnFalse= findViewById(R.id.BTNNo)
        nextButton= findViewById(R.id.BTNNext)
        prevButton= findViewById(R.id.BTNPrev)
        questionTextView = findViewById(R.id.TVQuiz)

        nextButton.setOnClickListener{
            quizViewModel.MoveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.MoveToPrev()
            updateQuestion()
        }

        btnTrue.setOnClickListener{
            checkAnswer(true)
        }

        btnFalse.setOnClickListener{
            checkAnswer(false)
        }

        findViewById<Button>(R.id.buttonAnswer).setOnClickListener{
            //val intent = Intent(this, CheatActivity::class.java)
            //intent.putExtra("answer", quizViewModel.CurrentQuestionAnswer)
            val intent = CheatActivity.newIntent(this, quizViewModel.CurrentQuestionAnswer)
            //startActivity(intent)
            resultLauncher.launch(intent)
        }

        updateQuestion()
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.CurrentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        if (quizViewModel.isCheater) {
            Toast.makeText(this@MainActivity, "Списывать нехорошо", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val correctAnswer = quizViewModel.CurrentQuestionAnswer

        val messageResId = when {
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this@MainActivity, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

}