

package com.example.merlinv1

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class SextaActivity : AppCompatActivity() {
    private val handler: Handler = Handler()
    private val TAG = "ESP32"
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID para el perfil SPP
    private var bluetoothSocket: BluetoothSocket? = null
    private lateinit var inputStream: InputStream
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var devicesListView: ListView
    private lateinit var deviceList: ArrayList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var Textdato: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sexta)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )

        devicesListView = findViewById(R.id.deviceListView)
        deviceList = ArrayList()
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList)
        devicesListView.adapter = arrayAdapter
        Textdato = findViewById(R.id.Textdato)
        val discoverBtn: Button = findViewById(R.id.btnDiscover)
        discoverBtn.setOnClickListener {
            discoverDevices()
        }



        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no es compatible en este dispositivo", Toast.LENGTH_SHORT).show()
            finish()
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
                                handler.post {
                                    // Actualizar la ListView con los datos de temperatura (parsear readMessage)
                                    arrayAdapter.add(readMessage)
                                    Textdato.text = "$readMessage"
                                }
                            }
                        } catch (e: IOException) {
                            // Manejar errores de conexión Bluetooth
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
}