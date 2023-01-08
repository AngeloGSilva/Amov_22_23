package pt.isec.a2019133504.amov_22_23

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import pt.isec.a2019133504.amov_22_23.Data.CurrentUser
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.UserData
import pt.isec.a2019133504.amov_22_23.Data.FirebaseDb
import pt.isec.a2019133504.amov_22_23.databinding.ActivitySignInBinding
import java.io.ByteArrayOutputStream
import java.io.File


class SignInActivity : AppCompatActivity() {
    private val user = CurrentUser
    val STRING_LENGTH = 10
    lateinit var db : FirebaseFirestore
    private lateinit var binding: ActivitySignInBinding
    //private lateinit var auth: FirebaseAuth
    private val strEmail get() = binding.edEmail.text.toString()
    private val strPass get() = binding.edPassword.text.toString()
    private val auth = FirebaseAuth.getInstance()
    private var TAG = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        db = Firebase.firestore
        setContentView(binding.root)

        binding.btndebug1.setOnClickListener {
            signInWithEmail("eueu@gmail.com","eueueu")
        }
        binding.btndebug2.setOnClickListener {
            signInWithEmail("adeusadeus@gmail.com","adeusadeus")
        }


    }

    fun checkphotoexists(){
        val storageref = FirebaseStorage.getInstance().reference.child("images/${auth.currentUser?.uid}")
        val localfile = File.createTempFile("tempfile",".jpg",this.cacheDir)
        storageref.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            //ProfileActivity.imgdata = getImageUri(this,bitmap)
            CurrentUser.imgdata = bitmap
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            null,
            null
        )
        return Uri.parse(path)
    }

    override fun onStart() {
        super.onStart()
    }

    fun showStatus() {
        binding.tvStatus.text = TAG
    }

    fun signOut() {
        if (auth.currentUser != null) {
            auth.signOut()
        }

    }

    fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { result ->
                checkphotoexists()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener(this) { e->
                TAG = "Login Falhou"
                showStatus()
            }
    }

    fun createUserWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { result ->
                FirebaseDb.setUserData(result.user!!.uid, UserData("User_" + getRandomString(STRING_LENGTH)))
                checkphotoexists()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener(this) { e ->
                TAG = "Registo Falhou"
                showStatus()
            }
    }
    fun onSignInEmail(view: View) {
        if(strEmail.isBlank() || strPass.isBlank()){
            TAG = "Preencha os campos"
            showStatus()
            return
        }
        signInWithEmail(strEmail,strPass)
    }

    fun onCreateAccountEmail(view: View) {
        if(strEmail.isBlank() || strPass.isBlank()) {
            TAG = "Preencha os campos"
            showStatus()
            return
        }
        createUserWithEmail(strEmail,strPass)
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}