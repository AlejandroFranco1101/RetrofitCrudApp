package com.example.retrofitcrudapp

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://66240d4504457d4aaf9b8530.mockapi.io/api/"

    private val gson by lazy {
        val intDeserializer = JsonDeserializer { json, _, _ ->
            try {
                json.asInt
            } catch (e: Exception) {
                // Extrae dígitos si el registro en MockAPI contiene texto como "edad 1"
                Regex("\\d+").find(json.asString)?.value?.toIntOrNull() ?: 0
            }
        }
        GsonBuilder()
            .registerTypeAdapter(Int::class.java, intDeserializer)
            .registerTypeAdapter(Int::class.javaObjectType, intDeserializer)
            .create()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val instance: AlumnoApi by lazy {
        retrofit.create(AlumnoApi::class.java)
    }

    val profesorInstance: ProfesorApi by lazy {
        retrofit.create(ProfesorApi::class.java)
    }
}
