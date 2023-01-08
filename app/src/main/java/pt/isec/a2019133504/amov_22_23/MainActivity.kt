package pt.isec.a2019133504.amov_22_23

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val user = CurrentUser
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.GearProfile?.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        //TODO alterar para activity modo1 (apenas teste)
        binding.btnMode1.setOnClickListener {

            val intent = Intent(this, Mode1Activity::class.java)
            startActivity(intent)

        }

        binding.btnMode2.setOnClickListener {
            val intent = Intent(this, Modo2Activity::class.java)
            startActivity(intent)
        }

        binding.btnScores.setOnClickListener {
            val intent = Intent(this,ScoreActivity::class.java)
            startActivity(intent)
        }
    }
}
