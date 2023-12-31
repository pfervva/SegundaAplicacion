package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.AlarmClock
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var progressBarChiste: ProgressBar

    private var isSpeaking = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonLlamada = findViewById<Button>(R.id.llamar)
        val botonAjustes = findViewById<Button>(R.id.ajustes)
        val botonDados = findViewById<Button>(R.id.dados)
        val botonWeb = findViewById<Button>(R.id.google)
        val botonAlarma = findViewById<Button>(R.id.alarma)
        val botonCaptura = findViewById<Button>(R.id.captura)
        val botonChiste = findViewById<Button>(R.id.chiste)
        imageView = findViewById(R.id.imageView2)
        progressBarChiste = findViewById(R.id.progressBarChiste)

        configureTextToSpeech()

        botonLlamada.setOnClickListener {
            val llamadaIntent = Intent(this, LlamadaActivity::class.java)
            startActivity(llamadaIntent)
        }
        botonAjustes.setOnClickListener {
            val llamadaIntent = Intent(this, Ajustes::class.java)
            startActivity(llamadaIntent)
        }
        botonDados.setOnClickListener {
            val llamadaIntent = Intent(this, DadosActivity::class.java)
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

            val minutosAlarma = minutosActuales + 3
            val horaAlarma = horaActual + minutosAlarma / 60
            val minutosFinal = minutosAlarma % 60

            try {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_HOUR, horaAlarma)
                    putExtra(AlarmClock.EXTRA_MINUTES, minutosFinal)
                    putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                    putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma")
                    putExtra(AlarmClock.EXTRA_VIBRATE, true)
                    putExtra(AlarmClock.EXTRA_RINGTONE, "content://settings/system/alarm_alert")
                }
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al configurar la alarma", Toast.LENGTH_SHORT).show()
            }
        }
        botonCaptura.setOnClickListener {
            val captura = captureScreen()
            if (captura != null) {
                imageView.setImageBitmap(captura)
                Toast.makeText(this, "Captura pantalla hecha", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    this,
                    "No he podido tomar la captura de pantalla",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        botonChiste.setOnClickListener {
            if (!isSpeaking) {
                isSpeaking = true

                progressBarChiste.visibility = View.VISIBLE

                startProgressBarAnimation()

                val chistesArray = resources.getStringArray(R.array.chistes)
                val randomIndex = Random.nextInt(chistesArray.size)
                val chisteSeleccionado = chistesArray[randomIndex]
                speakMeDescription(chisteSeleccionado)

                GlobalScope.launch(Dispatchers.Main) {
                    delay(600)  // Ajusta el tiempo de reproducción del chiste
                    isSpeaking = false

                    stopProgressBarAnimation()

                    GlobalScope.launch(Dispatchers.Main) {
                        delay(6000)  // 6 segundos
                        progressBarChiste.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun configureTextToSpeech() {
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if (it != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.getDefault()
            }
        })
    }

    private fun speakMeDescription(s: String) {
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun captureScreen(): Bitmap? {
        val vista = window.decorView.rootView
        vista.isDrawingCacheEnabled = true
        val captura = Bitmap.createBitmap(vista.drawingCache)
        vista.isDrawingCacheEnabled = false
        return captura
    }

    private fun startProgressBarAnimation() {
        progressBarChiste.rotation = 0f
        val rotate = ObjectAnimator.ofFloat(progressBarChiste, "rotation", 360f)
        rotate.duration = 600
        rotate.interpolator = LinearInterpolator()
        rotate.start()
    }

    private fun stopProgressBarAnimation() {
        progressBarChiste.clearAnimation()
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}
