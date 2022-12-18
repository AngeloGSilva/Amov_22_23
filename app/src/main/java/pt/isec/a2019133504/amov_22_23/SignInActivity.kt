package pt.isec.a2019133504.amov_22_23

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import pt.isec.a2019133504.amov_22_23.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "SIGNINACTICITY"
    }
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private val strEmail get() = binding.edEmail.text.toString()
    private val strPass get() = binding.edPassword.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
    }
    override fun onStart() {
        super.onStart()
        showUser(auth.currentUser)
    }

    fun createUserWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { result ->
                Log.i(TAG, "createUser: success")
                showUser(auth.currentUser)
            }
            .addOnFailureListener(this) { e ->
                Log.i(TAG, "createUser: failure ${e.message}")
                showUser(null)
            }
    }

    fun onSignInEmail(view: View) {
        signInWithEmail(strEmail,strPass)
    }
    fun onLogoutEmail(view: View) {
        signOut()
    }

    fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { result ->
                Log.d(TAG, "signInWithEmail: success")
                showUser(auth.currentUser)
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener(this) { e->
                Log.d(TAG, "signInWithEmail: failure ${e.message}")
                showUser(null)
            }
    }

    fun onCreateAccountEmail(view: View) {
        if(strEmail.isBlank() || strPass.isBlank()) return
        createUserWithEmail(strEmail,strPass)
    }

    fun signOut() {
        if (auth.currentUser != null) {
            auth.signOut()
        }
        showUser(auth.currentUser)
    }

    fun showUser(user : FirebaseUser? = auth.currentUser) {
        val str = when (user) {
            null -> "No authenticated user"
            else -> "User: ${user.email}"
        }
        binding.tvStatus.text = str
        Log.i(TAG,str)
    }
}