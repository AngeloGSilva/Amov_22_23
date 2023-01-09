package pt.isec.a2019133504.amov_22_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.MultiplayerScore
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.PlayerScore
import pt.isec.a2019133504.amov_22_23.Data.FirebaseDb
import pt.isec.a2019133504.amov_22_23.adapters.LeaderboardAdapter
import pt.isec.a2019133504.amov_22_23.adapters.MultiplayerScoresAdapter
import pt.isec.a2019133504.amov_22_23.adapters.PlayerScoreAdapter
import pt.isec.a2019133504.amov_22_23.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity() {
    private val user = CurrentUser
    private lateinit var binding: ActivityScoreBinding

    companion object {
        val SINGLE = 0
        val MULTI = 1
        val type = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //TODO add listview to view

        when (intent.getIntExtra("type", SINGLE)) {
            SINGLE -> {
                FirebaseDb.getScores().addOnSuccessListener {
                    var scoreList = PlayerScore.fromQuery(it)
                    var adapter = PlayerScoreAdapter(scoreList,this)
                    binding.topScores.adapter = adapter
                }
            }
            MULTI -> {
                FirebaseDb.getScoresPontos().addOnSuccessListener {
                    var scoreList = MultiplayerScore.fromQuery(it)
                    var adapter = MultiplayerScoresAdapter(scoreList,this)
                    binding.topScores.adapter = adapter
                }
            }
        }
    }
}
