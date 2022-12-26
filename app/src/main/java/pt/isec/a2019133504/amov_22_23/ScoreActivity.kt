package pt.isec.a2019133504.amov_22_23

import android.content.ContentValues
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.a2019133504.amov_22_23.databinding.ActivityProfileBinding
import pt.isec.a2019133504.amov_22_23.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScoreBinding
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore
        var topScoresList = ArrayList<Int>()
        var contador = 0
        db.collection("Top5Scores").get().addOnSuccessListener {
                result ->
            for (document in result) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    topScoresList[contador] = document.data.get("Pontuacao").toString().toInt()
                contador++
                }
            }
        .addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents.", exception)
        }


        if (topScoresList.size == 0) return

        if (topScoresList.size >= 1)
            binding.score1.text = topScoresList[0].toString()

        if (topScoresList.size >= 2)
            binding.score1.text = topScoresList[1].toString()

        if (topScoresList.size >= 3)
            binding.score1.text = topScoresList[2].toString()

        if (topScoresList.size >= 4)
            binding.score1.text = topScoresList[3].toString()

        if (topScoresList.size == 5)
            binding.score1.text = topScoresList[4].toString()

    }
}
