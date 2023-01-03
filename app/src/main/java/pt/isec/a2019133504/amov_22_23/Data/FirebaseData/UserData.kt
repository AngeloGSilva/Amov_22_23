package pt.isec.a2019133504.amov_22_23.Data.FirebaseData

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class UserData(val Username: String) {
    fun getMap() : HashMap<String, Any?> {
        return hashMapOf(
            UserData.Username to Username,
        )
    }

    constructor(document : DocumentSnapshot) : this(document.getString(UserData.Username)!!)

    companion object {
        val Collection = "UserData"
        val Username = "Username"
    }
}