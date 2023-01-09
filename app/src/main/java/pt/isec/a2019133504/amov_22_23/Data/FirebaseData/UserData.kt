package pt.isec.a2019133504.amov_22_23.Data.FirebaseData

import com.google.firebase.firestore.DocumentSnapshot

data class UserData(val Username: String) {
    constructor(doc: DocumentSnapshot) : this(doc.getString("username")!!)
}