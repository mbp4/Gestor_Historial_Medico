package com.example.gestor_historial_medico

interface SecureStorage {
    fun saveData(key: String, data: String)
    fun getData(key: String): String?
    fun deleteData(key: String)
}