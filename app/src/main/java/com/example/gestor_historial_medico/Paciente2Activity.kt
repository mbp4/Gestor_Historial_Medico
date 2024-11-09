package com.example.gestor_historial_medico

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Paciente2Activity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var txtDato: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnVolver: Button
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente2)

        // Inicializa los elementos de la interfaz
        spinner = findViewById(R.id.spinner)
        txtDato = findViewById(R.id.txt_Datos)
        btnGuardar = findViewById(R.id.btn_Guardar)
        btnVolver = findViewById(R.id.btn_Volver)

        // Configura el Spinner
        val opciones = listOf("ALERGIAS", "APELLIDOS", "NOMBRE", "EDAD", "MEDICAMENTOS", "OPERACIONES")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinner.adapter = adapter


        // Acción del botón "Guardar"
        btnGuardar.setOnClickListener {
            confirmacion()
        }

        // Acción del botón "Volver"
        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun confirmacion() {
        // Muestra una alerta para confirmar la acción
        AlertDialog.Builder(this)
            .setTitle("Editar campo")
            .setMessage("¿Estás seguro de que quieres editar este campo?")
            .setPositiveButton("Sí") { _, _ -> guardarEditado() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun guardarEditado() {
        val dato = txtDato.text.toString()
        val opcionSeleccionada = spinner.selectedItem.toString()

        // Obtiene el id del paciente
        val paciente = intent.getStringExtra("USER_ID")

        if (paciente.isNullOrEmpty()) {
            Toast.makeText(this, "El paciente no se ha encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Asocia la opción seleccionada con el campo de Firestore
        val campoEditar = when (opcionSeleccionada) {
            "ALERGIAS" -> "alergias"
            "APELLIDOS" -> "apellido"
            "NOMBRE" -> "nombre"
            "EDAD" -> "edad"
            "MEDICAMENTOS" -> "medicamentos"
            "OPERACIONES" -> "operaciones"
            else -> null
        }

        // Si el campo es nulo, muestra un mensaje de error
        if (campoEditar == null) {
            Toast.makeText(this, "Campo no reconocido", Toast.LENGTH_SHORT).show()
            return
        }

        // Si el campo seleccionado es uno de tipo lista, se añade un elemento a la lista
        if (campoEditar == "alergias" || campoEditar == "medicamentos" || campoEditar == "operaciones") {
            db.collection("Pacientes").document(paciente)
                .update(campoEditar, com.google.firebase.firestore.FieldValue.arrayUnion(dato))
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "$opcionSeleccionada actualizada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }

        } else if (campoEditar == "edad") {
            // Si es el campo edad, se convierte a entero y se actualiza
            val edad = dato.toIntOrNull()
            if (edad != null) {
                db.collection("Pacientes").document(paciente)
                    .update(campoEditar, edad)
                    .addOnSuccessListener {
                        Toast.makeText(this, "$opcionSeleccionada actualizada correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Edad no válida", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si es un campo no lista, se actualiza directamente el valor
            db.collection("Pacientes").document(paciente)
                .update(campoEditar, dato)
                .addOnSuccessListener {
                    Toast.makeText(this, "$opcionSeleccionada actualizada correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
