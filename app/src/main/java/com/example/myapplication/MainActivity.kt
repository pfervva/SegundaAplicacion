package com.example.myapplication
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonLlamada = findViewById<Button>(R.id.llamar)
        val botonWeb = findViewById<Button>(R.id.google)
        val botonAlarma = findViewById<Button>(R.id.alarma)
        val botonCaptura = findViewById<Button>(R.id.captura)
        imageView = findViewById(R.id.imageView2)

        botonLlamada.setOnClickListener {
            val llamadaIntent = Intent(this, LlamadaActivity::class.java)
            startActivity(llamadaIntent)
        }

        botonWeb.setOnClickListener {
            val url = "https://www.google.com"
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(webIntent)
        }

        botonAlarma.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val horaActual = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            val minutosActuales = calendar.get(java.util.Calendar.MINUTE)

            // Sumar 2 minutos
            val minutosAlarma = minutosActuales + 3
            val horaAlarma = horaActual + minutosAlarma / 60
            val minutosFinal = minutosAlarma % 60

            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_HOUR, horaAlarma)
                putExtra(AlarmClock.EXTRA_MINUTES, minutosFinal)
                putExtra(AlarmClock.EXTRA_SKIP_UI, true) // Omitir la interfaz de usuario de la aplicación de reloj
                putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma")
                putExtra(AlarmClock.EXTRA_VIBRATE, true)
                putExtra(AlarmClock.EXTRA_RINGTONE, "content://settings/system/alarm_alert")
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "La aplicación de reloj no está disponible en este dispositivo", Toast.LENGTH_LONG).show()
            }
        }


        botonCaptura.setOnClickListener {
            val captura = captureScreen()
            if (captura != null) {
                imageView.setImageBitmap(captura)
                Toast.makeText(this, "Captura pantalla hecha", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No he podido tomar la captura de pantalla", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun captureScreen(): Bitmap? {
        val vista = window.decorView.rootView
        vista.isDrawingCacheEnabled = true
        val captura = Bitmap.createBitmap(vista.drawingCache)
        vista.isDrawingCacheEnabled = false
        return captura
    }
}