package pt.isec.a2019133504.amov_22_23

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import pt.isec.a2019133504.amov_22_23.Data.Cell
import pt.isec.a2019133504.amov_22_23.Data.SinglePlayer
import pt.isec.a2019133504.amov_22_23.View.BoardView
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding


class Mode1Activity : AppCompatActivity(), BoardView.OnTouchListener {

    private val singlePlayer = SinglePlayer()

    private var paused : Long = 0

    private lateinit var binding: ActivityMode1Binding
    private lateinit var timerObject : CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boardGame.registerListener(this)

        binding.imageView.setImageURI(ProfileActivity.imgdata)

        binding.boardGame.updateCells(singlePlayer.returnboardcells())

        singlePlayer.cellsLiveData.observe(this) { updateCells(it) }
        singlePlayer.maioresValores.observe(this) { updateValores(it) }
        singlePlayer.vitoriaLiveData.observe(this) { updateVitoria(it) }
        singlePlayer.pontosLiveData.observe(this) { updatePontos(it) }
        singlePlayer.timerCount.observe(this){updateTimer(it)}

        //startTimer(MathGame.level.maxTime)
    }

/*
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

*/

    private fun updateVitoria(estado : Boolean){

/*        if (estado) {
            viewModel.mathGame.resetBoardAtributos()
            timerObject.cancel()
            if (paused + MathGame.level.winTime < MathGame.level.maxTime)
                startTimer(paused.toInt() + MathGame.level.winTime)
            else
                startTimer(MathGame.level.maxTime)

            //runBlocking { launch { delay(5000) } }
        }*/

    }

    private fun updateValores(valores: Pair<Int, Int>){
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


