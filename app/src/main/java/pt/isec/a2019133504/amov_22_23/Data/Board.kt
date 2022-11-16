package pt.isec.a2019133504.amov_22_23.Data

import org.xml.sax.Parser
import java.text.ParseException
import kotlin.random.Random
import kotlin.random.nextInt

class Board(nivel : Int) {
    companion object{
        private val size = 5
    }

    var colunasValores = arrayListOf<Int>()
    var linhasValores = arrayListOf<Int>()
    val corretas = 0
        get() = field

    val nivel = nivel
    //var operators = arrayListOf<String>("*","+","/","-")
    var operators = arrayListOf<String>("+")
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

    fun getallResult(){
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
        for (r in 0 until size){
            for (c in 0 until  size){
                if (r % 2 == 0 && c % 2 == 0){
                    linhas[c] = (board[r][c] as Int)
                    //linhas.add(board[r][c] as kotlin.Int )
                }
                if (c % 2 == 0 && r % 2 == 0){
                    colunas[c] = (board[r][c] as Int)
                    //colunas.add(board[r][c] as kotlin.Int)
                }
            }
        }
        colunasValores = colunas.toList() as ArrayList<Int>
        linhasValores = linhas.toList() as ArrayList<Int>
    }

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
