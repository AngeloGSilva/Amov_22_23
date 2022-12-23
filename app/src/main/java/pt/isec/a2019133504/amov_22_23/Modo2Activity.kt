package pt.isec.a2019133504.amov_22_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Modo2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Button>(R.id.btnServer).setOnClickListener {
            startActivity(GameActivity.getServerModeIntent(this))
        }

        findViewById<Button>(R.id.btnClient).setOnClickListener {
            startActivity(GameActivity.getClientModeIntent(this))
        }
    }
}