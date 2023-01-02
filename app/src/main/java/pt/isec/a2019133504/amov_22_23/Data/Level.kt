package pt.isec.a2019133504.amov_22_23.Data

import kotlinx.serialization.Serializable
import org.json.JSONArray
import org.json.JSONObject
import pt.isec.a2019133504.amov_22_23.Data.Deserializers.IntRangeSerializer
import pt.isec.a2019133504.amov_22_23.Data.Deserializers._IntRange
import kotlin.collections.ArrayList


@Serializable
class Level(val ops: Array<String>,
            val maxTime : Int,
            val winTime : Int,
            val range: _IntRange,
            val threshold: Int) {

    companion object{
        val array = arrayOf<Level>(
            Level(arrayOf("+"),30,3,1..9, 5),
            Level(arrayOf("+","-"),40,4,1..99, 15),
            Level(arrayOf("+","-","*"),50,5,1..999, 30),
            Level(arrayOf("+","-","*","/"),60,6,1..999, 60))

        fun get(n : Int) : Level {
            return array.get(n)
        }

        fun isLast(n : Int) : Boolean {
            return n == array.size-1
        }
    }
}