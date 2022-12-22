package pt.isec.a2019133504.amov_22_23

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.MutableLiveData
import pt.isec.a2019133504.amov_22_23.Data.Cell
import pt.isec.a2019133504.amov_22_23.Data.SinglePlayer
import pt.isec.a2019133504.amov_22_23.View.BoardView
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding
import kotlin.math.sin


class Mode1Activity : AppCompatActivity(), BoardView.OnTouchListener {
    private val singlePlayer = SinglePlayer()
    private lateinit var binding: ActivityMode1Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boardGame.registerListener(this)

        binding.imageView.setImageURI(ProfileActivity.imgdata)

        binding.boardGame.updateCells(singlePlayer.returnboardcells())

        singlePlayer.cellsLiveData.observe(this) { updateCells(it) }
        singlePlayer.fimLiveData.observe(this) { updateFim(it) }
        singlePlayer.pontosLiveData.observe(this) { updatePontos(it) }
        singlePlayer.timerCount.observe(this){updateTimer(it)}
        singlePlayer.nextLevel.observe(this){updateLevel(it)}
    }

    private fun updateLevel(estado : Boolean){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta de Jogo")
        builder.setMessage("Jogo em pausa por 5 segundos!")
        builder.setCancelable(false)
        val caixaAlerta = builder.create()
        caixaAlerta.show()
        val handler = Handler()
        handler.postDelayed({
            caixaAlerta.cancel()
            singlePlayer.startTimer(singlePlayer.pause)
        }, 5000)
    }


    private fun updateFim(estado : Boolean){
        //TODO melhorar mensagem de fim e guardar pontos etc
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Fim Jogo")
        builder.setMessage(singlePlayer.pontos.toString())
        builder.setCancelable(false)
        val caixaAlerta = builder.create()
        caixaAlerta.show()
    }

    private fun updateValores(valores: Pair<Double, Double>){
        binding.InfoPontos.text = "Linha: " + (valores.first).toString()+ " Coluna: " +(valores.second).toString()
    }

    private fun updateCells(cells: Array<Array<Cell>>?) = cells?.let {
        binding.boardGame.updateCells(cells)
    }

    private fun updateTimer(timer : Long){
        binding.viewTimer.text = "seconds remaining: $timer"
    }

    private fun updatePontos(pontos : Int){
        binding.InfoP.text = pontos.toString()
    }


    override fun onCellTouched(row: Int, col: Int) {
        singlePlayer.updateSelectedCell(row, col)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta de Jogo")
        builder.setMessage("Se saires agora, o jogo nao será salvo!")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            finish()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }
        builder.show()
    }
}


