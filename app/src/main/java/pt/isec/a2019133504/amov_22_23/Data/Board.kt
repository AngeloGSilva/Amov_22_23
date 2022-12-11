package pt.isec.a2019133504.amov_22_23.Data

import kotlin.random.Random
import kotlin.random.nextInt

class Board(val size: Int, val level: Level) {
    //TODO calcular resultados das colunas/linhas...
    // guardar em array e criar gets e ter o segundo melhor e o melhor guardado
    // metodo para comprar o maior ou o segundo para os pontos return os pontos (1 ou 2)

    var cells: Array<Array<Cell>> = Array(5) { linha ->
        Array(5) { coluna ->
            if (linha % 2 == 0 && coluna % 2 == 0) {
                Cell((Random.nextInt(level.range) * 1.0).toString())
            } else if ((linha == coluna && (linha % 2 != 0 || coluna % 2 != 0)) || (linha % 2 != 0 && coluna % 2 != 0)) {
                Cell( " ")
            } else {
                Cell(level.opLevel.random())
            }
        }
    }
}