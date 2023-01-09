package pt.isec.a2019133504.amov_22_23

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import createFileFromUri
import getTempFilename
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.UserData
import pt.isec.a2019133504.amov_22_23.Data.FirebaseDb
import pt.isec.a2019133504.amov_22_23.databinding.ActivityProfileBinding
import setPic
import java.io.File
import java.io.FileInputStream

class ProfileActivity : AppCompatActivity() {
    private val user = CurrentUser

    lateinit var imageView: ImageView

    private lateinit var binding: ActivityProfileBinding
    private var imagePath : String? = null

    private var permissionsGranted = false

    lateinit var db : FirebaseFirestore
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

        binding.UsernameEdit?.setOnClickListener{
            binding.UsernameEdit!!.text.clear()
        }

        binding.btnCapture.setOnClickListener {
            takePhoto()
        }

        binding.btnGaleria?.setOnClickListener {
            escolhePhoto()
        }

        binding.saveData.setOnClickListener {
            //tentativa
            if(imagePath!=null)
                CurrentUser.imgdata = BitmapFactory.decodeFile(imagePath)
            var texto = binding.UsernameEdit.text
            if(!texto.isNullOrEmpty())
                if(!texto.toString().equals("New Username"))
                    setUserName()
            if (imagePath!=null){
                System.out.println("Path da foto:" + imagePath + "\nUID:" + uid)
                mountainsRef = storageRef.child("images/" + uid + "/")
                val stream = FileInputStream(File(imagePath))
                var uploadTask = mountainsRef.putStream(stream)
            }
        }

        verify_permissions()
        updatePreview()
 //Tratar o voltar para tras
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun escolhePhoto() {
        startActivityForContentResult.launch("image/*")
    }

    var startActivityForContentResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        Log.i(TAG, "startActivityForContentResult: ")
        imagePath = uri?.let { createFileFromUri(this, it) }
        updatePreview()
    }

    private fun verify_permissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
        )
    }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ grantResults ->
        permissionsGranted = grantResults.values.all { it }
    }

    fun updatePreview(){
        if(imagePath != null) {
            setPic(binding.profilephoto, imagePath!!)
        }
    }

    private fun takePhoto() {
        //TODO fazer no save em vez de no take
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

    fun setUserName() {
        val newUsername = binding.UsernameEdit.text.toString()

        FirebaseDb.setUserData(auth.currentUser!!.uid, UserData(newUsername)).addOnSuccessListener {
            CurrentUser.username = newUsername
            binding.UsernameView.text = newUsername
        }
    }

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
            //Toast.makeText(this,"FAILIURE",Toast.LENGTH_SHORT).show()
        }
        FirebaseDb.getUserData(auth.currentUser!!.uid).addOnSuccessListener {
            val userdata = UserData(it)
            CurrentUser.username = userdata.Username
            binding.UsernameView.text = userdata.Username
        }
    }
}
