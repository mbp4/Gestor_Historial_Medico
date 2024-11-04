package com.example.gestor_historial_medico

class RegistroMedicoRepositorioImpl : RegistroMedicoRepositorio {
    private val registros = mutableListOf<RegistroMedico>()

    override fun agregarRegistro(registro: RegistroMedico) {
        registros.add(registro)
    }

    override fun actualizarRegistro(registro: RegistroMedico) {
        val indice = registros.indexOfFirst { it.id == registro.id }
        if (indice != -1) {
            registros[indice] = registro
        }
    }

    override fun obtenerRegistroPorId(id: Int): RegistroMedico? {
        return registros.find { it.id == id }
    }

    override fun obtenerTodosLosRegistros(): List<RegistroMedico> {
        return registros.toList()
    }
}
