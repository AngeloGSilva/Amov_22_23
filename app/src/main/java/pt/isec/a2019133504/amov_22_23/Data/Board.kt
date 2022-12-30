package pt.isec.a2019133504.amov_22_23.Data

import org.json.JSONArray
import kotlin.random.Random
import kotlin.random.nextInt

class Board(val size: Int, val level: Level) {
    //TODO calcular resultados das colunas/linhas...
    // guardar em array e criar gets e ter o segundo melhor e o melhor guardado
    // metodo para comprar o maior ou o segundo para os pontos return os pontos (1 ou 2)

    //TODO getmaiorresultado e segundo maior Guardar os resultados das linhas e colunas em arrays

    var cells: Array<Array<Cell>> = Array(size) { linha ->
        Array(size) { coluna ->
            if (linha % 2 == 0 && coluna % 2 == 0) {
                Cell((Random.nextInt(level.range) * 1.0).toString())
            } else if ((linha == coluna && (linha % 2 != 0 || coluna % 2 != 0)) || (linha % 2 != 0 && coluna % 2 != 0)) {
                Cell( " ")
            } else {
                Cell(level.opLevel.random())
            }
        }
    }
    var colunas : MutableList<Int> = mutableListOf<Int>(0 , 0 , 0)
    var linhas : MutableList<Int> = mutableListOf<Int>(0 , 0 , 0)
    var maior : Double = 0.0
    var segundoMaior : Double = 0.0

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

        var operator = cells[linha][1].value
        var operator2 = cells[linha][3].value
        var num1 = cells[linha][0].value.toDouble()
        var num3 = cells[linha][2].value.toDouble()
        var num5 = cells[linha][4].value.toDouble()

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
        var operator = cells[1][coluna].value
        var operator2 = cells[3][coluna].value
        var num1 = cells[0][coluna].value.toDouble()
        var num3 = cells[2][coluna].value.toDouble()
        var num5 = cells[4][coluna].value.toDouble()
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

    fun getJsonArray() : JSONArray{
        val jsonArray = JSONArray()
        for (line in cells){
            val jsonarray2 =JSONArray()
            for(column in line){
                jsonarray2.put(column)
            }
            jsonArray.put(jsonarray2)
        }
        return jsonArray
    }

    companion object{
        fun fromJson(){

        }
    }

}