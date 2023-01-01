package pt.isec.a2019133504.amov_22_23.Data

import com.ezylang.evalex.Expression
import com.ezylang.evalex.data.EvaluationValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.json.JSONArray
import org.json.JSONTokener
import kotlin.random.Random
import kotlin.random.nextInt

open class Cell()
class Operator() : Cell()

@Serializable
class Board(val cells: Array<Array<String>>, @Transient val empty: Boolean = false) {

    //TODO calcular resultados das colunas/linhas...
    // guardar em array e criar gets e ter o segundo melhor e o melhor guardado
    // metodo para comprar o maior ou o segundo para os pontos return os pontos (1 ou 2)

    //TODO getmaiorresultado e segundo maior Guardar os resultados das linhas e colunas em arrays

    @Transient private var colunas : MutableList<Int> = mutableListOf<Int>(0 , 0 , 0)
    @Transient private var linhas : MutableList<Int> = mutableListOf<Int>(0 , 0 , 0)
    @Transient private var maior : Double = 0.0
    @Transient private var segundoMaior : Double = 0.0

    fun getResLinha(linha: Int): Int = linhas[linha/2]
    fun getResColuna(coluna: Int): Int = colunas[coluna/2]

    init {
        if (!empty)
            calculate()
    }

    private fun calculate() {
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

    private fun getResultadoLinha(linha: Int): Double {
        val exp = cells[linha].joinToString(separator = "")
        val expression = Expression(exp)
        val result: EvaluationValue = expression.evaluate()
        return result.numberValue.toDouble()
    }

    private fun getResultadoColuna(coluna: Int): Double {
        val exp = cells[0][coluna] + cells[1][coluna] + cells[2][coluna] + cells[3][coluna] + cells[4][coluna]
        val expression = Expression(exp)
        val result: EvaluationValue = expression.evaluate()
        return result.numberValue.toDouble()
    }

    companion object{
        val sz : Int = 5

        fun fromLevel(level: Level) : Board {
            val cells: Array<Array<String>> = Array(sz) { linha ->
                Array(sz) { coluna ->
                    if (linha % 2 == 0 && coluna % 2 == 0)
                        Random.nextInt(level.range).toString()
                    else if (linha == coluna || (linha % 2 != 0 && coluna % 2 != 0))
                        ""
                    else
                        level.ops.random()
                }
            }
            return Board(cells)
        }

        val empty = Board(Array(sz){Array(sz){""}}, true)
    }

}