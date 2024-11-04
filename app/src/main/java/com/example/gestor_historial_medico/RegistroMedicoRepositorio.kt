package com.example.gestor_historial_medico

interface RegistroMedicoRepositorio {
    fun agregarRegistro(registro: RegistroMedico)
    fun actualizarRegistro(registro: RegistroMedico)
    fun obtenerRegistroPorId(id: Int): RegistroMedico?
    fun obtenerTodosLosRegistros(): List<RegistroMedico>
}