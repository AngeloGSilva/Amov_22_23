package pt.isec.a2019133504.amov_22_23.Data

import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random
import kotlin.random.nextInt


class MathGame {

    lateinit var boardsLevel : Array<Board>

    companion object{
        var level: Level = Level.level1
    }

    //para atualizar dados na view
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var maioresValores = MutableLiveData<Pair<Int,Int>>()
    var pontosLiveData = MutableLiveData<Int>()
    var cellsLiveData = MutableLiveData< Array<Array<Cell>>>()
    var vitoriaLiveData = MutableLiveData<Boolean>()

    lateinit var boards : Array<Board>

    private var selectedRow = -1
    private var selectedCol = -1


    private var vitoria = false

    private var pontos = 0

    private var numeroAcertos = 0

    var board: Board

    //TODO Init apenas para criar boards
    //TODO objeto singlePlayer onde sao criadas boards e passadas para o MathGame
    //TODO single player ter o timer no mathGame
    //TODO class multiplayer onde vai receber boards/set delas
    //TODO fling no single vai incaminhr para o mathgame no multiplayer envia em json para o server
    //TODO apagar a cena da vitoria

    private lateinit var textTimer : TextView
    //buscar a textView??

    private lateinit var timerObject : CountDownTimer
    private fun startTimer(totalTime : Int){
        timerObject = object : CountDownTimer((totalTime*1000).toLong(), 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                //binding.viewTimer.text = "seconds remaining: " + millisUntilFinished / 1000
                //TODO Usar livedata para atualizar a textview do counter
                //paused = millisUntilFinished / 1000
            }
            override fun onFinish() {
                resetBoardAtributos()
                //binding.viewTimer.text = "Done!"
                //boardGame.visibility = View.INVISIBLE
            }
        }.start()
    }



    init {

        //startTimer(30)
        //TODO ver quantas boards criar para level (5 por agora) e se pode ser assim
        boards = Array(5){Board(5, level)}

        level = Level.level1
        //board = Board(5, level)
        board = boards[0]
        cellsLiveData.postValue(board.cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        getMaiorCalculo()
        vitoriaLiveData.postValue(false)
        pontosLiveData.postValue(pontos)
        vitoria = false
    }

    fun resetBoardAtributos(){
        board = Board(5, level)
        //board = boards[++]
        cellsLiveData.postValue(board.cells)
        selectedRow = -1
        selectedCol = -1
        getMaiorCalculo()
        vitoriaLiveData.postValue(false)
        pontosLiveData.postValue(pontos)
        vitoria = false
    }




    fun updateSelectedCell(row: Int, col: Int) {
        if (row == -1 && col == -1) return
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
        checkVitoria()
    }




    fun checkVitoria(){
        if (selectedRow == maiorRow && selectedCol == -1 || selectedCol == maiorCol && selectedRow == -1) {
            vitoria = true
            pontos += 1
            pontosLiveData.postValue(pontos)
            vitoriaLiveData.postValue(true)
            numeroAcertos++
            changeLevel(level.ident)

/*
            timerObject.cancel()
            if (paused + MathGame.level.winTime < MathGame.level.maxTime)
                startTimer(paused.toInt() + MathGame.level.winTime)
            else
                startTimer(MathGame.level.maxTime)

*/

        }
    }

    //TODO ALTERAR O LEVEL APENAS QUANTO ACERTOU EM X CALCULOS
    fun changeLevel(levelAtual : Int){
        numeroAcertos = 0
        when(levelAtual){
            1 -> level = Level.level2
            2 -> level = Level.level3
            3 -> level = Level.level4
            else -> level = Level.level4
        }
    }
}