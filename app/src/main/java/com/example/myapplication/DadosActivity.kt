package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.myapplication.databinding.ActivityDadosBinding
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DadosActivity : AppCompatActivity() {

    private lateinit var bindingDados: ActivityDadosBinding
    private var sum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDados = ActivityDadosBinding.inflate(layoutInflater)
        setContentView(bindingDados.root)
        initEvent()
    }

    private fun initEvent() {
        bindingDados.txtResultado.visibility = View.INVISIBLE
        bindingDados.imageButton.setOnClickListener {
            bindingDados.txtResultado.visibility = View.VISIBLE
            game()
        }
    }

    private fun game() {
        scheduleRun()
    }

    private fun scheduleRun() {
        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val msc = 1000
        for (i in 1..5) {
            schedulerExecutor.schedule(
                {
                    throwDadoInTime()
                },
                msc * i.toLong(), TimeUnit.MILLISECONDS
            )
        }

        schedulerExecutor.schedule({
            viewResult()
        }, msc * 7.toLong(), TimeUnit.MILLISECONDS)

        schedulerExecutor.shutdown()
    }

    private fun throwDadoInTime() {
        val numDados = Array(3) { Random.nextInt(1, 6) }
        val imageViews: Array<ImageView> = arrayOf(
            bindingDados.imagviewDado1,
            bindingDados.imagviewDado2,
            bindingDados.imagviewDado3
        )

        sum = numDados.sum()
        for (i in 0 until 3) {
            selectView(imageViews[i], numDados[i])
        }
    }

    private fun selectView(imgV: ImageView, v: Int) {
        when (v) {
            1 -> imgV.setImageResource(R.drawable.dado1)
            2 -> imgV.setImageResource(R.drawable.dado2)
            3 -> imgV.setImageResource(R.drawable.dado3)
            4 -> imgV.setImageResource(R.drawable.dado4)
            5 -> imgV.setImageResource(R.drawable.dado5)
            6 -> imgV.setImageResource(R.drawable.dado6)
        }
    }

    private fun viewResult() {
        bindingDados.txtResultado.text = sum.toString()
        println(sum)
    }
}