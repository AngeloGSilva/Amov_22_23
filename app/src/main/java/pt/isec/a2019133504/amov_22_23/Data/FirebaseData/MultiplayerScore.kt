package pt.isec.a2019133504.amov_22_23.Data.FirebaseData

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.getField

data class MultiplayerScore(val players: List<PlayerScore>, val Tempo: Long) {
    val TotalScore = players.sumOf { p -> p.pontuacao }

    constructor(document : DocumentSnapshot) : this(
        document.getField<List<PlayerScore>>("players")!!,
        document.getLong("tempo")!!,
    )

    companion object {
        fun fromQuery(it: QuerySnapshot) : List<MultiplayerScore> {
            var ret = mutableListOf<MultiplayerScore>()
            for (i in it)
                ret.add(MultiplayerScore(i))
            return ret
        }
    }
}