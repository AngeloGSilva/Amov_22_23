package pt.isec.a2019133504.amov_22_23.Data

import pt.isec.a2019133504.amov_22_23.View.BoardView
import kotlin.random.Random
import kotlin.random.nextInt

class SinglePlayer() {
    val boards : Array<Array<Board>> = Array(Level.array.size) { level ->
        Array(10) { board ->
                Board(5,Level.array[level])
            }
        }
}