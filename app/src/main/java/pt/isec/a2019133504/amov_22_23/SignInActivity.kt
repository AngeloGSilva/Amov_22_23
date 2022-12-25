package pt.isec.a2019133504.amov_22_23

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.a2019133504.amov_22_23.Data.Perfil
import pt.isec.a2019133504.amov_22_23.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    companion object{
        private var TAG = ""
        lateinit var perfil : Perfil
    }
    lateinit var db : FirebaseFirestore
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private val strEmail get() = binding.edEmail.text.toString()
    private val strPass get() = binding.edPassword.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        setContentView(binding.root)
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
                perfil = Perfil("Eu",email)
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
                //TODO USERNAME
                perfil = Perfil("Eu",email)
                val photoUser = hashMapOf(
                    "Photo" to null,
                )
                db.collection("UserData").document(email).set(photoUser)
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
}