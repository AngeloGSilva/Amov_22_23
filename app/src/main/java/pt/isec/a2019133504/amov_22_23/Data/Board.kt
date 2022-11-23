package pt.isec.a2019133504.amov_22_23.Data

class Board(val size: Int, val cells: Array<Array<Cell>>) {
    fun getCell(row: Int, col: Int) = cells[row][col]
}