package pt.isec.a2019133504.amov_22_23

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.MultiplayerScore
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.PlayerScore
import pt.isec.a2019133504.amov_22_23.Data.FirebaseDb
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

    private var dlg: AlertDialog? = null
    private var ConnectedPlayerListView : ListView? = null

    fun showDlg(mpScore: MultiplayerScore) {
        val llh = LinearLayout(this).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.setPadding(50, 50, 50, 50)
            layoutParams = params
            setBackgroundColor(Color.rgb(240, 224, 208))
            orientation = LinearLayout.VERTICAL
            ConnectedPlayerListView = ListView(context).apply {
                val paramsLV = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                layoutParams = paramsLV
                adapter = PlayerScoreAdapter(mpScore.players, context)
            }
            addView(ConnectedPlayerListView)
        }
        dlg = AlertDialog.Builder(this)
            .setTitle("Multiplayer Game")
            .setView(llh)
            .setOnCancelListener {
                finish()
            }
            .create()
        dlg?.show()
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
                    binding.topScores.setOnItemClickListener() { parent, view, pos, id ->
                        showDlg(binding.topScores.adapter.getItem(pos) as MultiplayerScore)
                    }
                }
                FirebaseDb.getScoresTempo().addOnSuccessListener {
                    var scoreList = MultiplayerScore.fromQuery(it)
                    var adapter = MultiplayerScoresAdapter(scoreList,this)
                    binding.TopScores2.adapter = adapter
                    binding.TopScores2.setOnItemClickListener() { parent, view, pos, id ->
                        showDlg(binding.TopScores2.adapter.getItem(pos) as MultiplayerScore)
                    }
                }
            }
        }
    }
}
