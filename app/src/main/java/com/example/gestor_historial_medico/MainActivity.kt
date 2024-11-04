package com.example.gestor_historial_medico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val repositorio = RegistroMedicoRepositorioImpl()

            val viewModel = RegistroMedicoViewModel(repositorio)

            PantallaRegistroMedico(viewModel)
        }
    }
}