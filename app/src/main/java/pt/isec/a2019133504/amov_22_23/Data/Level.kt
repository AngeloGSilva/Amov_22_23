package pt.isec.a2019133504.amov_22_23.Data

class Level(val ident : Int,
            val opLevel: ArrayList<String>,
            val maxTime : Int,
            val winTime : Int,
            val range: IntRange) {

    companion object{
        val array = arrayOf<Level>(
            Level(1,arrayListOf("+"),30,3,1..9),
            Level(2,arrayListOf("+","-"),40,4,1..99),
            Level(3,arrayListOf("+","-","*"),50,5,1..999),
            Level(4,arrayListOf("+","-","*","/"),60,6,1..999))

        fun get(n : Int) : Level {
            return array.get(n)
        }

        fun isLast(n : Int) : Boolean {
            return n == array.size-1
        }
    }
}