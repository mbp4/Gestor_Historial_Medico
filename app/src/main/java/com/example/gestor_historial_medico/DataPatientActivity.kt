package com.example.gestor_historial_medico


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.firestore.FirebaseFirestore

class DataPatientActivity : ComponentActivity() {

    private lateinit var tvPatientInfo: TextView
    private lateinit var btnEditData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_patient)

        tvPatientInfo = findViewById(R.id.tv_patient_info)
        btnEditData = findViewById(R.id.btn_edit_data)

        val userId = intent.getStringExtra("USER_ID") ?: ""
        loadPatientData("Prueba")

        btnEditData.setOnClickListener {
            /*
            val intent = Intent(this, EditDataActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
             */
        }
    }

    private fun loadPatientData(userId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Pacientes").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val patient = document.toObject(Patient::class.java)
                    if (patient != null) {
                        val patientInfo = """
                        ID: ${patient.idPaciente}
                        Nombre: ${patient.nombre} ${patient.apellido}
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
}
