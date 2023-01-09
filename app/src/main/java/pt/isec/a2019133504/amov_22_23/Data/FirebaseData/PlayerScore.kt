package pt.isec.a2019133504.amov_22_23.Data.FirebaseData

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class PlayerScore(val uid: String, val username: String, val pontuacao: Int, val Tempo: Long) {
    constructor(document : DocumentSnapshot) : this(
        document.getString("uid")!!,
        document.getString("username")!!,
        document.getLong("pontuacao")!!.toInt(),
        document.getLong("tempo")!!
    )

    companion object {
        fun fromQuery(it: QuerySnapshot) : List<PlayerScore> {
            var ret = mutableListOf<PlayerScore>()
            for (i in it)
                ret.add(PlayerScore(i))
            return ret
        }
    }
}