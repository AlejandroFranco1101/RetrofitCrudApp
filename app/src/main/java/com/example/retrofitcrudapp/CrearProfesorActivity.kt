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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearProfesorActivity : AppCompatActivity() {
    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var crearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_profesor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nombreEditText = findViewById(R.id.editTextNombreProfesor)
        apellidoEditText = findViewById(R.id.editTextApellidoProfesor)
        edadEditText = findViewById(R.id.editTextEdadProfesor)
        crearButton = findViewById(R.id.btnGuardarProfesor)

        crearButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val edad = edadEditText.text.toString().toIntOrNull() ?: 0

            val profesor = Profesor(0, nombre, apellido, edad)
            val api = RetrofitClient.profesorInstance

            api.crearProfesor(profesor).enqueue(object : Callback<Profesor> {
                override fun onResponse(call: Call<Profesor>, response: Response<Profesor>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CrearProfesorActivity,
                            "Profesor creado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent(baseContext, ProfesorActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error crear profesor: $error")
                        Toast.makeText(
                            this@CrearProfesorActivity,
                            "Error al crear el profesor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Profesor>, t: Throwable) {
                    Toast.makeText(
                        this@CrearProfesorActivity,
                        "Error al crear el profesor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
