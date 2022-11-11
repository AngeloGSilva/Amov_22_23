package pt.isec.a2019133504.amov_22_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.a2019133504.amov_22_23.databinding.ActivityCameraBinding
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMainBinding
import pt.isec.a2019133504.amov_22_23.databinding.ActivityMode1Binding

class Mode1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMode1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }




}