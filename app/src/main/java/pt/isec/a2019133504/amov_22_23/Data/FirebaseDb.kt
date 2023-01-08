package pt.isec.a2019133504.amov_22_23.Data

import android.annotation.SuppressLint
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.MultiplayerScore
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.Score
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.UserData
import java.util.UUID

object FirebaseDb {
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore

    fun addScore(score : Score) {
        db.collection("Top5Scores").orderBy("Pontuacao", Query.Direction.ASCENDING).limit(1).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["Pontuacao"].toString().toInt() < score.pontuacao) {
                        db.collection("Top5Scores").document(document.id).set(score.getMap())
                    }
                }
            }
            .addOnFailureListener{ result->
                //TODO set e limite 5
            }
    }

    fun addMultiScore(score : MultiplayerScore) {
        db.collection("Top5ScoresPontos").get()
            .addOnSuccessListener { result ->
                if (result.size() < 5) {
                    db.collection("Top5ScoresPontos").document(UUID.randomUUID().toString()).set(score)
                    return@addOnSuccessListener
                }

                var doc = result.documents.minBy { d -> (d.data!!["totalScore"] as Int) }
                if((doc.data!!["totalScore"] as Int) < score.TotalScore)
                    db.collection("Top5ScoresPontos").document(doc.id).set(score)
            }

        db.collection("Top5ScoresTempo").get()
            .addOnSuccessListener { result ->
                if (result.size() < 5) {
                    db.collection("Top5ScoresTempo").document(UUID.randomUUID().toString()).set(score)
                    return@addOnSuccessListener
                }

                var doc = result.documents.minBy { d -> (d.data!!["tempo"] as Long) }
                if((doc.data!!["tempo"] as Long) < score.Tempo)
                    db.collection("Top5ScoresTempo").document(doc.id).set(score)
            }
    }

    fun getUserData(uid : String): Task<DocumentSnapshot> {
        return db.collection(UserData.Collection).document(uid).get()
    }

    fun setUserData(uid : String, newUserData: UserData): Task<Void> {
        return db.collection(UserData.Collection).document(uid).set(newUserData.getMap())
    }

    fun getScores() : Task<QuerySnapshot> {
        return db.collection("Top5Scores").get()
    }
}