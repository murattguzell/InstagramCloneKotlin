package com.muratguzel.instakotlin.Login.viewmodel

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.muratguzel.instakotlin.Login.model.UserDetails
import com.muratguzel.instakotlin.Login.model.Users
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    val getPhoneNumber = MutableLiveData<String>()
    val getEmail = MutableLiveData<String>()
    var emailKayit = MutableLiveData<Boolean>()
    var verificationID = MutableLiveData<String>()
    var getcode = MutableLiveData<String>()
    val mAuth: FirebaseAuth = Firebase.auth
    val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val registerStatus = MutableLiveData<Boolean>()
    val verifyPhoneNumberWithCodeStates = MutableLiveData<Boolean>()
    val progressBar = MutableLiveData<Boolean>()
    val phoneCodeProgreesBar = MutableLiveData<Boolean>()
    val emailUsage = SingleLiveEvent<Boolean>()
    val phoneUsage = SingleLiveEvent<Boolean>()
    val userNameUsage = SingleLiveEvent<Boolean>()
    var loginStates = MutableLiveData<Boolean>()
    var userGetData = MutableLiveData<Users>()
    val mStorage :FirebaseStorage= Firebase.storage


    fun updateData(phoneNumber: String?, email: String?, verificationId: String?, code: String?) {
        email?.let { getEmail.value = it }
        phoneNumber?.let { getPhoneNumber.value = it }
        verificationId?.let { verificationID.value = it }
        code?.let { getcode.value = it }
    }


    fun registerEmail(email: String, password: String, userName: String, nameAndSurName: String) {
        progressBar.value = true
        viewModelScope.launch(Dispatchers.IO) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar.value = false
                    if (task.isSuccessful) {
                        registerStatus.value = true
                        Log.d(
                            "RegisterFragment",
                            "createUserWithEmail: ${if (task.isSuccessful) "success" else "failure"}"

                        )

                        val userId = mAuth.currentUser?.uid
                        if (userId != null) {
                            val savedUsersDetails = UserDetails("", "", "", "0", "0", "0")
                            val savedUsers =
                                Users(
                                    email,
                                    password,
                                    userName,
                                    nameAndSurName,
                                    "",
                                    "",
                                    userId,
                                    savedUsersDetails
                                )
                            // We save the logged in user's data to the database
                            // Firestore'da "users" koleksiyonu altında userId ile belge oluşturuyoruz
                            mFirestore.collection("users").document(userId).set(savedUsers)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Toast.makeText(
                                            getApplication(),
                                            "Veri Kayıt Başarılı",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            getApplication(),
                                            "Veri Kayıt Başarısız",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                .addOnFailureListener { dbException ->
                                    Log.e(
                                        "hata",
                                        "Veri kaydedilemedi: ${dbException.localizedMessage}"
                                    )
                                    mAuth.currentUser!!.delete()
                                        .addOnCompleteListener { deleteTask ->
                                            if (deleteTask.isSuccessful) {
                                                registerStatus.value = false
                                                Toast.makeText(
                                                    getApplication(),
                                                    "kişi kaydedilemedi",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        }
                                }

                        }


                    } else {

                        progressBar.value = false
                        registerStatus.value = false
                    }


                }.addOnFailureListener { authException ->
                    registerStatus.value = false
                    Log.e(
                        "hata",
                        "kişi kaydedilemedi: ${authException.localizedMessage}"
                    )
                }
        }
    }

    fun registerPhone(
        fakeEmail: String,
        password: String,
        userName: String,
        nameAndSurName: String,
    ) {
        progressBar.value = true
        viewModelScope.launch(Dispatchers.IO) {
            mAuth.createUserWithEmailAndPassword(fakeEmail, password)
                .addOnCompleteListener { authtask ->
                    if (authtask.isSuccessful) {
                        progressBar.value = false
                        registerStatus.value = true
                        Log.d(
                            "RegisterFragment",
                            "createUserWithEmail: ${if (authtask.isSuccessful) "success" else "failure"}"
                        )
                        // Firestore'da "users" koleksiyonu altında userId ile belge oluşturuyoruz

                        val userId = mAuth.currentUser?.uid
                        if (userId != null) {
                            Log.e("phone", "${getPhoneNumber.value}")
                            val savedUsersDetails = UserDetails("", "", "", "0", "0", "0")

                            val savedUsers = Users(
                                "",
                                password,
                                userName,
                                nameAndSurName,
                                getPhoneNumber.value,
                                fakeEmail,
                                userId,
                                savedUsersDetails
                            )

                            // Firestore'da "users" koleksiyonu altında userId ile belge oluşturuyoruz
                            mFirestore.collection("users").document(userId).set(savedUsers)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Toast.makeText(
                                            getApplication(),
                                            "Veri Kayıt Başarılı",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            getApplication(),
                                            "Veri Kayıt Başarısız",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                .addOnFailureListener { dbException ->
                                    Log.e(
                                        "hata",
                                        "Veri kaydedilemedi: ${dbException.localizedMessage}"
                                    )
                                    mAuth.currentUser!!.delete()
                                        .addOnCompleteListener { deleteTask ->
                                            registerStatus.value = false
                                            Toast.makeText(
                                                getApplication(),
                                                "kişi kaydedilemedi",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }

                        }

                    } else {
                        progressBar.value = false

                        registerStatus.value = false
                    }


                }.addOnFailureListener { authException ->
                    registerStatus.value = false
                    progressBar.value = false

                    Log.e(
                        "hata",
                        "kişi kaydedilemedi: ${authException.localizedMessage}"
                    )
                }
        }

    }


    fun emailControl(email: String) {
        Log.d("EmailControl", "Sorgu başlatıldı: $email")  // Her seferinde sorgu başlarken logla
        mFirestore.collection("users").get().addOnSuccessListener { result ->
            var isEmailUsed = false
            if (result != null) {
                for (user in result.documents) {
                    val okunanKullanici = user.toObject(Users::class.java)
                    if (okunanKullanici != null && okunanKullanici.email == email) {
                        isEmailUsed = true
                        Log.d("EmailControl", "Email bulundu: ${okunanKullanici.email}")
                        break
                    }
                }
            }
            emailUsage.setValue(isEmailUsed)
            Log.d("EmailControl", "Firestore sorgusu tamamlandı, email kullanımı: $isEmailUsed")
        }.addOnFailureListener { exception ->
            Log.e("FirestoreError", "Error getting documents: ", exception)

        }
    }

    fun phoneControl(phoneNumber: String) {
        mFirestore.collection("users").get().addOnSuccessListener { result ->
            var phoneActive = false
            if (result != null) {
                for (user in result.documents) {
                    val okunanKullanici = user.toObject(Users::class.java)
                    if (okunanKullanici != null && okunanKullanici.phoneNumber == phoneNumber) {
                        phoneActive = true
                        Log.d("PhoneControl", "Phone number bulundu: ${okunanKullanici.email}")
                        break
                    }
                }
            }
            phoneUsage.setValue(phoneActive)
        }.addOnFailureListener { exception ->
            Log.e("FirestoreError", "Error getting documents: ", exception)
        }
    }

    fun userNameControl(userName: String) {
        mFirestore.collection("users").get().addOnSuccessListener { result ->
            var userNameActive = false
            if (result != null) {
                for (user in result.documents) {
                    val okunanKullanici = user.toObject(Users::class.java)
                    if (okunanKullanici != null && okunanKullanici.userName == userName) {
                        userNameActive = true
                        Log.d(
                            "UserNameControl",
                            "User name kullanılıyor: ${okunanKullanici.userName}"
                        )
                        break
                    }
                }
            }
            userNameUsage.setValue(userNameActive)

        }.addOnFailureListener { exception ->
            Log.e("FirestoreError", "Error getting documents: ", exception)
        }
    }


    fun phoneCodeSent(phoneNumber: String, activity: Activity) {
        if (phoneNumber.isNotEmpty()) {
            phoneCodeProgreesBar.value = true
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        viewModelScope.launch(Dispatchers.Main) {
                            phoneCodeProgreesBar.value = false
                            getcode.value = credential.smsCode
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        phoneCodeProgreesBar.value = false

                        Log.e("RegisterViewModel", "Verification failed: ${e.localizedMessage}")
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken,
                    ) {
                        viewModelScope.launch(Dispatchers.Main) {
                            phoneCodeProgreesBar.value = false
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
        viewModelScope.launch(Dispatchers.Main) {
            try {
                phoneCodeProgreesBar.value = false
                verifyPhoneNumberWithCodeStates.value = true
                Log.d("RegisterViewModel", "Verification successful:")
            } catch (e: Exception) {
                phoneCodeProgreesBar.value = false
                verifyPhoneNumberWithCodeStates.value = false
                Log.e("RegisterViewModel", "Verification failed: ${e.localizedMessage}")
            }
        }
    }

    fun signInControl(inputmethod: String, password: String) {
        var kullaniciBulundu = false
        mFirestore.collection("users").orderBy("email").addSnapshotListener { value, error ->
            if (value != null) {
                for (user in value.documents) {
                    val okunanKullanici = user.toObject(Users::class.java)
                    if (okunanKullanici!!.userName.toString() == inputmethod) {
                        signIn(okunanKullanici, password, false)
                        kullaniciBulundu = true
                        break
                    } else if (okunanKullanici.email.toString() == inputmethod) {
                        signIn(okunanKullanici, password, false)
                        kullaniciBulundu = true

                        break

                    } else if (okunanKullanici.phoneNumber.toString() == inputmethod) {
                        signIn(okunanKullanici, password, true)
                        kullaniciBulundu = true
                        break
                    }
                }
            }
            if (kullaniciBulundu == false) {
                Toast.makeText(getApplication(), "kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun userGetData() {
        mFirestore.collection("users").document(mAuth.currentUser!!.uid)
            .addSnapshotListener { value, error ->
                val okunanKullaniciBilgileri = value?.toObject(Users::class.java)
                if (okunanKullaniciBilgileri != null) {
                    userGetData.value = okunanKullaniciBilgileri!!
                }
            }
    }


    fun updateProfile(
        user: Users,
        nameAndSurName: String,
        userName: String,
        biograpyh: String,
        website: String,
        updateUserName: Boolean,
        profilPhotoUri: Uri?,
        context: Context
    ) {
        var prfileUpdate = false
        Log.e("RegisterViewModel", "$user")
        if (!user.nameAndSurName!!.equals(nameAndSurName)) {
            mFirestore.collection("users").document(user.userId!!)
                .update("nameAndSurName", nameAndSurName)
            prfileUpdate = true
        }

        if (!user.usersDetail!!.biograpyh!!.equals(biograpyh)) {
            mFirestore.collection("users").document(user.userId!!)
                .update("usersDetail.biograpyh", biograpyh)
            prfileUpdate = true
        }
        if (!user.usersDetail!!.website!!.equals(website)) {
            mFirestore.collection("users").document(user.userId!!)
                .update("usersDetail.website", website)
            prfileUpdate = true
        }
        if (!user.userName!!.equals(userName)) {
            if (updateUserName) {
                mFirestore.collection("users").document(user.userId!!).update("userName", userName)
                prfileUpdate = true
            } else {
                prfileUpdate = false
                Toast.makeText(getApplication(), "Bu kullanıcı adı alınmış", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        //profile image upload
        if (profilPhotoUri!=null){
            val dialogBinding = LayoutInflater.from(getApplication()).inflate(R.layout.profile_foto_update_dialog,null)
            val myDialog = Dialog(context)
            myDialog.setContentView(dialogBinding)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

        var uploadTask = mStorage.reference.child("users").child(userGetData.value!!.userId!!).child(profilPhotoUri.lastPathSegment!!).putFile(profilPhotoUri)
            .addOnCompleteListener { result->
                if (result.isSuccessful){
                    Toast.makeText(getApplication(),"Resim Yüklendii ${result.result}",Toast.LENGTH_SHORT).show()
                    prfileUpdate = true
                    myDialog.dismiss()
                    mFirestore.collection("users").document(userGetData!!.value!!.userId!!).update("usersDetail.profileImage",profilPhotoUri)

                }
            }.addOnFailureListener { exception->
                Log.e("RegisterViewModel",exception.localizedMessage!!)
            }
        }

        if (prfileUpdate) {

            Toast.makeText(getApplication(), "kullanıcı bilgileri güncellendi", Toast.LENGTH_SHORT)
                .show()

        }
    }

    private fun signIn(okunanKullanici: Users, password: String, phoneNumberSignIn: Boolean) {
        var girisYapcakEmail = ""
        if (phoneNumberSignIn) {
            girisYapcakEmail = okunanKullanici.emailPhoneNumber!!.toString()
        } else {
            girisYapcakEmail = okunanKullanici.email!!.toString()
        }
        mAuth.signInWithEmailAndPassword(girisYapcakEmail, password)
            .addOnCompleteListener { mAuthResult ->
                if (mAuthResult.isSuccessful) {
                    loginStates.value = true
                    Toast.makeText(
                        getApplication(),
                        "kullanıcı başarıyla giriş yaptı",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loginStates.value = false
                    Toast.makeText(
                        getApplication(),
                        "kullanıcı adı,Telefon numarası, email veya şifre hatalı",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener { exception ->
                Log.e(
                    "RegisterViewModel",
                    "kullanıcı giriş yapamadı: ${exception.localizedMessage}"
                )
            }

    }
}
