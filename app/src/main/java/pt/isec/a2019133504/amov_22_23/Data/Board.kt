package pt.isec.a2019133504.amov_22_23.Data

class Board(val row: Int,
            val col: Int,
            var value: Int,
            var isStartingCell: Boolean = false,
            var notes: MutableSet<Int> = mutableSetOf<Int>()) {


}