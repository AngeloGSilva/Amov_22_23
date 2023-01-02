package pt.isec.a2019133504.amov_22_23.Data

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

class SinglePlayer() {
    var pontos = 0
    private var NivelAtual: Int = 0
    var boards: Array<Board> = Array(10) { board ->
        Board.fromLevel(Level.get(NivelAtual))
    }
    private var BoardAtual: Int = 0
    var pause: Long = 0

    private var selectedRow = -1
    private var selectedCol = -1

    //para atualizar dados na view
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()

    //var maioresValores = MutableLiveData<Pair<Double,Double>>()
    var pontosLiveData = MutableLiveData<Int>()
    var cellsLiveData = MutableLiveData<Board>()
    var fimLiveData = MutableLiveData<Boolean>()
    var timerCount = MutableLiveData<Long>()
    var nextLevel = MutableLiveData<Boolean>()

    //TODO Init apenas para criar boards
    //TODO objeto singlePlayer onde sao criadas boards e passadas para o MathGame
    //TODO single player ter o timer no mathGame
    //TODO class multiplayer onde vai receber boards/set delas
    //TODO fling no single vai incaminhr para o mathgame no multiplayer envia em json para o server
    //TODO apagar a cena da vitoria

    private lateinit var timerObject: CountDownTimer
    fun startTimer(totalTime: Long) {
        timerObject = object : CountDownTimer((totalTime * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerCount.postValue(millisUntilFinished / 1000)
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
        return boards[BoardAtual]
    }

    fun returnboardcells(): Board {
        return boards[BoardAtual]
    }

    init {
        startTimer(30)
        cellsLiveData.postValue(boards[BoardAtual])
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

    fun checkVitoria() {
        if (selectedCol == -1 && selectedRow != -1)
            pontos += returnboard().getResLinha(selectedRow)
        else if (selectedRow == -1 && selectedCol != -1)
            pontos += returnboard().getResColuna(selectedCol)

        timerObject.cancel()

            val time = pause + Level.get(NivelAtual).winTime
            if (time >= Level.get(NivelAtual).maxTime)
                startTimer(Level.get(NivelAtual).maxTime.toLong())
            else
                startTimer(time)


            if (Level.isLast(NivelAtual) && BoardAtual == boards.size-1) {
                //TODO para acabar
                fimLiveData.postValue(true)
                pontosLiveData.postValue(pontos)
            } else {
                if (BoardAtual == boards.size-1) {
                    BoardAtual = 0
                    NivelAtual++
                    boards = Array(10) { board ->
                        Board.fromLevel(Level.get(NivelAtual))
                    }
                    timerObject.cancel()
                    nextLevel.postValue(true)
                } else
                    BoardAtual++

                pontosLiveData.postValue(pontos)
                cellsLiveData.postValue(returnboardcells())
            }


    }
}
