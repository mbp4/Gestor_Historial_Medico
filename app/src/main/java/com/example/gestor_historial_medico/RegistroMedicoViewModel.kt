package com.example.gestor_historial_medico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistroMedicoViewModel(private val repositorio: RegistroMedicoRepositorio) : ViewModel() {

    private val _registrosMedicos = MutableLiveData<List<RegistroMedico>>()
    val registrosMedicos: LiveData<List<RegistroMedico>> get() = _registrosMedicos

    fun agregarRegistroMedico(registro: RegistroMedico) {
        repositorio.agregarRegistro(registro)
        _registrosMedicos.value = repositorio.obtenerTodosLosRegistros()
    }

    fun actualizarRegistroMedico(registro: RegistroMedico) {
        repositorio.actualizarRegistro(registro)
        _registrosMedicos.value = repositorio.obtenerTodosLosRegistros()
    }

    fun cargarRegistrosMedicos() {
        _registrosMedicos.value = repositorio.obtenerTodosLosRegistros()
    }
}