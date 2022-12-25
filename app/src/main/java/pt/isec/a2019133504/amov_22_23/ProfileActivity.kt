package pt.isec.a2019133504.amov_22_23

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import createFileFromUri
import getTempFilename
import pt.isec.a2019133504.amov_22_23.databinding.ActivityProfileBinding
import setPic
import java.io.File
import java.io.FileInputStream
import java.util.UUID

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

    private lateinit var auth : FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef :StorageReference
    private lateinit var mountainsRef :StorageReference
    lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = findViewById(R.id.profilephoto)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid!!
        db = Firebase.firestore

        storage = Firebase.storage
        storageRef = storage.reference

        checkUserPhoto()

        binding.btnCapture.setOnClickListener {
            takePhoto()
        }

        binding.btnGaleria?.setOnClickListener {
            escolhePhoto()
        }

        binding.editprofile.setOnClickListener {

        }

        binding.saveData?.setOnClickListener {
            //tentativa
            System.out.println("Path da foto:" + imagePath+ "\nUID:"+uid )
            mountainsRef = storageRef.child("images/"+uid+"/")
            val stream = FileInputStream(File(imagePath))
            var uploadTask = mountainsRef.putStream(stream)
        }

        verify_permissions()
        updatePreview()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun escolhePhoto() {
        Log.i(TAG, "chooseImage_v3: ")
        startActivityForContentResult.launch("image/*")
    }

    // para a v3
    var startActivityForContentResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        Log.i(TAG, "startActivityForContentResult: ")
        imagePath = uri?.let { createFileFromUri(this, it) }
        updatePreview()
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
    /*override fun onResume() {
       // imageView.setImageURI(imgdata)
        super.onResume()
    }*/

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
        val progressdialog = ProgressDialog(this)
        progressdialog.setMessage("Fetching Data..")
        progressdialog.setCancelable(false)
        progressdialog.show()

        val storageref = FirebaseStorage.getInstance().reference.child("images/$uid")
        val localfile = File.createTempFile("tempfile","jpg")
        storageref.getFile(localfile).addOnSuccessListener {
            if(progressdialog.isShowing)
                progressdialog.dismiss()
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            imageView.setImageBitmap(bitmap)
        }.addOnFailureListener{
            if(progressdialog.isShowing)
                progressdialog.dismiss()
            Toast.makeText(this,"FAILIURE",Toast.LENGTH_SHORT).show()
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageView.setImageURI(data?.data)
            //binding.profilephoto.setImageURI(data?.data)
            //imgdata = data?.data
        }
    }*/

}