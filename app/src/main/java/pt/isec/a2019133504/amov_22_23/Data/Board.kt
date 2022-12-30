package pt.isec.a2019133504.amov_22_23.Data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.json.JSONArray
import org.json.JSONTokener
import kotlin.random.Random
import kotlin.random.nextInt

@Serializable
class Board(val cells: Array<Array<String>>) {

    //TODO calcular resultados das colunas/linhas...
    // guardar em array e criar gets e ter o segundo melhor e o melhor guardado
    // metodo para comprar o maior ou o segundo para os pontos return os pontos (1 ou 2)

    //TODO getmaiorresultado e segundo maior Guardar os resultados das linhas e colunas em arrays

    @Transient var colunas : MutableList<Int> = mutableListOf<Int>(0 , 0 , 0)
    @Transient var linhas : MutableList<Int> = mutableListOf<Int>(0 , 0 , 0)
    @Transient var maior : Double = 0.0
    @Transient var segundoMaior : Double = 0.0

    init {
        val colunasResult : List<Double> = listOf(getResultadoColuna(0) ,getResultadoColuna(2) , getResultadoColuna(4))
        val linhasResult : List<Double> = listOf(getResultadoLinha(0) ,getResultadoLinha(2) , getResultadoLinha(4))
        val resultados = (colunasResult + linhasResult).sortedDescending()


        colunasResult.forEachIndexed { index, element ->
           if (element == resultados[0])
               this.colunas[index] = 2
            else if (element == resultados[1])
               this.colunas[index] = 1
        }

        linhasResult.forEachIndexed { index, element ->
            if (element == resultados[0])
                this.linhas[index] = 2
            else if (element == resultados[1])
                this.linhas[index] = 1
        }

        colunas.forEachIndexed{ index, element ->
            if (element == 1)
                segundoMaior = getResultadoColuna(index*2)
            if (element == 2)
                maior = getResultadoColuna(index*2)
        }

        linhas.forEachIndexed{ index, element ->
            if (element == 1)
                segundoMaior = getResultadoLinha(index*2)
            if (element == 2)
                maior = getResultadoLinha(index*2)
        }
    }


    //TODO Falta tratar do caso de dividir por zero
    //FIXME
    fun getResultadoLinha(linha: Int): Double {
        var result = 0.0

        var operator = cells[linha][1]
        var operator2 = cells[linha][3]
        var num1 = cells[linha][0].toDouble()
        var num3 = cells[linha][2].toDouble()
        var num5 = cells[linha][4].toDouble()

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
        var operator = cells[1][coluna]
        var operator2 = cells[3][coluna]
        var num1 = cells[0][coluna].toDouble()
        var num3 = cells[2][coluna].toDouble()
        var num5 = cells[4][coluna].toDouble()
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

    companion object{
        val sz : Int = 5;

        fun fromLevel(level: Level) : Board {
            val cells: Array<Array<String>> = Array(sz) { linha ->
                Array(sz) { coluna ->
                    if (linha % 2 == 0 && coluna % 2 == 0) {
                        (Random.nextInt(level.range) * 1.0).toString()
                    } else if ((linha == coluna && (linha % 2 != 0 || coluna % 2 != 0)) || (linha % 2 != 0 && coluna % 2 != 0)) {
                        " "
                    } else {
                        level.ops.random()
                    }
                }
            }
            return Board(cells)
        }
    }

}