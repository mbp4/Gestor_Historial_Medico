package com.example.gestor_historial_medico

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class Paciente2Activity: AppCompatActivity(){
    private lateinit var spinner: Spinner
    private lateinit var txtDato: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnVolver: Button
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente2)

        spinner = findViewById(R.id.spinner)
        txtDato = findViewById(R.id.txt_Datos)
        btnGuardar = findViewById(R.id.btn_Guardar)
        btnVolver = findViewById(R.id.btn_Volver)

        val opciones = listOf("ALERGIAS", "APELLIDOS", "NOMBRE", "EDAD", "ID", "MEDICAMENTOS", "OPERACIONES")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinner.adapter = adapter

        btnGuardar.setOnClickListener {
            guardarEditado()
            finish()
        }

        btnVolver.setOnClickListener {
            finish()
        }


    }

    private fun guardarEditado() {
        val dato = txtDato.text.toString()
        val opcionSeleccionada = spinner.selectedItem.toString()

        when (opcionSeleccionada) {
            "ALERGIAS" -> {

                Toast.makeText(this, "Alergias actualizadas", Toast.LENGTH_SHORT).show()
            }
            "APELLIDOS" -> {

                Toast.makeText(this, "Apellidos actualizados", Toast.LENGTH_SHORT).show()
            }
            "NOMBRE" -> {

                Toast.makeText(this, "Nombre actualizado", Toast.LENGTH_SHORT).show()
            }
            "EDAD" -> {

                Toast.makeText(this, "Edad actualizada", Toast.LENGTH_SHORT).show()
            }
            "ID" -> {

                Toast.makeText(this, "ID actualizado", Toast.LENGTH_SHORT).show()
            }
            "MEDICAMENTOS" -> {

                Toast.makeText(this, "Medicamentos actualizados", Toast.LENGTH_SHORT).show()
            }
            "OPERACIONES" -> {

                Toast.makeText(this, "Operaciones actualizadas", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Campo no reconocido", Toast.LENGTH_SHORT).show()
            }
        }

        if (dato.isEmpty()){
            Toast.makeText(this, "No puede ingresar un dato en blanco", Toast.LENGTH_SHORT).show()
        }
    }
}