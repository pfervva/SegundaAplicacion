package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDadosBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DadosActivity : AppCompatActivity() {

    private lateinit var bindingDados: ActivityDadosBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var sum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDados = ActivityDadosBinding.inflate(layoutInflater)
        setContentView(bindingDados.root)
        initEvent()

        // Obtén la configuración almacenada en SharedPreferences
        sharedPreferences = getSharedPreferences("configuracion", MODE_PRIVATE)
    }

    private fun initEvent() {
        bindingDados.txtResultado.visibility = View.INVISIBLE
        bindingDados.imageButton.setOnClickListener {
            bindingDados.txtResultado.visibility = View.VISIBLE
            game()
        }
    }

    private fun game() {
        // Utiliza la configuración obtenida de SharedPreferences
        val nombreJugador = sharedPreferences.getString("nombreJugador", "") ?: ""
        val numeroTiradas = sharedPreferences.getInt("numeroTiradas", 5)
        val dragonBallActivado = sharedPreferences.getBoolean("dragonBallActivado", false)

        // Utiliza el nombre de jugador y el número de tiradas en lugar de valores fijos
        scheduleRun(nombreJugador, numeroTiradas, dragonBallActivado)
    }

    private fun scheduleRun(nombreJugador: String, numeroTiradas: Int, dragonBallActivado: Boolean) {
        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val msc = 1000
        for (i in 1..numeroTiradas) {
            schedulerExecutor.schedule(
                {
                    throwDadoInTime(dragonBallActivado)
                },
                msc * i.toLong(), TimeUnit.MILLISECONDS
            )
        }

        schedulerExecutor.schedule({
            viewResult(nombreJugador)
        }, msc * (numeroTiradas + 5).toLong(), TimeUnit.MILLISECONDS)

        schedulerExecutor.shutdown()
    }

    private fun throwDadoInTime(dragonBallActivado: Boolean) {
        val numDados = Array(3) { Random.nextInt(1, 7) }
        val imageViews: Array<ImageView> = arrayOf(
            bindingDados.imagviewDado1,
            bindingDados.imagviewDado2,
            bindingDados.imagviewDado3
        )

        sum = numDados.sum()
        for (i in 0 until 3) {
            selectView(imageViews[i], numDados[i], dragonBallActivado)
        }
    }

    private fun selectView(imgV: ImageView, imagenes: Int, dragonBallActivado: Boolean) {
        if (dragonBallActivado) {
            // Si Dragon Ball está activado, utiliza las imágenes de bolas de Dragon Ball
            when (imagenes) {
                1 -> imgV.setImageResource(R.drawable.dg1)
                2 -> imgV.setImageResource(R.drawable.dg2)
                3 -> imgV.setImageResource(R.drawable.dg3)
                4 -> imgV.setImageResource(R.drawable.dg4)
                5 -> imgV.setImageResource(R.drawable.dg5)
                6 -> imgV.setImageResource(R.drawable.dg1)
            }
        } else {
            // Si Dragon Ball no está activado, utiliza las imágenes de dados normales
            when (imagenes) {
                1 -> imgV.setImageResource(R.drawable.dado1)
                2 -> imgV.setImageResource(R.drawable.dado2)
                3 -> imgV.setImageResource(R.drawable.dado3)
                4 -> imgV.setImageResource(R.drawable.dado4)
                5 -> imgV.setImageResource(R.drawable.dado5)
                6 -> imgV.setImageResource(R.drawable.dado6)
            }
        }
    }

    private fun viewResult(nombreJugador: String) {
        // Calcula la suma de los dados y realiza las acciones necesarias
        val resultadoTextView: TextView = findViewById(R.id.txt_resultado)
        resultadoTextView.text = "$nombreJugador salió un $sum"
        println(sum)
    }
}