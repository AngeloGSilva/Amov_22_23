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
                    db.collection("Top5ScoresPontos").document((result.size()+1).toString()).set(score)
                    return@addOnSuccessListener
                }

                for (document in result)
                    if((document.data["TotalScore"] as Int) < score.TotalScore)
                        db.collection("Top5ScoresPontos").document(document.id).set(score)
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