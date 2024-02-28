package com.example.merlinv1

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat
import java.util.UUID
import kotlin.math.exp
import android.media.MediaPlayer



class SeptimaActivity : AppCompatActivity() {
    private val handler: Handler = Handler()
    private val TAG = "ESP32"
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID para el perfil SPP
    private var bluetoothSocket: BluetoothSocket? = null
    private lateinit var inputStream: InputStream
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var Textdato: TextView


    private lateinit var mediaPlayerBeep: MediaPlayer
    private lateinit var mediaPlayerTry: MediaPlayer
    private lateinit var mediaPlayerCompl: MediaPlayer

    private lateinit var tableLayout: TableLayout
    private lateinit var Textrestante1: TextView
    private lateinit var Textrestante2: TextView
    private lateinit var Texttotalfilas: TextView
    private lateinit var TextD: TextView
    private lateinit var TextIRI: TextView
    private lateinit var TextPSI: TextView
    private lateinit var btnGuardar: Button
    private lateinit var btnBorrar: Button
    private lateinit var btnAnalizar: Button

    private var filasss = 0
    private val rows = 51
    private val columns = 21
    private var selectedRow = -1
    private var contX = 0
    private var contY = 0
    private var contaa = 0
    private var contAnalizar = 0
    private var contAnalizar2 = 0
    private var contfilas = 0
    private var contguion = 0

    private var contfilasTotal = 0
    private var totalD = 0.00
    private var iri = 0.00
    private var psi = 0.00
    private  var contrestante = 0.000
    private  var contrestante2 = 0.000
    private  var contfilasTotal2 = 0.000
    private  var contrrr = 0
    private var contXF = 0
    private var contXFRes = 0
    private var activobt = 0
    @SuppressLint("MissingInflatedId")




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_septima)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnBorrar = findViewById(R.id.btnBorrar)
        btnAnalizar = findViewById(R.id.btnAnalizar)

        //btnAnalizar.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
        // btnGuardar.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
        //btnBorrar.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
        mediaPlayerBeep = MediaPlayer.create(this, R.raw.beep)
        mediaPlayerTry = MediaPlayer.create(this, R.raw.tryagain)
        mediaPlayerCompl = MediaPlayer.create(this, R.raw.completed)

        Textrestante1 = findViewById(R.id.Textrestante1)
        Textrestante2 = findViewById(R.id.Textrestante2)
        Texttotalfilas = findViewById(R.id.Texttotalfilas)
        TextD = findViewById(R.id.TextD)
        TextIRI = findViewById(R.id.TextIRI)
        TextPSI = findViewById(R.id.TextPSI)
        tableLayout = findViewById(R.id.tableLayout)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
        Textdato = findViewById(R.id.Textdato)
        val discoverBtn: Button = findViewById(R.id.btnDiscover)
        discoverBtn.setOnClickListener {
            discoverDevices()

        }

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no es compatible en este dispositivo", Toast.LENGTH_SHORT).show()
           // finish()
        }


        createTable()
        btnGuardar.setOnClickListener {
            //onDestroy()
            onGuardarClick()
            Toast.makeText(this, "Guardar Registro", Toast.LENGTH_SHORT).show()

        }

        btnBorrar.setOnClickListener {

            limpiar()
        }

        btnAnalizar.setOnClickListener {
            if( contaa >= 200){

                activobt=0

            contAnalizar = 0
            onAnalizarClick()
            contAnalizar2 = 0
            onAnalizar2Click()
            contfilas = 0
            contfilasTotal = 0

            onAnalizar3Click()
            contrrr=0
            contfilas = 0
            contrestante = 0.000
            onAnalizar4Click()
            val contrest1= formatToTwoDecimals(contrestante)
            contrestante =contrest1.toDouble()
            contrrr=0
            contfilas = 0
            contrestante2 = 0.000
            onAnalizar5Click()
            val contrest2= formatToTwoDecimals(contrestante2)
            contrestante2 =contrest2.toDouble()
            if(contrestante>=1.00){
                contrestante=0.00
            }
            if(contrestante2>=1.00){
                contrestante2=0.00
            }
            totalD=(contrestante+contrestante2+contfilasTotal)*5
            val totalDS= formatToTwoDecimals(totalD)
            totalD =totalDS.toDouble()
            iri = 0.593+(0.0471*totalD)
            val iriS= formatToTwoDecimals(iri)
            iri =iriS.toDouble()
            calculateResult()
            val psiS= formatToTwoDecimals(psi)
            psi =psiS.toDouble()
            Textrestante1.text = "$contrestante"
            Textrestante2.text = "$contrestante2"
            Texttotalfilas.text = "$contfilasTotal"
            TextD.text = "$totalD"
            TextIRI.text = "$iri"
            TextPSI.text = "$psi"
        }
            }
    }
    private fun discoverDevices() {
        if (bluetoothAdapter.isEnabled) {


            val esp32Address = "CC:50:E3:9C:2E:1A" // Reemplaza con la dirección Bluetooth de tu ESP32

            // Obtener el dispositivo Bluetooth usando la dirección
            val esp32Device: BluetoothDevice? = bluetoothAdapter!!.getRemoteDevice(esp32Address)

            // Establecer una conexión BluetoothSocket
            try {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }

                // Log.d(TAG, "Conexión establecida con el dispositivo ESP32")
                Toast.makeText(
                    this,
                    " Conexión establecida con el dispositivo ESP32",
                    Toast.LENGTH_SHORT
                ).show()
                // Obtener el flujo de entrada para recibir datos


                // Implementar la lógica para leer datos del flujo de entrada según sea necesario
                // ...
                val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
                if (pairedDevices.isNotEmpty()) {
                    val device: BluetoothDevice = pairedDevices.iterator().next()
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)

                    Thread {
                        try {
                            bluetoothSocket = esp32Device?.createRfcommSocketToServiceRecord(MY_UUID)
                            bluetoothSocket?.connect()
                            inputStream = bluetoothSocket!!.inputStream
                            val buffer = ByteArray(1024)
                            var bytes: Int

                            while (true) {
                                bytes = try {
                                    inputStream.read(buffer)

                                } catch (e: IOException) {
                                    break
                                }

                                val readMessage = String(buffer, 0, bytes)
                                val intValue = readMessage.toInt()
                                handler.post {
                                    // Actualizar la ListView con los datos de temperatura (parsear readMessage)
                                    //arrayAdapter.add(readMessage)


                                    Textdato.text = intValue.toString()


                                    handleCellClick2(intValue)


                                }


                            }
                        } catch (e: IOException) {

                        }
                    }.start()
                }


            } catch (e: IOException) {
                //  Log.e(TAG, "Error al establecer la conexión Bluetooth: ${e.message}")
                Toast.makeText(this, " Error al establecer la conexión Bluetooth: ${e.message}", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(this, "Habilita el Bluetooth para descubrir dispositivos", Toast.LENGTH_SHORT).show()
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action


        }
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            // Cerrar la conexión Bluetooth al destruir la actividad
            inputStream?.close()
            bluetoothSocket?.close()
        } catch (e: IOException) {
            //Log.e(TAG, "Error al cerrar la conexión Bluetooth: ${e.message}")
            Toast.makeText(this, "Error al cerrar la conexión Bluetooth: ${e.message}", Toast.LENGTH_SHORT).show()

        }
    }
    private fun createTable() {
        for (i in 0 until rows) {
            val row = TableRow(this)


            for (j in 0..columns) {
                val textView = TextView(this)
                if(i==0 && j==0){
                    textView.width = dpToPx(50)
                    textView.height = dpToPx(50)
                    textView.setBackgroundResource(android.R.color.darker_gray)
                    textView.text = "Cont:"
                    textView.setPadding(4, 4, 4, 4)
                }
                if(i==0 && j==1){
                    textView.width = dpToPx(50)
                    textView.height = dpToPx(50)
                    textView.setBackgroundResource(android.R.color.darker_gray)
                    textView.text = contaa.toString()
                    textView.setPadding(4, 4, 4, 4)
                }
                if (j == 0 && i>0) {
                    // Columna 0 y Columna 1 sin bordes
                    textView.width = dpToPx(50)
                    textView.height = dpToPx(20)
                    textView.setBackgroundResource(android.R.color.darker_gray)
                    textView.text = "$i"
                    textView.setPadding(4, 4, 4, 4)

                }
                if (j == 1 && i>0) {
                    // Columna 0 y Columna 1 sin bordes
                    textView.width = dpToPx(50)
                    textView.height = dpToPx(20)
                    textView.setBackgroundResource(android.R.color.darker_gray)
                    textView.text = "0"
                    textView.setPadding(4, 4, 4, 4)

                }
                if (j > 1 && i>0) {
                    // Resto de las columnas con bordes delgados
                    textView.width = dpToPx(20)
                    textView.height = dpToPx(20)
                    textView.setBackgroundResource(android.R.color.darker_gray)
                    textView.text = ""
                    textView.setPadding(4, 0, 4, 0)

                }


                textView.setBackgroundColor(Color.LTGRAY)

                textView.gravity = Gravity.CENTER

                if (j != 0 && i>0) {
                    textView.setOnClickListener {
                        handleCellClick(it)
                    }
                }

                row.addView(textView)
            }

            tableLayout.addView(row)
        }
    }



    private fun handleCellClick2(view: Int) {
        if(view==-2) {
            mediaPlayerTry.start()
        }
        if(view==-1) {
            mediaPlayerBeep.start()
        }

        if(view==-3) {
            mediaPlayerCompl.start()
        }

        if(view>0) {


            val rowIndex = view

            if (selectedRow != -1) {
                // Clear previous selection
                clearSelection(selectedRow)
            }



            markCell(rowIndex)
            actcontx(rowIndex)
            actcontfilas(rowIndex)
            selectedRow = rowIndex

        }

    }


    private fun handleCellClick(view: View) {

        val row = view.parent as TableRow
        val rowIndex = tableLayout.indexOfChild(row)

        if (selectedRow != -1) {
            // Clear previous selection
            clearSelection(selectedRow)
        }

        markCell(rowIndex)
        actcontx(rowIndex)
        actcontfilas(rowIndex)
        selectedRow = rowIndex
    }





    private fun markCell(rowIndex: Int) {
        val row = tableLayout.getChildAt(rowIndex) as TableRow
        val cell = row.getChildAt(0) as TextView
        val cellcX = (row.getChildAt(1) as TextView).text.toString().toInt()

        for (i in 2 until row.childCount) {
            val cellValue = (row.getChildAt(i) as TextView).text.toString()

            if (cellValue == ""&&contaa <200) {
                (row.getChildAt(i) as TextView).text = "x"
                (row.getChildAt(i) as TextView).setTextColor(Color.RED)
                contX= cellcX+1
                if(i<=2){
                    contY++
                }

                contaa += 1

                break
            }

        }

        cell.setBackgroundColor(Color.WHITE)
    }








    private fun clearSelection(rowIndex: Int) {
        val row = tableLayout.getChildAt(rowIndex) as TableRow
        val cell = row.getChildAt(0) as TextView

        cell.setBackgroundColor(Color.LTGRAY)
    }





    private fun actcontx(rowIndex: Int) {
        val row = tableLayout.getChildAt(rowIndex) as TableRow
        val cellcX = (row.getChildAt(1) as TextView).text.toString().toInt()


        if(cellcX<20 ){
            (row.getChildAt(1) as TextView).text = contX.toString()
        }


    }





    private fun actcontfilas(rowIndex: Int) {
        val row = tableLayout.getChildAt(0) as TableRow
        val cell = row.getChildAt(0) as TextView
        (row.getChildAt(1) as TextView).text = contaa.toString()
        (row.getChildAt(1) as TextView).setTextColor(Color.RED)
    }




    private fun limpiar() {
        contX = 0
        contY = 0
        contaa = 0
        for (i in 0 until rows) {
            val row = tableLayout.getChildAt(i) as TableRow


            for (j in 0..columns) {
                val textView = row.getChildAt(j) as TextView
                if (j == 0 && i>0) {


                    textView.setBackgroundColor(Color.LTGRAY)

                }
                if (j == 1 && i>0) {

                    textView.text = contX.toString()


                }
                if(i==0 && j==1){

                    textView.text = contaa.toString()

                }

                if (j > 1 && i > 0) {



                    textView.text = ""

                }

            }

        }
        Textrestante1.text = ""
        Textrestante2.text = ""
        Texttotalfilas.text = ""
        TextD.text = ""
        TextIRI.text = ""
        TextPSI.text = ""
        Toast.makeText(this, "Nuevo Registro", Toast.LENGTH_SHORT).show()

    }

    fun onGuardarClick() {

        val restante1 = Textrestante1.text.toString()
        val restante2 = Textrestante2.text.toString()
        val cfilasTotal = Texttotalfilas.text.toString()
        val totalD = TextD.text.toString()
        val IRI = TextIRI.text.toString()
        val PSI = TextPSI.text.toString()
        // Aquí puedes obtener los datos de la tabla y pasarlos a la siguiente actividad
        val intent = Intent(this, CuartaActivity()::class.java)
        intent.putExtra("restante1", restante1)
        intent.putExtra("restante2", restante2)
        intent.putExtra("totalfilas", cfilasTotal)
        intent.putExtra("totalDD", totalD)
        intent.putExtra("IRI", IRI)
        intent.putExtra("PSI", PSI)

        startActivity(intent)
    }
    fun onAnalizarClick() {

        for (i in 0 until rows) {
            val row = tableLayout.getChildAt(i) as TableRow




            for (j in 2 until columns) {
                val textView = row.getChildAt(j) as TextView
                val cellValue = textView.text.toString()


                if ( i > 0 && cellValue == "x" && contAnalizar < 10) {





                    textView.text = "-"
                    textView.setTextColor(Color.GREEN)
                    contAnalizar++
//                        if(i<=2){
//                            contY++
//                        }
//
//                        contaa += 1
//                        TextVariable1.text = "variable1: $contaa"
//                        TextVariable2.text = "variable1: $contY"





                }

            }

        }



    }
    fun onAnalizar2Click() {


        for (i in rows-1 downTo 1) {
            val row = tableLayout.getChildAt(i) as TableRow




            for (j in 2 until columns) {
                val textView = row.getChildAt(j) as TextView
                val cellValue = textView.text.toString()


                if ( i > 0 && cellValue == "x" && contAnalizar2 < 10) {





                    textView.text = "-"
                    textView.setTextColor(Color.CYAN)
                    contAnalizar2++
//                        if(i<=2){
//                            contY++
//                        }
//
//                        contaa += 1
//                        TextVariable1.text = "variable1: $contaa"
//                        TextVariable2.text = "variable1: $contY"





                }

            }

        }


    }
    fun onAnalizar3Click() {

        for (i in 0 until rows) {
            val row = tableLayout.getChildAt(i) as TableRow




            for (j in 1 until columns) {
                val textView = row.getChildAt(j) as TextView
                val cellValue = textView.text.toString()

                if (i >1&&j ==2&& cellValue == "x") {
                    textView.text = "x"
                    textView.setTextColor(Color.BLACK)
                    contfilasTotal++

                }

                if ( i >1&& cellValue == "x") {





                    textView.text = "x"
                    textView.setTextColor(Color.BLUE)

                }

            }

        }

    }


    fun onAnalizar4Click() {

        for (i in 0 until rows) {
            val row = tableLayout.getChildAt(i) as TableRow




            for (j in 1 until columns) {
                val textView = row.getChildAt(j) as TextView
                val cellValue = textView.text.toString()
                if(j == 1&&i>0){
                    contXF = cellValue.toInt()
                }


                if (j>1&&i > 1 && cellValue == "x") {

                    contfilas++
                }

            }

            if(contfilas>0&&contrrr==0){
                contrestante = contfilas.toDouble() / contXF.toDouble()
                contrrr=1
            }
            contfilas=0

        }





    }
    fun onAnalizar5Click() {

        for (i in rows-1 downTo 1) {
            val row = tableLayout.getChildAt(i) as TableRow




            for (j in 1 until columns) {
                val textView = row.getChildAt(j) as TextView
                val cellValue = textView.text.toString()
                if(j == 1&&i>0){
                    contXF = cellValue.toInt()
                }


                if (j>1&&i > 1 && cellValue == "x") {

                    contfilas++
                }

            }

            if(contfilas>0&&contrrr==0) {
                contrestante2 = contfilas.toDouble() / contXF.toDouble()
                contrrr=1
            }

            contfilas=0
        }






    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
    private fun calculateResult() {
        val variable: Double = iri
        val result: Double = 5 * exp(-(variable/5.5)) // e^1
        val formattedNumber = formatToTwoDecimals(result)
        psi = formattedNumber.toDouble()

    }
    private fun formatToTwoDecimals(number: Double): String {
        val decimalFormat = DecimalFormat("#.####")

        return decimalFormat.format(number)
    }



}