package pt.isec.a2019133504.amov_22_23.Data

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth

object CurrentUser {
    private val auth = FirebaseAuth.getInstance()

    val email : String?
        get() = auth.currentUser?.email

    val uid : String?
        get() = auth.currentUser?.uid

    var username : String = ""

    var imgdata : Bitmap? = null
}