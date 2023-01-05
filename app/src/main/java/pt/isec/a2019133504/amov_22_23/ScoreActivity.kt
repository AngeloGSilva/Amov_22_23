package pt.isec.a2019133504.amov_22_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.Score
import pt.isec.a2019133504.amov_22_23.Data.FirebaseDb
import pt.isec.a2019133504.amov_22_23.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity() {
    private val user = CurrentUser
    private lateinit var binding: ActivityScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var topScoresList = ArrayList<Int>()
        var contador = 0

        //TODO add listview to view
        FirebaseDb.getScores().addOnSuccessListener {
            val scores = Score.fromQuery(it)
            //TODO change adapter based on multiplayer or singleplayer
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
