package com.example.gestor_historial_medico
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnNewUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnNewUser = findViewById(R.id.btnNewUser)

        btnLogin.setOnClickListener {
            loginUser()
        }

        btnNewUser.setOnClickListener {
            showNewUserDialog()
        }
    }

    private fun loginUser() {
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .whereEqualTo("usuario", username)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    } else {
                        val userDoc = documents.documents[0]
                        val storedHash = userDoc.getString("contrasena") ?: ""
                        val inputHash = hashPassword(password)

                        if (inputHash == storedHash) {
                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                            // Verificar si el usuario es paciente y obtener su ID
                            val role = userDoc.getString("rol")
                            if (role == "paciente") {
                                val userId = userDoc.id // El ID del documento es el userId
                                navigateToPatientActivity(userId)
                            } else if (role == "médico") {
                                // Si el usuario es médico, navegar a la actividad de médico
                                val intent = Intent(this, MedicoActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNewUserDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_new_user, null)
        val edtNewUsername = dialogView.findViewById<EditText>(R.id.edtNewUsername)
        val edtNewPassword = dialogView.findViewById<EditText>(R.id.edtNewPassword)
        val spinnerRole = dialogView.findViewById<Spinner>(R.id.spinnerRole)

        AlertDialog.Builder(this)
            .setTitle("Nuevo Usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val newUsername = edtNewUsername.text.toString().trim()
                val newPassword = edtNewPassword.text.toString().trim()
                val role = spinnerRole.selectedItem.toString()

                if (newUsername.isNotEmpty() && newPassword.isNotEmpty()) {
                    checkIfUserExists(newUsername, newPassword, role)
                } else {
                    Toast.makeText(this, "Datos incompletos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun checkIfUserExists(username: String, password: String, role: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .whereEqualTo("usuario", username)
            .get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                if (documents.isEmpty) {
                    val hashedPassword = hashPassword(password)
                    saveNewUser(username, hashedPassword, role)
                } else {
                    Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al verificar el usuario", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveNewUser(username: String, hashedPassword: String, role: String) {
        val user = User(username, hashedPassword, role)
        val db = FirebaseFirestore.getInstance()

        // Guardar el usuario en la colección "Users"
        db.collection("Users")
            .add(user)
            .addOnSuccessListener { userDocRef ->
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

                // Si el usuario es un paciente, crear el paciente en la colección "Pacientes"
                if (role == "paciente") {
                    val patient = Patient(
                        idPaciente = userDocRef.id,  // Usamos el ID del documento como idPaciente
                        nombre = "",                  // Deja estos campos vacíos o con valores por defecto
                        apellido = "",
                        edad = 0,
                        alergias = mutableListOf(),
                        enfermedades = mutableListOf(),
                        operaciones = mutableListOf()
                    )

                    // Guardar el paciente en la colección "Pacientes"
                    db.collection("Pacientes")
                        .document(userDocRef.id)  // Usamos el mismo ID de usuario como ID del paciente
                        .set(patient)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Paciente creado correctamente", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al crear el paciente", Toast.LENGTH_SHORT).show()
                        }
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
            }
    }

    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
    private fun navigateToPatientActivity(userId: String) {
        val intent = Intent(this, DataPatientActivity::class.java)
        intent.putExtra("USER_ID", userId) // Pasamos el userId como extra
        startActivity(intent)
        finish() // Finalizamos la actividad de login para que no se pueda regresar a ella
    }
}
