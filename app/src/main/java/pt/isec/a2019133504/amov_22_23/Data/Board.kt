package pt.isec.a2019133504.amov_22_23.Data

import org.xml.sax.Parser
import java.math.BigDecimal
import java.text.ParseException
import kotlin.random.Random
import kotlin.random.nextInt

class Board(nivel : Int) {
    companion object {
        private val size = 5
    }

    var colunasValores = arrayListOf<Double>()
        get() = field

    var linhasValores = arrayListOf<Double>()
        get() = field
    var maiorValor = 0.0
    val corretas = 0
        get() = field

    val nivel = nivel

    var operators = arrayListOf<String>("*","+","/","-")
    //var operators = arrayListOf<String>("+")

    //var board: Array<String>[size][size]
    var board = Array(size) { linha ->
        Array(size) { coluna ->
            if (linha % 2 == 0 && coluna % 2 == 0) {
                Random.nextInt(0,10)*1.0
            } else if ((linha == coluna && (linha % 2 != 0 || coluna % 2 != 0)) || (linha % 2 != 0 && coluna % 2 != 0)) {
                " "
            } else {
                operators.random()
            }
        }
    }

    /*fun setValue(board: Array<Array<String>>){
        this.board = board
    }*/
/*    Declaration:

    var matrix: Array<Array<Obstacle?>>
    Instantiation and initialization:

    matrix = Array(numRows) { row ->
        Array(numCols) { col ->
            Obstacle(row, col)
        }
    }*/
/*    var board: Array<Array<String>>
      get() {
            return null
        }
        set(value) {
            for (r in 0 until size){
                for (c in 0 until  size){
                    if (r % 2 == 0 && c % 2 == 0) {
                        board[r][c] = "12"
                    }else
                        board[r][c] = "X"
                }
            }
        }*/

    fun getallResult() {
/*        var  result = arrayListOf<Int>()
        var sum = 0;
        var ia = IntArray(size)
        for(r in 0 until size) {
            sum = 0;
            if (r == 0 || r==2 || r==4 ){
                for (c in 0 until size) {
                    var operador = board[r][1]
                    var operador2 = board[r][3]
                    if (c == 0 || c == 2 || c == 4)
                        ia[c] = (board[r][c] as kotlin.Int)
                    else
                        ia[c] = 0
                    when (operador) {
                        "+" -> {
                            sum += ia[c]
                            System.out.println(sum)
                        }
                    }
                }
                result.add(sum)
            }
        }
        return result*/
        var colunas = IntArray(size)
        var linhas = IntArray(size)
        //Preenche os arrays com numeros
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (r % 2 == 0 && c % 2 == 0) {
                    linhas[c] = (board[r][c] as Int)
                    //linhas.add(board[r][c] as kotlin.Int )
                }
                if (c % 2 == 0 && r % 2 == 0) {
                    colunas[c] = (board[r][c] as Int)
                    //colunas.add(board[r][c] as kotlin.Int)
                }
            }
        }
        attresults()


    }
    fun attresults(){
        colunasValores.clear()
        linhasValores.clear()
        linhasValores.add(getResultadoLinha(0))
        linhasValores.add(getResultadoLinha(2))
        linhasValores.add(getResultadoLinha(4))
        colunasValores.add(getResultadoColuna(0))
        colunasValores.add(getResultadoColuna(2))
        colunasValores.add(getResultadoColuna(4))
    }

    //TODO Falta tratar do caso de dividir por zero
    fun getResultadoLinha(linha: Int): Double {
         var result = 0.0
        var operator = board[linha][1]
        var operator2 = board[linha][3]
        var num1 = (board[linha][0] as Double)
        var num3 = (board[linha][2]as Double)
        var num5 = (board[linha][4]as Double)
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

    fun getResultadoColuna(coluna: Int):Double {
        var result = 0.0
        var operator = board[1][coluna]
        var operator2 = board[3][coluna]
        var num1 = (board[0][coluna] as Double)
        var num3 = (board[2][coluna]as Double)
        var num5 = (board[4][coluna]as Double)
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

    fun getColunaMax():Double{
        return colunasValores.max()
    }

    fun getLinhaMax():Double{
        return linhasValores.max()
    }

    fun setMaiorValor(){
        if (getColunaMax() > getLinhaMax())
            maiorValor = getColunaMax()
        else
            maiorValor = getLinhaMax()
    }
}
