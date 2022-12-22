package pt.isec.a2019133504.amov_22_23.Data

import android.os.CountDownTimer
import android.os.Handler
import androidx.lifecycle.MutableLiveData

class SinglePlayer() {
    val boards : Array<Array<Board>> = Array(Level.array.size) { level ->
        Array(10) { board ->
                Board(5,Level.array[level])
            }
        }

    var pontos = 0
    private var BoardAtual : Int = 0
    private var NivelAtual : Int = 0
    var pause : Long = 0

    private var selectedRow = -1
    private var selectedCol = -1

    //para atualizar dados na view
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    //var maioresValores = MutableLiveData<Pair<Double,Double>>()
    var pontosLiveData = MutableLiveData<Int>()
    var cellsLiveData = MutableLiveData< Array<Array<Cell>>>()
    var fimLiveData = MutableLiveData<Boolean>()
    var timerCount = MutableLiveData<Long>()
    var nextLevel = MutableLiveData<Boolean>()

    //TODO Init apenas para criar boards
    //TODO objeto singlePlayer onde sao criadas boards e passadas para o MathGame
    //TODO single player ter o timer no mathGame
    //TODO class multiplayer onde vai receber boards/set delas
    //TODO fling no single vai incaminhr para o mathgame no multiplayer envia em json para o server
    //TODO apagar a cena da vitoria

    private lateinit var timerObject : CountDownTimer
    fun startTimer(totalTime : Long){
        timerObject = object : CountDownTimer((totalTime*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerCount.postValue(millisUntilFinished/1000)
                pause = millisUntilFinished / 1000
            }
            override fun onFinish() {
                //TODO para acabar
                fimLiveData.postValue(true)
                pontosLiveData.postValue(pontos)
            }
        }.start()
    }



    fun returnboard(): Board {
        return boards[NivelAtual][BoardAtual]
    }

    fun returnboardcells(): Array<Array<Cell>> {
        return boards[NivelAtual][BoardAtual].cells
    }

    init {
        startTimer(30)
        cellsLiveData.postValue(boards[NivelAtual][BoardAtual].cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        pontosLiveData.postValue(pontos)
    }



    fun updateSelectedCell(row: Int, col: Int) {
        if (row == -1 && col == -1) return
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
        checkVitoria()
    }

    fun checkVitoria(){

        //TODO ver a linha ou coluna e ver onde o resultado da linha ou coluna selecionada se encontra(soma mais alta ou segunda mais alta)
        //TODO somar os pontos e continuar para a proxima board do msm niver ou proxima board do nivel seguinte

        if (selectedCol == -1 && selectedRow != -1) {
            if(returnboard().getResultadoLinha(selectedRow) == returnboard().maior){
                pontos += 2
                timerObject.cancel()
                startTimer(pause+returnboard().level.winTime)
            }else if (returnboard().getResultadoLinha(selectedRow) == returnboard().segundoMaior) {
                pontos += 1
                timerObject.cancel()
                startTimer(pause+returnboard().level.winTime)
            }
        }else if (selectedRow == -1 && selectedCol != -1){
            if(returnboard().getResultadoColuna(selectedCol) == returnboard().maior){
                pontos += 2
                timerObject.cancel()
                startTimer(pause+returnboard().level.winTime)
            }else if (returnboard().getResultadoColuna(selectedCol) == returnboard().segundoMaior) {
                pontos += 1
                timerObject.cancel()
                startTimer(pause+returnboard().level.winTime)
            }
        }


        if (NivelAtual == 3 && BoardAtual==9){
            //TODO para acabar
            fimLiveData.postValue(true)
            pontosLiveData.postValue(pontos)
        }else{
            if (BoardAtual == 9) {
                BoardAtual = 0
                NivelAtual++
                timerObject.cancel()
                nextLevel.postValue(true)
                //TODO aletar o nextLevel para os 5 segundos de pausa de forma mais correta
            } else
                BoardAtual++
            pontosLiveData.postValue(pontos)
            cellsLiveData.postValue(returnboardcells())
        }
    }

}
