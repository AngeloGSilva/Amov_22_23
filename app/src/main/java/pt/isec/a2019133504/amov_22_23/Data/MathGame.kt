package pt.isec.a2019133504.amov_22_23.Data

import android.os.CountDownTimer
import android.view.View
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

    private var selectedRow = -1
    private var selectedCol = -1

    private var maiorRow = 0
        get() = field
    private var maiorCol = 0
        get() = field

    private var vitoria = false

    private var pontos = 0

    private var numeroAcertos = 0

    var board: Board


/*    var opLevel1 = "+"
    var opLevel2 = arrayListOf<String>("+","-")
    var opLevel3 = arrayListOf<String>("+","-","*")
    var opLevel4 = arrayListOf<String>("+","-","*","/")*/

    //TODO Init apenas para criar boards
    //TODO objeto singlePlayer onde sao criadas boards e passadas para o MathGame
    //TODO no single player ter o timer no mathGame
    //TODO class multiplayer onde vai receber boards/set delas
    //TODO fling no single vai incaminhr para o mathgame no multiplayer envia em json para o server
    //TODO apagar a cena da vitoria

    init {
        level = Level.level1
        board = Board(5, level)
        cellsLiveData.postValue(board.cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        getMaiorCalculo()
        vitoriaLiveData.postValue(false)
        pontosLiveData.postValue(pontos)
        vitoria = false
    }

    fun resetBoardAtributos(){
        board = Board(5, level)
        cellsLiveData.postValue(board.cells)
        selectedRow = -1
        selectedCol = -1
        getMaiorCalculo()
        vitoriaLiveData.postValue(false)
        pontosLiveData.postValue(pontos)
        vitoria = false
    }


    fun updateValor(){
        maioresValores.postValue(Pair(maiorRow,maiorCol))
    }

    fun updateSelectedCell(row: Int, col: Int) {
        if (row == -1 && col == -1) return
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
        checkVitoria()
    }

    //TODO Falta tratar do caso de dividir por zero
    //FIXME
    fun getResultadoLinha(linha: Int): Double {
        var result = 0.0

        var operator = board.cells[linha][1].value
        var operator2 = board.cells[linha][3].value
        var num1 = board.cells[linha][0].value.toDouble()
        var num3 = board.cells[linha][2].value.toDouble()
        var num5 = board.cells[linha][4].value.toDouble()

        when (operator) {
            "*" -> {
                when (operator2) {
                    "+" -> {
                        result = (num1 * num3) + num5
                        //result = ((num1.times(num3)).plus(num5))
                    }
                    "-" -> {
                        result = (num1.times(num3)).minus(num5)
                    }
                    "*"->{
                        result = num1.times(num3).times(num5)
                    }
                    "/"->{
                        result = (num1 * num3)/num5
                        //result = num1.times(num3).div(num5)
                    }
                }
            }
            "/" ->{
                when (operator2) {
                    "+" -> {
                        result = (num1.div(num3)).plus(num5)
                    }
                    "-" -> {
                        result = (num1.div(num3)).minus(num5)
                    }
                    "*"->{
                        result = num1.div(num3).times(num5)
                    }
                    "/"->{
                        result = num1.div(num3).div(num5)
                    }
                }
            }
            "+" -> {
                when (operator2) {
                    "+" -> {
                        result = num1.plus(num3).plus(num5)
                    }
                    "-" -> {
                        result = num1.plus(num3).minus(num5)
                    }
                    "*"->{
                        result = num1.plus(num3.times(num5))
                    }
                    "/"->{
                        result = num1.plus(num3.div(num5))
                    }
                }
            }
            "-"->{
                when (operator2) {
                    "+" -> {
                        result = num1.minus(num3).plus(num5)
                    }
                    "-" -> {
                        result = num1.minus(num3).minus(num5)
                    }
                    "*"->{
                        result = num1.minus(num3.times(num5))
                    }
                    "/"->{
                        result = num1.minus(num3.div(num5))
                    }
                }
            }
        }
        return result
    }

    fun getResultadoColuna(coluna: Int): Double {
        var result = 0.0
        var operator = board.cells[1][coluna].value
        var operator2 = board.cells[3][coluna].value
        var num1 = board.cells[0][coluna].value.toDouble()
        var num3 = board.cells[2][coluna].value.toDouble()
        var num5 = board.cells[4][coluna].value.toDouble()
        //if (operator.equals("*") || operator.equals("/") && operator2.equals("+") || operator2.equals("-")) {
        when (operator) {
            "*" -> {
                when (operator2) {
                    "+" -> {
                        result = ((num1.times(num3)).plus(num5))
                    }
                    "-" -> {
                        result = (num1.times(num3)).minus(num5)
                    }
                    "*"->{
                        result = num1.times(num3).times(num5)
                    }
                    "/"->{
                        result = num1.times(num3).div(num5)
                    }
                }
            }
            "/" ->{
                when (operator2) {
                    "+" -> {
                        result = (num1.div(num3)).plus(num5)
                    }
                    "-" -> {
                        result = (num1.div(num3)).minus(num5)
                    }
                    "*"->{
                        result = num1.div(num3).times(num5)
                    }
                    "/"->{
                        result = num1.div(num3).div(num5)
                    }
                }
            }
            "+" -> {
                when (operator2) {
                    "+" -> {
                        result = num1.plus(num3).plus(num5)
                    }
                    "-" -> {
                        result = num1.plus(num3).minus(num5)
                    }
                    "*"->{
                        result = num1.plus(num3.times(num5))
                    }
                    "/"->{
                        result = num1.plus(num3.div(num5))
                    }
                }
            }
            "-"->{
                when (operator2) {
                    "+" -> {
                        result = num1.minus(num3).plus(num5)
                    }
                    "-" -> {
                        result = num1.minus(num3).minus(num5)
                    }
                    "*"->{
                        result = num1.minus(num3.times(num5))
                    }
                    "/"->{
                        result = num1.minus(num3.div(num5))
                    }
                }
            }
        }
        return result
    }


    fun getMaiorCalculo(){
        getMaiorCol()
        getMaiorRow()
        if (getResultadoLinha(maiorRow) >= getResultadoColuna(maiorCol))
            maiorCol = -1
        else
            maiorRow = -1

        updateValor()
    }

    fun getMaiorCol(){
        var max = getResultadoColuna(0)
        for (i in 0 until board.size){
            if (i % 2 == 0 && getResultadoColuna(i) >= max){
                max = getResultadoColuna(i)
                maiorCol = i
            }
        }
    }

    fun getMaiorRow(){
        var max = getResultadoLinha(0)
        for (i in 0 until board.size){
            if (i % 2 == 0 && getResultadoLinha(i) >= max){ //i%2 linhas validas
                max = getResultadoLinha(i)
                maiorRow = i
            }
        }
    }

    fun checkVitoria(){
        if (selectedRow == maiorRow && selectedCol == -1 || selectedCol == maiorCol && selectedRow == -1) {
            vitoria = true
            pontos += 1
            pontosLiveData.postValue(pontos)
            vitoriaLiveData.postValue(true)
            numeroAcertos++
            if(numeroAcertos == level.numeroAcertos){
                changeLevel(level.ident)
            }
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