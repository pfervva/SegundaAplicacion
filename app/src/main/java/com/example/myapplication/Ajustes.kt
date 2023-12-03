package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Ajustes : AppCompatActivity() {

    private lateinit var editTextNumeroTiradas: EditText
    private lateinit var btnSaveConfiguracion: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        editTextNumeroTiradas = findViewById(R.id.editTextNumeroTiradas)
        btnSaveConfiguracion = findViewById(R.id.btnGuardarConfiguracion)

        sharedPreferences = getSharedPreferences("configuracion", MODE_PRIVATE)

        // Cargar configuración actual y mostrarla en el EditText
        val numeroTiradas = sharedPreferences.getInt("numeroTiradas", 5) // Valor por defecto: 5
        editTextNumeroTiradas.setText(numeroTiradas.toString())

        btnSaveConfiguracion.setOnClickListener {
            // Guardar la configuración en SharedPreferences al hacer clic en el botón
            val nuevoNumeroTiradas = editTextNumeroTiradas.text.toString().toIntOrNull() ?: 5
            guardarConfiguracion(nuevoNumeroTiradas)
        }
    }

    private fun guardarConfiguracion(numeroTiradas: Int) {
        // Usar un editor para modificar SharedPreferences
        val editor = sharedPreferences.edit()

        // Guardar el nuevo número de tiradas
        editor.putInt("numeroTiradas", numeroTiradas)

        // Aplicar los cambios
        editor.apply()

        // Puedes mostrar un mensaje o realizar otras acciones después de guardar la configuración
    }
}