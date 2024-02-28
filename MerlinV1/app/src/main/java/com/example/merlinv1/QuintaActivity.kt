package com.example.merlinv1
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class QuintaActivity : AppCompatActivity() {
    private lateinit var datePicker: DatePicker
    private lateinit var spinnerCollections: Spinner
    private lateinit var enviarButton: Button
    private lateinit var analizarButton: Button
    private lateinit var fechaText: TextView
    private lateinit var txtvalor: TextView
    private lateinit var txtvalor2: TextView
    private lateinit var Textrestante1: TextView
    private lateinit var Textrestante2: TextView
    private lateinit var Texttotalfilas: TextView
    private lateinit var TextD: TextView
    private lateinit var TextIRI: TextView
    private lateinit var TextPSI: TextView
    private val firestore = FirebaseFirestore.getInstance()
    private var coll = "0"
    private var docum = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quinta)
        enviarButton = findViewById(R.id.enviarButton)

        analizarButton= findViewById(R.id.analizarButton)
        //analizarButton.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
      //  enviarButton.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))

        datePicker = findViewById(R.id.datePicker)
        spinnerCollections = findViewById(R.id.spinnerCollections)
        // Obtiene referencias a los elementos de la interfaz de usuario
         fechaText = findViewById(R.id.txtfecha)

        txtvalor = findViewById(R.id.txtvalor)
        txtvalor2 = findViewById(R.id.txtvalor2)
        Textrestante1 = findViewById(R.id.Textrestante1)
        Textrestante2 = findViewById(R.id.Textrestante2)
        Texttotalfilas = findViewById(R.id.Texttotalfilas)
        TextD = findViewById(R.id.TextD)
        TextIRI = findViewById(R.id.TextIRI)
        TextPSI = findViewById(R.id.TextPSI)
        //Toast.makeText(this, "Fecha seleccionada: $fecha", Toast.LENGTH_SHORT).show()


        analizarButton.setOnClickListener {
            val selectedDate = getSelectedDate()
            val fecha = selectedDate
            fechaText.text = fecha.toString()
             coll = fechaText.text.toString()
            selectCollections()
        }

        enviarButton.setOnClickListener {

            queryFirestore(coll, docum)
            //onGuardarClick()

        }


        // Llamada a la función para cargar las colecciones

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

    private fun loadCollections() {
        firestore.collection(coll).get()
            .addOnSuccessListener { result ->
                val collectionNames = ArrayList<String>()

                for (document in result) {
                    collectionNames.add(document.id)
                }

                // Configurar el adaptador para el Spinner
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, collectionNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCollections.adapter = adapter

                // Manejar la selección del Spinner
                spinnerCollections.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedCollection = parent.getItemAtPosition(position).toString()

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                })
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al obtener las colecciones: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun selectCollections() {
        txtvalor.text = "Coleccion:"
        txtvalor2.text =  "Documento:"
        Textrestante1.text = "Restante1:"
        Textrestante2.text =  "Restante2:"
        Texttotalfilas.text = "Totalfilas:"
        TextD.text =  "D:"
        TextIRI.text =  "IRI:"
        TextPSI.text =  "PSI:"
        coll = fechaText.text.toString()
        firestore.collection(coll).get()
            .addOnSuccessListener { result ->
                val collectionNames = ArrayList<String>()

                for (document in result) {
                    collectionNames.add(document.id)
                }

                // Configurar el adaptador para el Spinner
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, collectionNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCollections.adapter = adapter

                // Manejar la selección del Spinner
                spinnerCollections.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        docum = parent.getItemAtPosition(position).toString()

                        Toast.makeText(
                            this@QuintaActivity,
                            "Seleccionaste: $docum",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                })
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al obtener las colecciones: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun queryFirestore(collection: String, documentId: String) {
        val docRef = firestore.collection(collection).document(documentId)

        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val restante1 = documentSnapshot.getDouble("restante1") // Reemplaza "nombre_campo" con el nombre real del campo
                    val restante2 = documentSnapshot.getDouble("restante2") // Reemplaza "nombre_campo" con el nombre real del campo
                    val totalfilas = documentSnapshot.getDouble("totalfilas") // Reemplaza "nombre_campo" con el nombre real del campo
                    val D = documentSnapshot.getDouble("D") // Reemplaza "nombre_campo" con el nombre real del campo
                    val IRI = documentSnapshot.getDouble("IRI") // Reemplaza "nombre_campo" con el nombre real del campo
                    val PSI = documentSnapshot.getDouble("PSI") // Reemplaza "nombre_campo" con el nombre real del campo

                    txtvalor.text = "Coleccion: $coll"
                    txtvalor2.text =  "Documento: $docum"
                    Textrestante1.text = "Restante1: $restante1"
                    Textrestante2.text =  "Restante2: $restante2"
                    Texttotalfilas.text = "Totalfilas: $totalfilas"
                    TextD.text =  "D: $D"
                    TextIRI.text =  "IRI: $IRI"
                    TextPSI.text =  "PSI: $PSI"
                } else {
                    Textrestante1.text = "Documento no encontrado"
                }
            }
            .addOnFailureListener { e ->
                Textrestante1.text = "Error al obtener el documento: $e"
            }
    }
}
