package com.example.prueba6
//import kotlinx.android.synthetic.segunda.activity_main.*
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import kotlin.math.exp
class SegundaActivity : AppCompatActivity() {

    private lateinit var editTextVariable: EditText
    private lateinit var buttonCalculate: Button
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)

        editTextVariable = findViewById(R.id.editTextVariable)
        buttonCalculate = findViewById(R.id.buttonCalculate)
        textViewResult = findViewById(R.id.textViewResult)

        buttonCalculate.setOnClickListener {
            calculateResult()
        }
    }

    private fun calculateResult() {
        val variable: Double = editTextVariable.text.toString().toDoubleOrNull() ?: 0.0
        val result: Double = variable * exp(1.0) // e^1
        val formattedNumber = formatToTwoDecimals(result)
        val umber = formattedNumber.toDouble()
        textViewResult.text = "Result: $umber"
    }
    private fun formatToTwoDecimals(number: Double): String {
        val decimalFormat = DecimalFormat("#.###")

        return decimalFormat.format(number)
    }
}
