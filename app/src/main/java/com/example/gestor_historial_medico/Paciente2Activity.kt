package com.example.gestor_historial_medico

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
            confirmacion()
            finish()
        }

        btnVolver.setOnClickListener {
            finish()
        }


    }

    private fun confirmacion() {
        AlertDialog.Builder(this)
            .setTitle("Editar campo")
            .setMessage("¿Estás seguro de que quieres editar este campo?")
            .setPositiveButton("Sí") { _, _ ->
                // Si el usuario confirma, se llama a guardarEditado()
                guardarEditado()
            }
                .setNegativeButton("No", null)
                .show()

    }

    private fun guardarEditado() {
        val dato = txtDato.text.toString()
        val opcionSeleccionada = spinner.selectedItem.toString()

        val paciente = intent.getStringExtra("idPaciente")
        if (paciente.isNullOrEmpty()) {
            Toast.makeText(this, "El paciente no se ha encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val campoEditar = when (opcionSeleccionada) {
            "ALERGIAS" -> "alergias"
            "APELLIDOS" -> "apellidos"
            "NOMBRE" -> "nombre"
            "EDAD" -> "edad"
            "ID" -> "idPaciente"
            "MEDICAMENTOS" -> "medicamentos"
            "OPERACIONES" -> "operaciones"
            else -> null
        }

        //para editar los campos al tener listados en algunos no queremos que se borren los datos antiguos por lo que comprobamos el dato que es y dependiendo del dato se hara una operacion u otra
        if (campoEditar != null) {
            if (campoEditar == "alergias" || campoEditar == "medicamentos" || campoEditar == "operaciones") {
                db.collection("pacientes").document(paciente)
                    .update(campoEditar, com.google.firebase.firestore.FieldValue.arrayUnion(dato))
                    .addOnSuccessListener {
                        Toast.makeText(this, "$opcionSeleccionada actualizada correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                db.collection("pacientes").document(paciente)
                    .update(campoEditar, dato)
                    .addOnSuccessListener {
                        Toast.makeText(this, "$opcionSeleccionada actualizada correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            }

        } else {
            Toast.makeText(this, "Campo no reconocido", Toast.LENGTH_SHORT).show()
        }
    }
}