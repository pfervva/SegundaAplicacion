package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Ajustes : AppCompatActivity() {

    private lateinit var editTextNombreJugador: EditText
    private lateinit var editTextNumeroTiradas: EditText
    private lateinit var btnSaveConfiguracion: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        editTextNombreJugador = findViewById(R.id.editTextNombreJugador)
        editTextNumeroTiradas = findViewById(R.id.editTextNumeroTiradas)
        btnSaveConfiguracion = findViewById(R.id.btnGuardarConfiguracion)

        sharedPreferences = getSharedPreferences("configuracion", MODE_PRIVATE)

        // Cargar configuración actual y mostrarla en los EditText
        val nombreJugador = sharedPreferences.getString("nombreJugador", "") ?: ""
        val numeroTiradas = sharedPreferences.getInt("numeroTiradas", 5)
        editTextNombreJugador.setText(nombreJugador)
        editTextNumeroTiradas.setText(numeroTiradas.toString())

        btnSaveConfiguracion.setOnClickListener {
            // Guardar la configuración en SharedPreferences al hacer clic en el botón
            val nuevoNombreJugador = editTextNombreJugador.text.toString()
            val nuevoNumeroTiradas = editTextNumeroTiradas.text.toString().toIntOrNull() ?: 5
            guardarConfiguracion(nuevoNombreJugador, nuevoNumeroTiradas)
        }
    }

    private fun guardarConfiguracion(nombreJugador: String, numeroTiradas: Int) {
        // Usar un editor para modificar SharedPreferences
        val editor = sharedPreferences.edit()

        // Guardar el nuevo nombre de jugador y el número de tiradas
        editor.putString("nombreJugador", nombreJugador)
        editor.putInt("numeroTiradas", numeroTiradas)

        // Aplicar los cambios
        editor.apply()

        // Puedes mostrar un mensaje o realizar otras acciones después de guardar la configuración
    }
}
