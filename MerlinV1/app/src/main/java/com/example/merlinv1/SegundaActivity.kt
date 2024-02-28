package com.example.merlinv1
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class SegundaActivity : AppCompatActivity() {

    private lateinit var editTextData: EditText
    private lateinit var btnReadData: Button
    private lateinit var btnWriteData: Button
    private lateinit var btnON: Button
    private lateinit var btnOFF: Button
    private lateinit var btnON2: Button
    private lateinit var btnOFF2: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)

        editTextData = findViewById(R.id.editTextData)
        btnReadData = findViewById(R.id.btnReadData)
        btnWriteData = findViewById(R.id.btnWriteData)
        btnON = findViewById(R.id.btnON)
        btnOFF = findViewById(R.id.btnOFF)
        btnON2 = findViewById(R.id.btnON2)
        btnOFF2 = findViewById(R.id.btnOFF2)
        // Obtén la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("/prueba1/int")

        btnReadData.setOnClickListener {
            // Lee el dato de la base de datos
            readDataFromDatabase()
        }

        btnWriteData.setOnClickListener {
            // Escribe el dato en la base de datos
            writeDataToDatabase()
        }
        btnON.setOnClickListener {
            // Escribe el dato en la base de datos
            writeON()
        }
        btnOFF.setOnClickListener {
            // Escribe el dato en la base de datos
            writeOFF()
        }
        btnON2.setOnClickListener {
            // Escribe el dato en la base de datos
            writeON2()
        }
        btnOFF2.setOnClickListener {
            // Escribe el dato en la base de datos
            writeOFF2()
        }
    }

    private fun readDataFromDatabase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Obtiene el valor actual del dato en la base de datos
                val value = dataSnapshot.getValue(String::class.java)
                editTextData.setText(value)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja errores de lectura de la base de datos
            }
        })
    }

    private fun writeDataToDatabase() {
        val newData = editTextData.text.toString()

        // Escribe el nuevo dato en la base de datos
        databaseReference.setValue(newData)
    }
    private fun writeON() {
        val newData = 1

        // Escribe el nuevo dato en la base de datos
        databaseReference.setValue(newData)
    }
    private fun writeOFF() {
        val newData = 2

        // Escribe el nuevo dato en la base de datos
        databaseReference.setValue(newData)
    }
    private fun writeON2() {
       val  newData = 3

        // Escribe el nuevo dato en la base de datos
        databaseReference.setValue(newData)
    }
    private fun writeOFF2() {
        val newData = 4

        // Escribe el nuevo dato en la base de datos
        databaseReference.setValue(newData)
    }
}