package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class Ajustes : AppCompatActivity() {

    private lateinit var editTextNombreJugador: EditText
    private lateinit var editTextNumeroTiradas: EditText
    private lateinit var switchDragonBall: Switch
    private lateinit var radioButtonMostrarCarta: RadioButton
    private lateinit var btnSaveConfiguracion: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        editTextNombreJugador = findViewById(R.id.editTextNombreJugador)
        editTextNumeroTiradas = findViewById(R.id.editTextNumeroTiradas)
        switchDragonBall = findViewById(R.id.switchCartaONumero)
        radioButtonMostrarCarta = findViewById(R.id.radioButtonMostrarCarta)
        btnSaveConfiguracion = findViewById(R.id.btnGuardarConfiguracion)

        sharedPreferences = getSharedPreferences("configuracion", MODE_PRIVATE)

        // Cargar configuración actual y mostrarla en los EditText, Switch y RadioButton
        val nombreJugador = sharedPreferences.getString("nombreJugador", "") ?: ""
        val numeroTiradas = sharedPreferences.getInt("numeroTiradas", 5)
        val dragonBallActivado = sharedPreferences.getBoolean("dragonBallActivado", false)
        val mostrarSoloPares = sharedPreferences.getBoolean("mostrarSoloPares", false)

        editTextNombreJugador.setText(nombreJugador)
        editTextNumeroTiradas.setText(numeroTiradas.toString())
        switchDragonBall.isChecked = dragonBallActivado
        radioButtonMostrarCarta.isChecked = mostrarSoloPares

        radioButtonMostrarCarta.setOnCheckedChangeListener { _, isChecked ->
            // Aquí puedes aplicar la lógica para mostrar solo números pares
            if (isChecked) {
                // Si el botón está marcado, ajustar el valor de número de tiradas a un número par
                val numeroTiradas = editTextNumeroTiradas.text.toString().toIntOrNull() ?: 5
                if (numeroTiradas % 2 != 0) {
                    // Si el número de tiradas es impar, ajustarlo a un número par
                    editTextNumeroTiradas.setText((numeroTiradas + 1).toString())
                }
            }
        }

        btnSaveConfiguracion.setOnClickListener {
            // Guardar la configuración en SharedPreferences al hacer clic en el botón
            val nuevoNombreJugador = editTextNombreJugador.text.toString()
            val nuevoNumeroTiradas = editTextNumeroTiradas.text.toString().toIntOrNull() ?: 5
            val nuevoDragonBallActivado = switchDragonBall.isChecked
            val nuevoMostrarSoloPares = radioButtonMostrarCarta.isChecked

            guardarConfiguracion(
                nuevoNombreJugador,
                nuevoNumeroTiradas,
                nuevoDragonBallActivado,
                nuevoMostrarSoloPares
            )
        }
    }

    private fun guardarConfiguracion(
        nombreJugador: String,
        numeroTiradas: Int,
        dragonBallActivado: Boolean,
        mostrarSoloPares: Boolean
    ) {
        // Usar un editor para modificar SharedPreferences
        val editor = sharedPreferences.edit()

        // Guardar el nuevo nombre de jugador, el número de tiradas, la configuración de Dragon Ball y la opción de mostrar solo pares
        editor.putString("nombreJugador", nombreJugador)
        editor.putInt("numeroTiradas", numeroTiradas)
        editor.putBoolean("dragonBallActivado", dragonBallActivado)
        editor.putBoolean("mostrarSoloPares", mostrarSoloPares)

        // Aplicar los cambios
        editor.apply()

        // Puedes mostrar un mensaje o realizar otras acciones después de guardar la configuración
    }
}
