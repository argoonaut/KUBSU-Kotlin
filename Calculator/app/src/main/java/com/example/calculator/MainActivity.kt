package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    // Отложенная инициализация переменных
    private lateinit var builder: AlertDialog.Builder
    private lateinit var button_Dot : Button
    private lateinit var button_Rofl : Button
    private lateinit var button_0 : Button
    private lateinit var button_1 : Button
    private lateinit var button_2 : Button
    private lateinit var button_3 : Button
    private lateinit var button_4 : Button
    private lateinit var button_5 : Button
    private lateinit var button_6 : Button
    private lateinit var button_7 : Button
    private lateinit var button_8 : Button
    private lateinit var button_9 : Button
    private lateinit var textInput : EditText
    private lateinit var button_Sum : Button
    private lateinit var button_Division : Button
    private lateinit var button_Multiplication : Button
    private lateinit var button_Subtraction : Button
    private lateinit var button_Equal : Button
    private lateinit var textOperation : TextView
    private lateinit var textResult : TextView
    private lateinit var button_AC : Button
    private lateinit var button_C : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        builder = AlertDialog.Builder(this)

        button_Rofl = findViewById(R.id.button_Rofl)
        button_Dot = findViewById(R.id.button_Dot)
        button_0 = findViewById(R.id.button_0)
        button_1 = findViewById(R.id.button_1)
        button_2 = findViewById(R.id.button_2)
        button_3 = findViewById(R.id.button_3)
        button_4 = findViewById(R.id.button_4)
        button_5 = findViewById(R.id.button_5)
        button_6 = findViewById(R.id.button_6)
        button_7 = findViewById(R.id.button_7)
        button_8 = findViewById(R.id.button_8)
        button_9 = findViewById(R.id.button_9)
        textInput = findViewById(R.id.TextInput)
        button_Division = findViewById(R.id.button_Division)
        button_Multiplication = findViewById(R.id.button_Multiplication)
        button_Sum = findViewById(R.id.button_Sum)
        button_Subtraction = findViewById(R.id.button_Subtraction)
        button_Equal = findViewById(R.id.button_Equal)
        textOperation = findViewById(R.id.TextOperation)
        textResult = findViewById(R.id.TextResult)
        button_AC = findViewById(R.id.button_AC)
        button_C = findViewById(R.id.button_C)

        // Диалоговое окно для кнопки с смайликом
        button_Rofl.setOnClickListener {
            builder.setTitle("Осторожно шутка!")
                .setMessage("Сколько разработчиков Microsoft требуется, чтобы вкрутить лампочку?")
                .setCancelable(true)
                .setPositiveButton("2"){dialogInterface,it->
                    dialogInterface.cancel()
                }
                .setNegativeButton("1"){dialogInterface,it->
                    dialogInterface.cancel()
                }
                .setNeutralButton("Ответ"){dialogInterface,it->
                    Toast.makeText(this@MainActivity, "Ни одного, они просто определят темноту новым стандартом", Toast.LENGTH_LONG).show()
                }
                .show()
        }

        button_Dot.setOnClickListener {
            textInput.text = textInput.text.append(".")
        }

        button_0.setOnClickListener {
            textInput.text = textInput.text.append("0")
        }

        button_1.setOnClickListener {
            textInput.text = textInput.text.append("1")
        }

        button_2.setOnClickListener {
            textInput.text = textInput.text.append("2")
        }

        button_3.setOnClickListener {
            textInput.text = textInput.text.append("3")
        }

        button_4.setOnClickListener {
            textInput.text = textInput.text.append("4")
        }

        button_5.setOnClickListener {
            textInput.text = textInput.text.append("5")
        }

        button_6.setOnClickListener {
            textInput.text = textInput.text.append("6")
        }

        button_7.setOnClickListener {
            textInput.text = textInput.text.append("7")
        }

        button_8.setOnClickListener {
            textInput.text = textInput.text.append("8")
        }

        button_9.setOnClickListener {
            textInput.text = textInput.text.append("9")
        }

        button_Sum.setOnClickListener {
            if (textOperation.text.toString()!="+" && textOperation.text.toString()!="")
                textOperation.text = "+"
            else {
                textOperation.text = "+"
                if (textResult.text.isEmpty()) {
                    textResult.text = textInput.text.toString()
                    textInput.setText("")
                } else {
                    var Summ: Double? = textResult.text.toString().toDouble()
                    if (!textInput.text.isEmpty())
                        Summ = Summ?.plus(textInput.text.toString().toDouble())
                    if (Summ != null) {
                        if (Summ % 1.0 == 0.0)
                            textResult.text = Summ.toString()
                        else
                            textResult.text = Summ.toString()
                    }
                    textInput.setText("")
                }
            }
        }

        button_Division.setOnClickListener {
            if (textOperation.text.toString()!="/" && textOperation.text.toString()!="")
                textOperation.text = "/"
            else {
                textOperation.text = "/"
                if (textResult.text.isEmpty()) {
                    textResult.text = textInput.text.toString()
                    textInput.setText("")
                } else {
                    if (!textInput.text.isEmpty()) {
                        if (textInput.text.toString().toDouble() == 0.0) {
                            textResult.text = "Ошибка"
                            textInput.setText("")
                        } else {
                            var Div: Double? = textResult.text.toString().toDouble()
                            Div = Div?.div(textInput.text.toString().toDouble())
                            if (Div != null) {
                                if (Div % 1.0 == 0.0)
                                    textResult.text = Div.toInt().toString()
                                else
                                    textResult.text = Div.toString()
                            }
                            textInput.setText("")
                        }

                    }
                }
            }
        }

        button_Subtraction.setOnClickListener{
            if (textOperation.text.toString()!="-" && textOperation.text.toString()!="")
                textOperation.text = "-"
            else {
                textOperation.text = "-"
                if (textResult.text.isEmpty()) {
                    if (!textInput.text.isEmpty())
                        textResult.text = textInput.text.toString()
                    textInput.setText("")
                } else {
                    if (!textInput.text.isEmpty()) {
                        var Minus: Double? = -1 * textInput.text.toString().toDouble()
                        Minus = Minus!! + textResult.text.toString().toDouble()
                        if (Minus != null) {
                            if (Minus % 1.0 == 0.0)
                                textResult.text = Minus.toInt().toString()
                            else
                                textResult.text = Minus.toString()
                        }
                        textInput.setText("")
                    }
                }
            }
        }

        button_Multiplication.setOnClickListener {
            if (textOperation.text.toString()!="*" && textOperation.text.toString()!="")
                textOperation.text = "*"
            else {
                textOperation.text = "*"
                if (textResult.text.isEmpty()) {
                    //textInput.setText(textInput.text.toString())
                    textResult.text = textInput.text.toString()
                    textInput.setText("")
                } else {
                    if (!textInput.text.isEmpty()) {
                        var Mult: Double? = textInput.text.toString().toDouble()
                        Mult = Mult!! * textResult.text.toString().toDouble()
                        if (Mult != null) {
                            if (Mult % 1.0 == 0.0) {
                                //textInput.setText(Mult.toInt().toString())
                                textResult.text = Mult.toInt().toString()

                            }
                            else
                                textResult.text = toString()
                        }
                    }
                    textInput.setText("")
                }
            }
        }

        button_Equal.setOnClickListener{

            if (textResult.text.isEmpty())
            {
                //textInput.setText(textInput.text.toString())
                textResult.text = textInput.text.toString()
                textInput.setText("")
            }

            when(textOperation.text.toString())
            {
                "*" -> {
                    var Mult: Double? = textInput.text.toString().toDouble()
                    Mult = Mult!! * textResult.text.toString().toDouble()
                    if (Mult != null) {
                        if (Mult!! % 1.0 == 0.0) {
                            textInput.setText(Mult!!.toInt().toString())
                            textResult.text = ""
                            textOperation.setText("")
                            //textResult.text = Mult!!.toInt().toString()
                        }
                        else {
                            textInput.setText(Mult.toString())
                            textResult.text = ""
                            textOperation.setText("")
                        }
                    }
                }
                "+" -> {
                    var Summ: Double? = textResult.text.toString().toDouble()
                    if (!textInput.text.isEmpty())
                        Summ = Summ?.plus(textInput.text.toString().toDouble())
                    if (Summ != null) {
                        if (Summ!! % 1.0 == 0.0) {
                            textInput.setText(Summ.toString())
                            textResult.text = ""
                            textOperation.setText("")
                            //textResult.text = Summ.toString()
                        }
                        else {
                            textInput.setText(Summ.toString())
                            textResult.text = ""
                            textOperation.setText("")
                        }
                    }
                }
                "/" -> {
                    if (!textInput.text.isEmpty()) {
                        if (textInput.text.toString().toDouble() == 0.0) {
                            textInput.setText("Ошибка")
                            textResult.setText("")
                            textOperation.setText("")
                        } else {
                            var Div: Double? = textResult.text.toString().toDouble()
                            Div = Div?.div(textInput.text.toString().toDouble())
                            if (Div != null) {
                                if (Div!! % 1.0 == 0.0){
                                    textInput.setText(Div!!.toInt().toString())
                                    textResult.setText("")
                                    textOperation.setText("")
                                }
                                else {
                                    textInput.setText(Div.toString())
                                    textResult.text = ""
                                    textOperation.setText("")
                                }
                            }
                        }
                    }
                }
                "-" -> {
                    var Minus: Double? = -1 * textInput.text.toString().toDouble()
                    Minus = Minus!! + textResult.text.toString().toDouble()
                    if (Minus != null) {
                        if (Minus!! % 1.0 == 0.0) {
                            textInput.setText(Minus!!.toInt().toString())
                            textResult.text = ""
                            textOperation.setText("")
                        }
                        else {
                            textInput.setText(Minus.toString())
                            textResult.text = ""
                            textOperation.setText("")
                        }
                    }
                }
            }

                /*"+" -> button_Sum.callOnClick()
                "/" -> button_Division.callOnClick()
                "-" -> button_Subtraction.callOnClick()*/

            //textOperation.text = "="

        }

        button_AC.setOnClickListener{
            textOperation.setText("")
            textInput.setText("")
            textResult.setText("")
        }

        button_C.setOnClickListener {
            if (textOperation.text.isEmpty()) textResult.setText(textResult.text.toString().dropLast(1))
            if (textInput.text.isEmpty()) textOperation.setText("")
            textInput.setText(textInput.text.toString().dropLast(1))
        }

    }
}