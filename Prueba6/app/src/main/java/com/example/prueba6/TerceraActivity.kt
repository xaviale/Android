package com.example.prueba6

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import kotlin.math.exp



class TerceraActivity : AppCompatActivity() {
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
    private lateinit var btnEjemplo: Button
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
    @SuppressLint("MissingInflatedId")




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tercera)
        btnGuardar = findViewById(R.id.btnGuardar)
         btnBorrar = findViewById(R.id.btnBorrar)
        btnAnalizar = findViewById(R.id.btnAnalizar)
        btnEjemplo = findViewById(R.id.btnEjemplo)
        //btnAnalizar.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
       // btnGuardar.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
        //btnBorrar.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))

        Textrestante1 = findViewById(R.id.Textrestante1)
        Textrestante2 = findViewById(R.id.Textrestante2)
        Texttotalfilas = findViewById(R.id.Texttotalfilas)
        TextD = findViewById(R.id.TextD)
        TextIRI = findViewById(R.id.TextIRI)
        TextPSI = findViewById(R.id.TextPSI)
        tableLayout = findViewById(R.id.tableLayout)

        createTable()
        btnGuardar.setOnClickListener {

            onGuardarClick()
            Toast.makeText(this, "Guardar Registro", Toast.LENGTH_SHORT).show()

        }

        btnBorrar.setOnClickListener {

            limpiar()
        }
        btnEjemplo.setOnClickListener {

            ejemplo()
        }
        btnAnalizar.setOnClickListener {
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


    private fun ejemplo() {
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

                if (j == 1 && i==1) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==1) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==2) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==2) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==3) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==3) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==4) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==4) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==5) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==5) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==6) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==6) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==7) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==7) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==8) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==8) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==9) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==9) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==10) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==10) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==11) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==11) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==12) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==12) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==13) {
                    contX=2
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==13) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==14) {
                    contX=1
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==14) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==15) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==15) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==16) {
                    contX=2
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==16) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==17) {
                    contX=4
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==17) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==18) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==18) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==19) {
                    contX=5
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==19) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==20) {
                    contX=3
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==20) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==21) {
                    contX=5
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==21) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==22) {
                    contX=7
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==22) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==23) {
                    contX=15
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==23) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==24) {
                    contX=15
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==24) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==25) {
                    contX=11
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==25) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==26) {
                    contX=19
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==26) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==27) {
                    contX=15
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==27) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==28) {
                    contX=17
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==28) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==29) {
                    contX=17
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==29) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==30) {
                    contX=12
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==30) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==31) {
                    contX=9
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==31) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==32) {
                    contX=15
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==32) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==33) {
                    contX=7
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==33) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==34) {
                    contX=4
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==34) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==35) {
                    contX=3
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==35) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==36) {
                    contX=3
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==36) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==37) {
                    contX=4
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==37) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==38) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==38) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==39) {
                    contX=2
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==39) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==40) {
                    contX=2
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==40) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==41) {
                    contX=1
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==41) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==42) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==42) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==43) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==43) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==44) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==44) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==45) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==45) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==46) {
                    contX=1
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==46) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==47) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==47) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==48) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==48) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==49) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==49) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }

                if (j == 1 && i==50) {
                    contX=0
                    textView.text = contX.toString()
                }
                if (j > 1 && i ==50) {
                    for ( ccc in j until contX +2 ) {
                        textView.text = "x"
                    }
                }


                if(i==0 && j==1){
                    contaa = 200
                    textView.text = contaa.toString()

                }



            }

        }
        Textrestante1.text = ""
        Textrestante2.text = ""
        Texttotalfilas.text = ""
        TextD.text = ""
        TextIRI.text = ""
        TextPSI.text = ""
        Toast.makeText(this, "Ejemplo Registrado", Toast.LENGTH_SHORT).show()

    }
}