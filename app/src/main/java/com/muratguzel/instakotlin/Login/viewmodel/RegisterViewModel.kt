package com.muratguzel.instakotlin.Login.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.muratguzel.instakotlin.Login.view.RegisterFragment
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.utils.FragmentUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    val getPhoneNumber = MutableLiveData<String>()
    val getEmail = MutableLiveData<String>()
    var emailKayit = MutableLiveData<Boolean>()
    var verificationID = MutableLiveData<String>()
    var getcode = MutableLiveData<String>()
    val mAuth: FirebaseAuth = Firebase.auth
    val mRef: DatabaseReference = Firebase.database.reference
    val registerStatus = MutableLiveData<Boolean>()
    val verifyPhoneNumberWithCodeStates = MutableLiveData<Boolean>()


    fun updateData(phoneNumber: String?, email: String?, verificationId: String?, code: String?) {
        email?.let { getEmail.value = it }
        phoneNumber?.let { getPhoneNumber.value = it }
        verificationId?.let { verificationID.value = it }
        code?.let { getcode.value = it }
    }


    fun registerEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    viewModelScope.launch(Dispatchers.Main) {
                        registerStatus.value = task.isSuccessful
                        Log.d(
                            "RegisterFragment",
                            "createUserWithEmail: ${if (task.isSuccessful) "success" else "failure"}"
                        )
                    }

                }
        }

    }

    fun registerPhone(fakeEmail: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mAuth.createUserWithEmailAndPassword(fakeEmail, password)
                .addOnCompleteListener { task ->
                    viewModelScope.launch(Dispatchers.Main) {
                        registerStatus.value = task.isSuccessful
                        Log.d(
                            "RegisterFragment",
                            "createUserWithEmail: ${if (task.isSuccessful) "success" else "failure"}"
                        )
                    }

                }
        }

    }

    fun phoneCodeSent(phoneNumber: String, activity: Activity) {
        if (phoneNumber.isNotEmpty()) {
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        viewModelScope.launch(Dispatchers.Main) {
                            getcode.value = credential.smsCode
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Log.e("RegisterViewModel", "Verification failed: ${e.localizedMessage}")
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken,
                    ) {
                        viewModelScope.launch (Dispatchers.Main){
                            verificationID.value = verificationId
                        }
                    }
                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } else {
            Log.e("RegisterViewModel", "Phone number is empty")
        }
    }

    fun verifyPhoneNumberWithCode(credential: PhoneAuthCredential) {
        viewModelScope.launch (Dispatchers.Main){
            try {
                verifyPhoneNumberWithCodeStates.value = true
                Log.d("RegisterViewModel", "Verification successful:")
            } catch (e: Exception) {
                verifyPhoneNumberWithCodeStates.value = false
                Log.e("RegisterViewModel", "Verification failed: ${e.localizedMessage}")
            }
        }
    }
}