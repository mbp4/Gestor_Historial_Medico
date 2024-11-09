package com.example.gestor_historial_medico

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DataPatientActivity : ComponentActivity() {

    private lateinit var tvPatientInfo: TextView
    private lateinit var btnEditData: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_patient)

        tvPatientInfo = findViewById(R.id.tv_patient_info)
        btnEditData = findViewById(R.id.btn_edit_data)
        btnCerrarSesion = findViewById(R.id.btn_cerrar)

        val userId = intent.getStringExtra("USER_ID") ?: ""
        if (userId.isNotEmpty()) {
            loadPatientData(userId) // Cargar los datos del paciente con el userId
        } else {
            Toast.makeText(this, "No se encontró el ID de usuario", Toast.LENGTH_SHORT).show()
        }

        btnEditData.setOnClickListener {
            val intent = Intent(this, Paciente2Activity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí") { _, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        val userId = intent.getStringExtra("USER_ID") ?: ""
        if (userId.isNotEmpty()) {
            loadPatientData(userId) // Cargar los datos del paciente con el userId
        }
    }

    private fun loadPatientData(userId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Pacientes").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    try {
                        // Intentamos deserializar el objeto
                        val patient = document.toObject(Patient::class.java)
                        val nom = decrypt(patient?.nombre ?: "", "cifrado123456789")
                        val ape = decrypt(patient?.apellido ?: "", "cifrado123456789")

                        if (patient != null) {
                            val patientInfo = """
                        ID: ${patient.idPaciente}
                        Nombre: ${nom} ${ape}
                        Edad: ${patient.edad} años
                        Alergias: ${patient.alergias.joinToString(", ")}
                        Enfermedades: ${patient.enfermedades.joinToString(", ")}
                        Operaciones: ${patient.operaciones.joinToString(", ")}
                    """.trimIndent()
                            tvPatientInfo.text = patientInfo
                        } else {
                            Toast.makeText(
                                this,
                                "Error: No se pudo leer la información del paciente.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        // Maneja el caso en que el campo edad no se pueda convertir a Int
                        Toast.makeText(
                            this,
                            "Error al deserializar los datos del paciente: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "No se encontraron datos del paciente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al obtener datos: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun decrypt(encryptedText: String, secretKey: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val keySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val decrypted = cipher.doFinal(decodedBytes)
        return String(decrypted, Charsets.UTF_8)
    }
}
