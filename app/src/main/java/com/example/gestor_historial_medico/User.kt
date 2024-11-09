package com.example.gestor_historial_medico

class User(
    val usuario: String,
    val contrasena: String,
    val rol: String
) {
    constructor() : this("", "", "")
}