package pt.isec.a2019133504.amov_22_23.Data.FirebaseData

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class MultiplayerPlayer(val uid: String, val username: String, val pontuacao: Int)

data class MultiplayerScore(val players: List<MultiplayerPlayer>, val Tempo: Long) {
    val TotalScore = players.sumOf { p -> p.pontuacao }
}