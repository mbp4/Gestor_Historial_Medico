package com.example.gestor_historial_medico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.gestor_historial_medico.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MedicoActivity : AppCompatActivity() {

    private lateinit var pacienteSpinner: Spinner
    private lateinit var verDetalleButton: Button

    // Lista para almacenar los nombres de los pacientes y sus IDs
    private val listaNombresPacientes = mutableListOf<String>()
    private val listaIdsPacientes = mutableListOf<String>()

    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medico)

        pacienteSpinner = findViewById(R.id.pacienteSpinner)
        verDetalleButton = findViewById(R.id.verDetalleButton)

        // Cargar datos de pacientes desde Firebase
        cargarPacientes()

        // Configuración del botón "Ver Detalle"
        verDetalleButton.setOnClickListener {
            mostrarDetallesPaciente()
        }
    }

    // Cargar la lista de pacientes desde Firebase
    private fun cargarPacientes() {
        db.collection("Pacientes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombre = document.getString("nombre") ?: "Sin Nombre"
                    val idPaciente = document.getString("idPaciente") ?: ""

                    // Añadir nombre e id a las listas solo si el idPaciente no está vacío
                    if (idPaciente.isNotEmpty()) {
                        listaNombresPacientes.add(nombre)
                        listaIdsPacientes.add(idPaciente)
                    }
                }

                // Actualizar el Spinner con los nombres de los pacientes
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaNombresPacientes)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                pacienteSpinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("MedicoActivity", "Error al cargar pacientes", exception)
            }
    }

    // Mostrar los detalles del paciente seleccionado
    private fun mostrarDetallesPaciente() {
        val selectedIndex = pacienteSpinner.selectedItemPosition
        if (selectedIndex != -1) {
            val idPacienteSeleccionado = listaIdsPacientes[selectedIndex]

            // Verificar si el idPaciente es válido antes de continuar
            if (idPacienteSeleccionado.isNotEmpty()) {
                // Navegar a PacienteDetalleActivity pasando el ID del paciente
                val intent = Intent(this, PacienteDetalleActivity::class.java).apply {
                    putExtra("idPaciente", idPacienteSeleccionado)
                }
                startActivity(intent)
            } else {
                Log.e("MedicoActivity", "ID de paciente seleccionado está vacío")
            }
        }
    }
}
