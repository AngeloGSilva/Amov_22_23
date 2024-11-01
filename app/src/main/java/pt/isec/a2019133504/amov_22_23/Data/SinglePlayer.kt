package pt.isec.a2019133504.amov_22_23.Data

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SinglePlayer : ViewModel(){
    var pontos = 0
    private var NivelAtual: Int = 0
    var boards: Array<Board> = Array(10) { board ->
        Board.fromLevel(Level.get(NivelAtual))
    }
    private var BoardAtual: Int = 0

    private var selectedRow = -1
    private var selectedCol = -1

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()

    var starter : Boolean = false
    private var timeleft: Long = 0
    var pontosLiveData = MutableLiveData<Int>()
    var cellsLiveData = MutableLiveData<Board>()
    var fimLiveData = MutableLiveData<Boolean>()
    var timerCount = MutableLiveData<Long>()
    var nextLevel = MutableLiveData<Boolean>()

    private val timer = Timer()
    private var timertask : TimerTask? = null

    fun startTimer(totalTime: Long) {
        timeleft = totalTime
        timerCount.postValue(timeleft)
        timertask?.cancel()
        timertask = object : TimerTask() {
            override fun run() {
                timeleft--
                timerCount.postValue(timeleft)
                if (timeleft == 0L) {
                    this.cancel()
                    fimLiveData.postValue(true)
                    pontosLiveData.postValue(pontos)
                }
            }
        }
        timer.schedule(timertask, 0, 1000)
    }

    fun returnboard(): Board {
        return boards[BoardAtual]
    }

    fun returnboardcells(): Board {
        return boards[BoardAtual]
    }

    fun startGame(nivel: Int = 0, board: Int = 0, _pontos : Int = 0, tempo : Long = -1L) {
        var newLevel = false
        starter = true
        NivelAtual = nivel
        pontos = _pontos
        BoardAtual = board
        if (Level.isLast(NivelAtual) && BoardAtual == boards.size-1) {
            fimLiveData.postValue(true)
            pontosLiveData.postValue(pontos)
            starter = false
            return
        }
        if (BoardAtual == 10){
            BoardAtual = 0
            NivelAtual++
            newLevel = true
        }
        boards = Array(10) { board ->
            Board.fromLevel(Level.get(NivelAtual))
        }
        if (newLevel || tempo == -1L)
            startTimer(Level.get(NivelAtual).maxTime.toLong())
        else
            startTimer(tempo)
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

        timertask?.cancel()
        var res = 0
        if (selectedCol == -1 && selectedRow != -1)
            res = returnboard().getResLinha(selectedRow)
        else if (selectedRow == -1 && selectedCol != -1)
            res = returnboard().getResColuna(selectedCol)

        pontos += res

        if (Level.isLast(NivelAtual) && BoardAtual == boards.size-1) {
            fimLiveData.postValue(true)
            pontosLiveData.postValue(pontos)
            starter=false
            return
        }
        val time : Long
        if (BoardAtual == boards.size-1) {
            BoardAtual = 0
            NivelAtual++
            boards = Array(10) { board ->
                Board.fromLevel(Level.get(NivelAtual))
            }
            nextLevel.postValue(true)
            time = Level.get(NivelAtual).maxTime.toLong()
            pontosLiveData.postValue(pontos)
            cellsLiveData.postValue(returnboardcells())
            if (time > Level.get(NivelAtual).maxTime)
                timerCount.postValue(Level.get(NivelAtual).maxTime.toLong())
            else
                timerCount.postValue(time)
        } else{
            BoardAtual++
            time = timeleft + if (res == 0) 0 else Level.get(NivelAtual).winTime
            pontosLiveData.postValue(pontos)
            cellsLiveData.postValue(returnboardcells())
            if (time > Level.get(NivelAtual).maxTime)
                startTimer(Level.get(NivelAtual).maxTime.toLong())
            else
                startTimer(time)
        }
    }
}
