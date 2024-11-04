package com.example.gestor_historial_medico

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun PantallaRegistroMedico(viewModel: RegistroMedicoViewModel = viewModel(factory = ViewModelProvider.NewInstanceFactory())) {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var historialMedico by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )
        TextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = historialMedico,
            onValueChange = { historialMedico = it },
            label = { Text("Historial Médico") }
        )
        TextField(
            value = medicamentos,
            onValueChange = { medicamentos = it },
            label = { Text("Medicamentos") }
        )
        Button(onClick = {
            val registro = RegistroMedico(
                id = (1..1000).random(), //Simulación
                nombre = nombre,
                edad = edad.toIntOrNull() ?: 0,
                historialMedico = historialMedico,
                medicamentos = medicamentos
            )
            viewModel.agregarRegistroMedico(registro)
        }) {
            Text("Guardar Registro")
        }
    }
}
