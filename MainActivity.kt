package com.example.bmiceshi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editTextWeight = findViewById<EditText>(R.id.editTextWeight)
        val editTextHeight = findViewById<EditText>(R.id.editTextHeight)
        val buttonCalculate = findViewById<Button>(R.id.buttonCalculate)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)
        buttonCalculate.setOnClickListener {
            val weightString = editTextWeight.text.toString()
            val heightString = editTextHeight.text.toString()
            if (!weightString.isEmpty() && !heightString.isEmpty()) {
                val weight = weightString.toDouble()
                val height = heightString.toDouble()
                val bmi = weight / (height * height)
                textViewResult.text = String.format("%.2f", bmi) + " BMI"
                if (bmi < 18.5) {
                    textViewResult.append("\n低体重多吃")
                } else if (bmi < 24.9) {
                    textViewResult.append("\n正常，保持")
                } else if (bmi < 29.9) {
                    textViewResult.append("\n高体重少吃")
                } else {
                    textViewResult.append("\n胖胖不好")
                }
            }
        }
    }
}

