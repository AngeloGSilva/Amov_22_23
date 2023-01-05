package pt.isec.a2019133504.amov_22_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.databinding.ActivityModo2Binding
import pt.isec.a2019133504.amov_22_23.databinding.ActivityProfileBinding

class Modo2Activity : AppCompatActivity() {
    private val user = CurrentUser
    private lateinit var binding: ActivityModo2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModo2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnServer.setOnClickListener {
            startActivity(GameActivity.getServerModeIntent(this))
        }

        binding.btnClient.setOnClickListener {
            startActivity(GameActivity.getClientModeIntent(this))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}