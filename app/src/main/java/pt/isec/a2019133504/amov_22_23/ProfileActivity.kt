package pt.isec.a2019133504.amov_22_23

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.FirebaseFirestoreKtxRegistrar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import getTempFilename
import pt.isec.a2019133504.amov_22_23.Data.Perfil
import pt.isec.a2019133504.amov_22_23.databinding.ActivityProfileBinding
import setPic
import java.io.File

class ProfileActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var db : FirebaseFirestore
    private val pickImage = 100
    companion object {
        var imgdata : Uri? = null
    }

    private lateinit var binding: ActivityProfileBinding
    private var imagePath : String? = null

    private var permissionsGranted = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = findViewById(R.id.profilephoto)

        db = Firebase.firestore

        checkUserPhoto()

        binding.btnCapture.setOnClickListener {
            /*val intent = Intent(this,CameraActivity::class.java)
            startActivity(intent)*/
            takePhoto()
        }
        binding.editprofile.setOnClickListener {

        }
        //TODO alterar para o player mesmo (apenas teste)
       /* binding.realizaLogin.setOnClickListener {
            var perfil = Perfil("Angelo","angelo@emial", imgdata.toString())
            var intent = Intent(this,MainActivity::class.java)
            intent.putExtra("profileUser",perfil as java.io.Serializable)
            startActivity(intent)
        }*/
        verify_permissions()
        updatePreview()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun verify_permissions() {
        /*
        if ( ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsGranted = false
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSIONS_REQUEST_CODE
            )
        }else
            permissionsGranted = true
    */
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ grantResults ->
        permissionsGranted = grantResults.values.all { it }
    }

    fun updatePreview(){
        if(imagePath != null)
            setPic(binding.profilephoto,imagePath!!)
        /*else*/
/*
            binding.frPreview.background = ResourcesCompat.getDrawable(resources,android.R.drawable.ic_menu_report_image,null)
*/
    }

    private fun takePhoto() {
        imagePath = getTempFilename(this)
        startActivityForTakePhotoResult.launch(
            FileProvider.getUriForFile(this,
                "pt.isec.a2019133504.amov_22_23.android.fileprovider",
                File(imagePath) )
        )
    }
    var startActivityForTakePhotoResult = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            updatePreview()}
    }


    //update imageview after taking an photo
    override fun onResume() {
        //binding.profilephoto.setImageURI(imgdata)
        imageView.setImageURI(imgdata)
        super.onResume()
    }

    /*fun addUserToFirestore() {

        val scores = hashMapOf(

        )

        db.collection("Top5Scores").document("Top1").set()
            .addOnSuccessListener {
                Log.i(ContentValues.TAG, "addDataToFirestore: Success")
            }
            .addOnFailureListener { e->
                Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
            }
    }*/

    fun checkUserPhoto(){
        var img = db.collection("UsersData").document(SignInActivity.perfil.emailstr).get()

        if(img !=null){
            imageView.setImageURI(imgdata)
        }
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