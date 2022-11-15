package pt.isec.a2019133504.amov_22_23.Data

class Board() {
    companion object{
        private val size = 5
    }

    lateinit var board: Array<Array<String>>

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
        for (r in 0 until size){
            for (c in 0 until  size){

            }
        }
    }

    fun getResultadoColuna(coluna : Int){
        for (r in 0 until size){
            for (c in 0 until  size){

            }
        }
    }
}
