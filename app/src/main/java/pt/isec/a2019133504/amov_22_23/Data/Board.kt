package pt.isec.a2019133504.amov_22_23.Data

import org.xml.sax.Parser
import java.text.ParseException
import kotlin.random.Random
import kotlin.random.nextInt

class Board(nivel : Int) {
    companion object{
        private val size = 5
    }
    val corretas = 0
        get() = field

    val nivel = nivel
    var operators = arrayListOf<String>("*","+","/","-")
    //var board: Array<String>[size][size]
    var board = Array(size){ linha ->
        Array(size){ coluna ->
            if (linha % 2 == 0 && coluna % 2 == 0){
                Random.nextInt(0..10*nivel)
            }else if ((linha==coluna && (linha%2!=0 || coluna%2!=0)) || (linha%2!=0 && coluna%2!=0)){
                    " "
            }else{
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

    //TODO CHEGAR AO RESULDADO DA LINHA/COLUNA
    fun getResultadoLinha(linha : Int){
/*        var numero=0
        var operador: Any? = null
        for (r in 0 until size){
            for (c in 0 until  size){
                if (r == linha){
                    if (r%2==0)

                }
            }
        }*/
    }

    fun getResultadoColuna(coluna : Int){
        for (r in 0 until size){
            for (c in 0 until  size){

            }
        }
    }
}
