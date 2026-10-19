package com.example.retrofitcrudapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfesorActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfesorAdapter
    private lateinit var api: ProfesorApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profesor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fabAgregar: FloatingActionButton = findViewById(R.id.fab_agregar_profesor)
        val btnIrAlumnos: Button = findViewById(R.id.btnIrAlumnos)

        recyclerView = findViewById(R.id.recyclerViewProfesor)
        recyclerView.layoutManager = LinearLayoutManager(this)

        api = RetrofitClient.profesorInstance

        cargarDatos(api)

        fabAgregar.setOnClickListener {
            val i = Intent(baseContext, CrearProfesorActivity::class.java)
            startActivity(i)
        }

        btnIrAlumnos.setOnClickListener {
            val i = Intent(baseContext, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatos(api)
    }

    private fun cargarDatos(api: ProfesorApi) {
        val call = api.obtenerProfesores()
        call.enqueue(object : Callback<List<Profesor>> {
            override fun onResponse(call: Call<List<Profesor>>, response: Response<List<Profesor>>) {
                if (response.isSuccessful) {
                    val profesores = response.body()
                    if (profesores != null) {
                        adapter = ProfesorAdapter(profesores)
                        recyclerView.adapter = adapter

                        adapter.setOnItemClickListener(object : ProfesorAdapter.OnItemClickListener {
                            override fun onItemClick(profesor: Profesor) {
                                val opciones = arrayOf("Modificar Profesor", "Eliminar Profesor")

                                AlertDialog.Builder(this@ProfesorActivity)
                                    .setTitle(profesor.nombre)
                                    .setItems(opciones) { _, index ->
                                        when (index) {
                                            0 -> modificarProfesor(profesor)
                                            1 -> eliminarProfesor(profesor, api)
                                        }
                                    }
                                    .setNegativeButton("Cancelar", null)
                                    .show()
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener los profesores: $error")
                    Toast.makeText(
                        this@ProfesorActivity,
                        "Error al obtener los profesores 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Profesor>>, t: Throwable) {
                Log.e("API", "Error al obtener los profesores: ${t.message}")
                Toast.makeText(
                    this@ProfesorActivity,
                    "Error al obtener los profesores 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun modificarProfesor(profesor: Profesor) {
        val i = Intent(baseContext, ActualizarProfesorActivity::class.java)
        i.putExtra("profesor_id", profesor.id)
        i.putExtra("nombre", profesor.nombre)
        i.putExtra("apellido", profesor.apellido)
        i.putExtra("edad", profesor.edad)
        startActivity(i)
    }

    private fun eliminarProfesor(profesor: Profesor, api: ProfesorApi) {
        Log.e("API", "id : $profesor")
        val llamada = api.eliminarProfesor(profesor.id)
        llamada.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfesorActivity, "Profesor eliminado", Toast.LENGTH_SHORT).show()
                    cargarDatos(api)
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al eliminar profesor : $error")
                    Toast.makeText(
                        this@ProfesorActivity,
                        "Error al eliminar profesor 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error al eliminar profesor : $t")
                Toast.makeText(
                    this@ProfesorActivity,
                    "Error al eliminar profesor 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
