package pt.isec.a2019133504.amov_22_23.Data.FirebaseData

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class MultiplayerScore(val players: List<PlayerScore>, val Tempo: Long) {
    val TotalScore = players.sumOf { p -> p.pontuacao }
}