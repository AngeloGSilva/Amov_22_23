package pt.isec.a2019133504.amov_22_23

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isec.a2019133504.amov_22_23.Data.Cell
import pt.isec.a2019133504.amov_22_23.Data.Level
import pt.isec.a2019133504.amov_22_23.Data.MathGame
import pt.isec.a2019133504.amov_22_23.View.BoardView
import pt.isec.a2019133504.amov_22_23.View.MyViewModel
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding


class Mode1Activity : AppCompatActivity(), BoardView.OnTouchListener {

    private val viewModel : MyViewModel by viewModels()

    private var paused : Long = 0

    private lateinit var binding: ActivityMode1Binding
    //private var cells =viewModel.mathGame.getContent()
    private lateinit var timerObject : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boardGame.registerListener(this)

        binding.imageView.setImageURI(ProfileActivity.imgdata)

        binding.boardGame.updateCells(viewModel.mathGame.board.cells)

        viewModel.mathGame.cellsLiveData.observe(this) { updateCells(it) }
        viewModel.mathGame.maioresValores.observe(this) { updateValores(it) }
        viewModel.mathGame.vitoriaLiveData.observe(this) { updateVitoria(it) }
        viewModel.mathGame.pontosLiveData.observe(this) { updatePontos(it) }

        startTimer(MathGame.level.maxTime)
    }

    private fun startTimer(totalTime : Int){
        timerObject = object : CountDownTimer((totalTime*1000).toLong(), 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                binding.viewTimer.text = "seconds remaining: " + millisUntilFinished / 1000
                paused = millisUntilFinished / 1000
            }
            override fun onFinish() {
                viewModel.mathGame.resetBoardAtributos()
                binding.viewTimer.text = "Done!"
                binding.boardGame.visibility = View.INVISIBLE
            }
        }.start()
    }


    private fun updateVitoria(estado : Boolean){
        if (estado) {
            viewModel.mathGame.resetBoardAtributos()
            timerObject.cancel()
            if (paused + MathGame.level.winTime < MathGame.level.maxTime)
                startTimer(paused.toInt() + MathGame.level.winTime)
            else
                startTimer(MathGame.level.maxTime)

            //runBlocking { launch { delay(5000) } }
        }
    }

    private fun updateValores(valores: Pair<Int, Int>){
        binding.InfoPontos.text = "Linha: " + (valores.first).toString()+ " Coluna: " +(valores.second).toString()
    }

    private fun updateCells(cells: Array<Array<Cell>>?) = cells?.let {
        binding.boardGame.updateCells(cells)
    }

    private fun updatePontos(pontos : Int){
        binding.InfoP.text = pontos.toString()
    }


    override fun onCellTouched(row: Int, col: Int) {
        viewModel.mathGame.updateSelectedCell(row, col)
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


