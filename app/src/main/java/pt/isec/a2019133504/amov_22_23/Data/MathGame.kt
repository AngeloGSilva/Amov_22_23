package pt.isec.a2019133504.amov_22_23.Data

import androidx.lifecycle.MutableLiveData
import kotlin.random.Random
import kotlin.random.nextInt


class MathGame {

    //TODO VERIFICAR SE COMPANION OBJECT È A MELHOR MANEIRA PARA RESOLVER O PROBLEMA DE ACEDER AO LEVEL TIME NA ACTIVITY MODE 1
    companion object{
        //TODO CORRIGIR CLASS LEVEL CALL (isto esta assim pois o kotlin ja tem um class Level)

        lateinit var level:pt.isec.a2019133504.amov_22_23.Data.Level

        val level1 = Level(1,arrayListOf("+"),2,5,30,3,1..9)
        val level2 = Level(2,arrayListOf("+","-"),4,5,40,6,1..99)
        val level3 = Level(3,arrayListOf("+","-","*"),8,5,50,12,1..999)
        val level4 = Level(4,arrayListOf("+","-","*","/"),16,5,60,24,1..999)
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

    init {
/*        var cells = Array(5){ linha ->
            Array(5){ coluna ->
                if (linha % 2 == 0 && coluna % 2 == 0) {
                    Cell(linha,coluna,(Random.nextInt(0,10)*1.0).toString(),false)
                }else if ((linha == coluna && (linha % 2 != 0 || coluna % 2 != 0)) || (linha % 2 != 0 && coluna % 2 != 0)) {
                    Cell(linha,coluna," ",false)
                }else {
                    Cell(linha,coluna,operators.random(),true)
                }
            }
        }*/

        level = level1
        var cells = getrandomBoard()
        board = Board(5, cells)
        cellsLiveData.postValue(board.cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        getMaiorCalculo()
        vitoriaLiveData.postValue(false)
        pontosLiveData.postValue(pontos)
        vitoria = false
    }

    fun resetBoardAtributos(){
        var cells = getrandomBoard()
        board = Board(5, cells)
        cellsLiveData.postValue(board.cells)
        selectedRow = -1
        selectedCol = -1
        //selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        getMaiorCalculo()
        vitoriaLiveData.postValue(false)
        pontosLiveData.postValue(pontos)
        vitoria = false
    }

    fun getrandomBoard(): Array<Array<Cell>> {
        var cells = Array(5){ linha ->
            Array(5){ coluna ->
                if (linha % 2 == 0 && coluna % 2 == 0) {
                    Cell(linha,coluna,(Random.nextInt(level.range)*1.0).toString(),false)

                /*
                    when(level){
                        1 -> Cell(linha,coluna,(Random.nextInt(1,9)*1.0).toString(),false)
                        2 -> Cell(linha,coluna,(Random.nextInt(1,99)*1.0).toString(),false)
                        else -> {
                            Cell(linha,coluna,(Random.nextInt(1,999)*1.0).toString(),false)
                        }
                    }
                    //Cell(linha,coluna,(Random.nextInt(0,10)*1.0).toString(),false)
                */}else if ((linha == coluna && (linha % 2 != 0 || coluna % 2 != 0)) || (linha % 2 != 0 && coluna % 2 != 0)) {
                    Cell(linha,coluna," ",false)
                }else {
                    Cell(linha,coluna,level.opLevel.random(),true)
                /*
                    when(level){
                        1 -> Cell(linha,coluna,opLevel1,true)
                        2 -> Cell(linha,coluna,opLevel2.random(),true)
                        3 -> Cell(linha,coluna,opLevel3.random(),true)
                        else -> {
                            Cell(linha, coluna, opLevel4.random(), true)
                        }
                    }*/
                }
            }
        }
        return cells
    }

/*    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        val cell = board.getCell(selectedRow, selectedCol)
        if (cell.isOperator) return
        cellsLiveData.postValue(board.cells)
    }*/

    fun updateValor(){
        maioresValores.postValue(Pair(maiorRow,maiorCol))
    }

    fun updateSelectedCell(row: Int, col: Int) {
        if (row == -1 && col == -1) return
/*        val cell = board.getCell(row, col)
        if (!cell.isOperator) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(row, col))
        }*/
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
        checkVitoria()
    }

    //TODO Falta tratar do caso de dividir por zero
    fun getResultadoLinha(linha: Int): Double {
        var result = 0.0
/*        var operator = board[linha][1]
        var operator2 = board[linha][3]
        var num1 = (board[linha][0] as Double)
        var num3 = (board[linha][2]as Double)
        var num5 = (board[linha][4]as Double)*/

        var operator = board.cells[linha][1].value
        var operator2 = board.cells[linha][3].value
        var num1 = board.cells[linha][0].value.toDouble()
        var num3 = board.cells[linha][2].value.toDouble()
        var num5 = board.cells[linha][4].value.toDouble()

        //if (operator.equals("*") || operator.equals("/") && operator2.equals("+") || operator2.equals("-")) {
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
            pontos += level.winPoints
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
            1 -> level = level2
            2 -> level = level3
            3 -> level = level4
            else -> level = level4
        }
    }
}