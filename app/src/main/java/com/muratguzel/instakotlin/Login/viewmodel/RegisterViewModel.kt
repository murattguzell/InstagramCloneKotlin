package com.muratguzel.instakotlin.Login.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    val getPhoneNumber = MutableLiveData<String>()
    val getEmail = MutableLiveData<String>()
    var emailKayit = MutableLiveData<Boolean>()
    var verificationID = MutableLiveData<String>()
    var getcode = MutableLiveData<String>()
    val mAuth: FirebaseAuth = Firebase.auth
    val mRef: DatabaseReference = Firebase.database.reference
    val registerStatus = MutableLiveData<Boolean>()



    fun updateData(phoneNumber: String?, email: String?, verificationId: String?, code: String?) {
        email?.let { getEmail.value = it }
        phoneNumber?.let { getPhoneNumber.value = it }
        verificationId?.let { verificationID.value = it }
        code?.let { getcode.value = it }
    }


    fun registerEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                registerStatus.value = task.isSuccessful
                Log.d("RegisterFragment", "createUserWithEmail: ${if (task.isSuccessful) "success" else "failure"}")
            }
    }

    fun registerPhone(fakeEmail: String, password: String) {
        mAuth.createUserWithEmailAndPassword(fakeEmail, password)
            .addOnCompleteListener { task ->
                registerStatus.value = task.isSuccessful
                Log.d("RegisterFragment", "createUserWithEmail: ${if (task.isSuccessful) "success" else "failure"}")
            }
    }

}

