package pt.isec.a2019133504.amov_22_23.Data

import kotlinx.serialization.Serializable
import org.json.JSONArray
import org.json.JSONObject
import pt.isec.a2019133504.amov_22_23.Data.Deserializers.IntRangeSerializer
import kotlin.collections.ArrayList

typealias _IntRange = @Serializable(IntRangeSerializer::class) IntRange

@Serializable
class Level(val ops: Array<String>,
            val maxTime : Int,
            val winTime : Int,
            val range: _IntRange) {

    fun getJsonObject() : JSONObject {
        var json = JSONObject()
        json.put("maxTime", maxTime)
        json.put("winTime", winTime)
        json.put("rangeStart", range.start)
        json.put("rangeLast", range.last)
        json.put("ops", JSONArray().run { for (op in ops)put(op) })
        return json
    }

    companion object{
        val array = arrayOf<Level>(
            Level(arrayOf("+"),30,3,1..9),
            Level(arrayOf("+","-"),40,4,1..99),
            Level(arrayOf("+","-","*"),50,5,1..999),
            Level(arrayOf("+","-","*","/"),60,6,1..999))

        fun get(n : Int) : Level {
            return array.get(n)
        }

        fun fromJsonObject(_j : JSONObject) : Level {
            val ops = _j.getJSONArray("ops")
            return Level(Array<String>(ops.length()) { op -> ops.getString(op) } , _j.getInt("maxTime"), _j.getInt("winTime"), IntRange(_j.getInt("rangeStart"), _j.getInt("rangeLast")))
        }

        fun isLast(n : Int) : Boolean {
            return n == array.size-1
        }
    }
}