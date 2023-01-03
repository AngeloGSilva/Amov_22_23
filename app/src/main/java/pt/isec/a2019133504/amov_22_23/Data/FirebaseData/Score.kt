package pt.isec.a2019133504.amov_22_23.Data.FirebaseData

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class Score(val uid: String, val username: String, val pontuacao: Long, val Tempo: Long) {
    fun getMap() : HashMap<String, Any?> {
        return hashMapOf(
            Uid to uid,
            Username to username,
            Pontuacao to pontuacao,
            Time to Tempo
        )
    }

    constructor(document : DocumentSnapshot) : this(
        document.getString(Uid)!!,
        document.getString(Username)!!,
        document.getLong(Pontuacao)!!,
        document.getLong(Time)!!
    )

    companion object {
        val Uid = "Uid"
        val Username = "Username"
        val Pontuacao = "Pontuacao"
        val Time = "Time"

        fun fromQuery(querySnapshot: QuerySnapshot) : List<Score> {
            val list = mutableListOf<Score>()
            for (document in querySnapshot)
                list.add(Score(document))
            return list
        }
    }
}