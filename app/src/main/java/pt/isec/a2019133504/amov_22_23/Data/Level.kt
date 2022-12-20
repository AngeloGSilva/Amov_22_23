package pt.isec.a2019133504.amov_22_23.Data

class Level(val ident : Int,
            val opLevel: ArrayList<String>,
            val maxTime : Int,
            val winTime : Int,
            val range: IntRange) {

    companion object{
        val array = arrayOf<Level>(
            Level(1,arrayListOf("+"),30,3,1..9),
            Level(2,arrayListOf("+","-"),40,6,1..99),
            Level(3,arrayListOf("+","-","*"),50,12,1..999),
            Level(4,arrayListOf("+","-","*","/"),60,24,1..999))
    }
}