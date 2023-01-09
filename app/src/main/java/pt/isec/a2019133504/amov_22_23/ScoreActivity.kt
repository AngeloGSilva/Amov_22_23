package pt.isec.a2019133504.amov_22_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.PlayerScore
import pt.isec.a2019133504.amov_22_23.Data.FirebaseDb
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
                    //TODO change adapter based on multiplayer or singleplayer
                }
            }
            MULTI -> {
                FirebaseDb.getScores().addOnSuccessListener {
                    //TODO change adapter based on multiplayer or singleplayer
                }
            }
        }
    }
}
