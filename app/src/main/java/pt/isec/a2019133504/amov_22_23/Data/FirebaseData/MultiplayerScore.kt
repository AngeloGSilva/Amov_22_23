package pt.isec.a2019133504.amov_22_23.Data.FirebaseData

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class MultiplayerPlayer(val uid: String, val username: String, val pontuacao: Int) {
    fun getMap() : HashMap<String, Any?> {
        return hashMapOf(
            Uid to uid,
            Username to username,
            Pontuacao to pontuacao
        )
    }

    companion object {
        val Uid = "uid"
        val Username = "username"
        val Pontuacao = "pontuacao"

        fun fromQuery(querySnapshot: QuerySnapshot) : List<Score> {
            val list = mutableListOf<Score>()
            for (document in querySnapshot)
                list.add(Score(document))
            return list
        }
    }
}

data class MultiplayerScore(val players: List<MultiplayerPlayer>, val Tempo: Long) {
    fun getMap() : HashMap<String, Any?> {
        return hashMapOf(
            Players to players,
            Time to Tempo
        )
    }

    fun getSumScore() : Int {
        return players.sumOf { p -> p.pontuacao }
    }

    constructor(document : DocumentSnapshot) : this(
        document.get(Players)!! as List<MultiplayerPlayer>,
        document.getLong(Time)!!
    )

    companion object {
        val Players = "players"
        val Time = "Time"

        fun fromQuery(querySnapshot: QuerySnapshot) : List<Score> {
            val list = mutableListOf<Score>()
            for (document in querySnapshot)
                list.add(Score(document))
            return list
        }
    }
}