package com.muratguzel.instakotlin.Login.view

import SetUpBtnBackgroundViewUtil
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.muratguzel.instakotlin.Login.viewmodel.RegisterViewModel
import com.muratguzel.instakotlin.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel
    var gettelNo = ""
    var getverificationID = ""
    var getemail = ""
    var getcode = ""
    var emailclick = false
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
        mRef = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        if (mAuth.currentUser != null) {
            mAuth.signOut()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
        observeLiveData()

        binding.btnNextRegisterFragment.setOnClickListener {
            //kullanici email ile kayıt oluyor
            if (emailclick) {
                Log.d("RegisterFragment", "Email kayıt durumu: $getemail")
                var email = getemail
                var password = binding.etPassword.text.toString()
                Log.d("RegisterFragment", "şifre $password")
                viewModel.registerEmail(email, password)
            } else {
                //kullanıcı telefonuyla kayıt oluyor
                var fakeEmail = "$gettelNo@gmail.com"
                var password = binding.etPassword.text.toString()
                Log.d("RegisterFragment", "şifre $password")
                viewModel.registerPhone(fakeEmail, password)
            }
        }

        SetUpBtnBackgroundViewUtil.setUpOnTextChange(binding, requireContext())
    }

    private fun observeLiveData() {
        viewModel.emailKayit.observe(viewLifecycleOwner) { emailKayit ->
            Log.e("emailKayit", emailKayit.toString())
            if (emailKayit) {
                emailclick = true
                Log.e("emailClick", emailclick.toString())

                viewModel.getEmail.observe(viewLifecycleOwner) { email ->
                    email?.let {
                        getemail = email

                    }
                }
            } else {
                emailclick = false
                Log.e("emailClick", emailclick.toString())
                viewModel.getPhoneNumber.observe(viewLifecycleOwner) { phoneNumber ->
                    phoneNumber?.let {
                        gettelNo = phoneNumber

                    }
                    viewModel.verificationID.observe(viewLifecycleOwner) { verificationId ->
                        verificationId?.let {
                            getverificationID = verificationId

                        }

                    }
                    viewModel.getcode.observe(viewLifecycleOwner) { code ->
                        code?.let {
                            getcode = code
                        }

                    }

                }
            }
        }
        viewModel.registerStatus.observe(viewLifecycleOwner) { registerStatus ->
            if (registerStatus) {
                Snackbar.make(
                    binding.btnNextRegisterFragment,
                    "Kayıt Başarılı",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    binding.btnNextRegisterFragment,
                    "Kayıt Başarısız",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }

    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}