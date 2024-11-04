package com.example.gestor_historial_medico

data class RegistroMedico(
    val id: Int,
    var nombre: String,
    var edad: Int,
    var historialMedico: String,
    var medicamentos: String
)
