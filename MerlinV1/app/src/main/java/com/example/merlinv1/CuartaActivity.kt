package com.example.merlinv1
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CuartaActivity : AppCompatActivity() {
    private lateinit var datePicker: DatePicker

    private lateinit var fechaText: TextView
    private lateinit var nregistro: EditText
    private lateinit var Textrestante1: TextView
    private lateinit var Textrestante2: TextView
    private lateinit var Texttotalfilas: TextView
    private lateinit var TextD: TextView
    private lateinit var TextIRI: TextView
    private lateinit var TextPSI: TextView
    private lateinit var enviarButton: Button
    private lateinit var registrarButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuarta)
        datePicker = findViewById(R.id.datePicker)
        // Obtiene referencias a los elementos de la interfaz de usuario
       fechaText= findViewById(R.id.txtfecha)
        nregistro= findViewById(R.id.txtnregistro)
        Textrestante1 = findViewById(R.id.Textrestante1)
        Textrestante2 = findViewById(R.id.Textrestante2)
        Texttotalfilas = findViewById(R.id.Texttotalfilas)
        TextD = findViewById(R.id.TextD)
        TextIRI = findViewById(R.id.TextIRI)
        TextPSI = findViewById(R.id.TextPSI)
        enviarButton= findViewById(R.id.enviarButton)
        registrarButton= findViewById(R.id.registrarButton)
       // enviarButton.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))

        val restante1 = intent.getStringExtra("restante1")
        val restante2 = intent.getStringExtra("restante2")
        val cfilasTotal = intent.getStringExtra("totalfilas")
        val totalD = intent.getStringExtra("totalDD")
        val IRI = intent.getStringExtra("IRI")
        val PSI = intent.getStringExtra("PSI")
        // Configura un listener para el botón de envío
        enviarButton.setOnClickListener {
            if(nregistro.text.toString()!=null){


            val selectedDate = getSelectedDate()
            val fecha = selectedDate
            Toast.makeText(this, "Datos Guardados! Fecha: $fecha", Toast.LENGTH_SHORT).show()
            fechaText.text = fecha.toString()
            val vrestante1 = ("$restante1").toDouble()
            Textrestante1.text ="$restante1"
            val vrestante2= ("$restante2").toDouble()
            Textrestante2.text ="$restante2"
            val vtotalfilas = ("$cfilasTotal").toDouble()
            Texttotalfilas.text ="$cfilasTotal"
            val vD= ("$totalD").toDouble()
            TextD.text ="$totalD"
            val IRII= ("$IRI").toDouble()
            TextIRI.text ="$IRI"
            val PSII= ("$PSI").toDouble()
            TextPSI.text ="$PSI"
            val nnregistro = nregistro.text.toString().trim()
            val fechatxt = fechaText.text.toString().trim()
            // Verifica si se ingresó un valor antes de enviar a Firestore
            if (nnregistro.isNotEmpty() ) {
                // Llamada a la función para enviar el valor a Firestore
                enviarValorAFirestore(fechatxt,nnregistro , vrestante1, vrestante2,vtotalfilas,vD,IRII,PSII)
                atras()
            }
        }
        }
        registrarButton.setOnClickListener {
            if(nregistro.text.toString()!="n° registro"){


                val selectedDate = getSelectedDate()
                val fecha = selectedDate
                Toast.makeText(this, "Datos Actualizados!", Toast.LENGTH_SHORT).show()
                fechaText.text = fecha.toString()
                val vrestante1 = ("$restante1").toDouble()
                Textrestante1.text ="$restante1"
                val vrestante2= ("$restante2").toDouble()
                Textrestante2.text ="$restante2"
                val vtotalfilas = ("$cfilasTotal").toDouble()
                Texttotalfilas.text ="$cfilasTotal"
                val vD= ("$totalD").toDouble()
                TextD.text ="$totalD"
                val IRII= ("$IRI").toDouble()
                TextIRI.text ="$IRI"
                val PSII= ("$PSI").toDouble()
                TextPSI.text ="$PSI"
                val nnregistro = nregistro.text.toString().trim()
                val fechatxt = fechaText.text.toString().trim()
                // Verifica si se ingresó un valor antes de enviar a Firestore

            }else{
                Toast.makeText(this, "Añada un numero al registro!", Toast.LENGTH_SHORT).show()

            }
        }
    }
    fun atras() {


        // Aquí puedes obtener los datos de la tabla y pasarlos a la siguiente actividad
        val intent = Intent(this, MainActivity()::class.java)

        startActivity(intent)
    }
    private fun getSelectedDate(): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("dd_MM_yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    // Función para enviar un valor a Firestore
    private fun enviarValorAFirestore(coleccion: String, registro: String, vrestante1: Double, vrestante2: Double, vtotalfilas: Double, vD: Double, IRII: Double, PSII: Double) {
        // Utiliza coroutines para realizar la operación en un hilo separado
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Obtiene la referencia a la colección y al documento específico
                val docRef = db.collection(coleccion).document(registro)

                // Crea un mapa con el campo que deseas actualizar

                val data = hashMapOf("restante1" to vrestante1,"restante2" to vrestante2,"totalfilas" to vtotalfilas,"D" to vD,"IRI" to IRII,"PSI" to PSII)

                // Actualiza el documento en Firestore
                docRef.set(data)
                    .addOnSuccessListener {
                        // Operación exitosa
                        println("Valor enviado correctamente a Firestore")
                    }
                    .addOnFailureListener { e ->
                        // Manejo de errores
                        println("Error al enviar valor a Firestore: $e")
                    }
            } catch (e: Exception) {
                // Manejo de excepciones
                println("Excepción: $e")
            }

        }
    }
}




