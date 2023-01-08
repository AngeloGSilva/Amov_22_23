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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //TODO add listview to view


        FirebaseDb.getScores().addOnSuccessListener {
            val scores = Score.fromQuery(it)
            //TODO change adapter based on multiplayer or singleplayer
        }




    }
}
