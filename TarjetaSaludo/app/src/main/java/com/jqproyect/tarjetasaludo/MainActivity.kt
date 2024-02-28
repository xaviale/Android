package com.jqproyect.tarjetasaludo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val greetingTextView = findViewById<TextView>(R.id.greetingTextView)
        greetingTextView.setOnClickListener {
            // Cambiar el mensaje al hacer clic en el TextView
            greetingTextView.text = "¡Hola desde Android Studio!"
        }
    }
}