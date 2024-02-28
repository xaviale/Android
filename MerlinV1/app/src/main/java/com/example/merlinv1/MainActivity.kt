package com.example.merlinv1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    private lateinit var btnReporte: Button
    private lateinit var btnRegistro: Button
    private lateinit var btnFecha: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReporte= findViewById(R.id.btnReporte)
        btnRegistro = findViewById(R.id.btnRegistro)
        btnFecha = findViewById(R.id.btnFecha)
        // btnRegistro.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
        // btnReporte.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))

        btnReporte.setOnClickListener {
            startActivity(Intent(this@MainActivity, QuintaActivity::class.java))
        }

        btnRegistro.setOnClickListener {
            startActivity(Intent(this@MainActivity, TerceraActivity::class.java))
        }
        btnFecha.setOnClickListener {
            startActivity(Intent(this@MainActivity, SegundaActivity::class.java))
        }
    }
}