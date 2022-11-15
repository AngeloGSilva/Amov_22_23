package pt.isec.a2019133504.amov_22_23

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.a2019133504.amov_22_23.Data.Perfil
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.MainLogo.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }
        //TODO alterar para activity modo1 (apenas teste)
        binding.btnMode1.setOnClickListener {
            /*var perfil : Perfil = intent.getSerializableExtra("profileUser") as Perfil

            binding.MainLogo.setImageURI(Uri.parse(perfil.imgdata))
            binding.btnMode1.text = perfil.nomestr*/
            val intent = Intent(this,Mode1Activity::class.java)
            startActivity(intent)



        }
    }
}