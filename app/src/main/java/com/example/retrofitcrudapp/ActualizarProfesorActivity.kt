package com.example.retrofitcrudapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActualizarProfesorActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var actualizarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_profesor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nombreEditText = findViewById(R.id.nombreProfesorEditText)
        apellidoEditText = findViewById(R.id.apellidoProfesorEditText)
        edadEditText = findViewById(R.id.edadProfesorEditText)
        actualizarButton = findViewById(R.id.actualizarProfesorButton)

        val api = RetrofitClient.profesorInstance

        val profesorId = intent.getIntExtra("profesor_id", -1)
        Log.e("API", "profesorId : $profesorId")

        val nombre = intent.getStringExtra("nombre").toString()
        val apellido = intent.getStringExtra("apellido").toString()
        val edad = intent.getIntExtra("edad", 1)

        nombreEditText.setText(nombre)
        apellidoEditText.setText(apellido)
        edadEditText.setText(edad.toString())

        actualizarButton.setOnClickListener {
            val profesorActualizado = Profesor(
                profesorId,
                nombreEditText.text.toString(),
                apellidoEditText.text.toString(),
                edadEditText.text.toString().toIntOrNull() ?: 0
            )

            val jsonProfesorActualizado = Gson().toJson(profesorActualizado)
            Log.d("API", "JSON enviado: $jsonProfesorActualizado")

            api.actualizarProfesor(profesorId, profesorActualizado).enqueue(object : Callback<Profesor> {
                override fun onResponse(call: Call<Profesor>, response: Response<Profesor>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(
                            this@ActualizarProfesorActivity,
                            "Profesor actualizado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent(baseContext, ProfesorActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        try {
                            val errorJson = response.errorBody()?.string()
                            val errorObj = JSONObject(errorJson ?: "")
                            val errorMessage = errorObj.getString("message")
                            Toast.makeText(
                                this@ActualizarProfesorActivity,
                                errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@ActualizarProfesorActivity,
                                "Error al actualizar el profesor",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("API", "Error al parsear el JSON: ${e.message}")
                        }
                    }
                }

                override fun onFailure(call: Call<Profesor>, t: Throwable) {
                    Log.e("API", "onFailure : $t")
                    Toast.makeText(
                        this@ActualizarProfesorActivity,
                        "Error al actualizar el profesor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
