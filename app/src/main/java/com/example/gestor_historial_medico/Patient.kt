package com.example.gestor_historial_medico


data class Patient(
    val idPaciente: String ,
    val nombre: String,
    val apellido: String,
    val edad: Int = 0,
    val alergias: MutableList<String> = mutableListOf(),
    val enfermedades: MutableList<String> = mutableListOf(),
    val operaciones: MutableList<String> = mutableListOf()
)
{
    constructor() : this("", "", "", 0, mutableListOf(), mutableListOf(), mutableListOf())
}
