package com.example.gestor_historial_medico

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class PacienteDetalleActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = Firebase.firestore
    private lateinit var nombreTextView: TextView
    private lateinit var apellidosTextView: TextView
    private lateinit var edadTextView: TextView
    private lateinit var alergiasSpinner: Spinner
    private lateinit var medicamentosSpinner: Spinner
    private lateinit var operacionesSpinner: Spinner
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente_detalle)

        nombreTextView = findViewById(R.id.nombreTextView)
        apellidosTextView = findViewById(R.id.apellidosTextView)
        edadTextView = findViewById(R.id.edadTextView)
        alergiasSpinner = findViewById(R.id.alergiasSpinner)
        medicamentosSpinner = findViewById(R.id.medicamentosSpinner)
        operacionesSpinner = findViewById(R.id.operacionesSpinner)
        btnVolver = findViewById(R.id.btn_volver2)

        val idPaciente = intent.getStringExtra("idPaciente")
        if (!idPaciente.isNullOrEmpty()) {
            cargarDetallesPaciente(idPaciente)
        } else {
            Log.e("PacienteDetalleActivity", "ID de paciente es nulo o vacío")
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    // Cargar los detalles de un paciente usando su ID
    private fun cargarDetallesPaciente(idPaciente: String) {
        db.collection("Pacientes").document(idPaciente)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val nom = document.getString("nombre").toString()
                    val ape = document.getString("apellido").toString()

                    nombreTextView.text = decrypt(nom, "cifrado123456789")
                    apellidosTextView.text = decrypt(ape, "cifrado123456789")
                    edadTextView.text = document.getLong("edad")?.toString()

                    // Configurar el adaptador para el Spinner de alergias
                    val alergias = document["alergias"] as? List<String> ?: emptyList()
                    val alergiasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, alergias)
                    alergiasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    alergiasSpinner.adapter = alergiasAdapter

                    // Configurar el adaptador para el Spinner de medicamentos
                    val medicamentos = document["medicamentos"] as? List<String> ?: emptyList()
                    val medicamentosAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicamentos)
                    medicamentosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    medicamentosSpinner.adapter = medicamentosAdapter

                    // Configurar el adaptador para el Spinner de operaciones
                    val operaciones = document["operaciones"] as? List<String> ?: emptyList()
                    val operacionesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, operaciones)
                    operacionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    operacionesSpinner.adapter = operacionesAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("PacienteDetalleActivity", "Error al cargar detalles del paciente", exception)
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
