package pt.isec.a2019133504.amov_22_23

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import pt.isec.a2019133504.amov_22_23.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    /*lateinit var button: Button
    lateinit var buttonLogin : Button
    lateinit var editarperfil : Button
    lateinit var playedMatches1 : TextView
    lateinit var playedMatches2 : TextView*/

    private val pickImage = 100
    companion object {
/*        var nomestr: String = ""
        var emailstr : String = ""*/
        var imgdata : Uri? = null
/*        var nrgames : Long = 0
        var nrwins : Long = 0
        var nrgames2 : Long = 0
        var nrwins2 : Long = 0*/
    }


    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = findViewById(R.id.profilephoto)
        binding.btnCapture.setOnClickListener {
            //val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            //startActivityForResult(gallery, pickImage)
            val intent = Intent(this,CameraActivity::class.java)
            startActivity(intent)
        }
        binding.editprofile.setOnClickListener {
            //test to edit the background of snackbar
/*            Snackbar.make(binding.root,"ola", Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.principalVariante))
                .show()*/
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    //update imageview after taking an photo
    override fun onResume() {
        //binding.profilephoto.setImageURI(imgdata)
        imageView.setImageURI(imgdata)
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageView.setImageURI(data?.data)
            //binding.profilephoto.setImageURI(data?.data)
            //imgdata = data?.data
        }
    }

}