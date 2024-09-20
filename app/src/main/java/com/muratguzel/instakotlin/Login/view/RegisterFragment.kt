package com.muratguzel.instakotlin.Login.view

import SetUpBtnBackgroundViewUtil
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.muratguzel.instakotlin.Home.HomeActivity
import com.muratguzel.instakotlin.Login.viewmodel.AuthViewModel
import com.muratguzel.instakotlin.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    var gettelNo = ""
    var getverificationID = ""
    var getemail = ""
    var getcode = ""
    var emailclick = false
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
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
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        observeLiveData()

        binding.tvSignIn.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity (intent)
        }
        binding.btnNextRegisterFragment.setOnClickListener {

            viewModel.userNameControl(binding.etUserName.text.toString())
            viewModel.userNameUsage.observe(viewLifecycleOwner) { userNameUsage ->
                if (userNameUsage) {
                    Toast.makeText(
                        requireContext(),
                        "Kullanıcı adı kullanılıyor",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //kullanici email ile kayıt oluyor
                    if (emailclick) {
                        Log.d("RegisterFragment", "Email kayıt durumu: $getemail")
                        var userName = binding.etUserName.text.toString()
                        var nameAndSurName = binding.etNameAndSurName.text.toString()
                        var email = getemail
                        var password = binding.etPassword.text.toString()
                        Log.d("RegisterFragment", "şifre $password")
                        viewModel.registerEmail(email, password, userName, nameAndSurName)
                    } else {
                        //kullanıcı telefonuyla kayıt oluyor
                        var fakeEmail = "$gettelNo@gmail.com"
                        var userName = binding.etUserName.text.toString()
                        var nameAndSurName = binding.etNameAndSurName.text.toString()
                        var password = binding.etPassword.text.toString()
                        Log.d("RegisterFragment", "şifre $password")
                        viewModel.registerPhone(fakeEmail, password, userName, nameAndSurName)
                    }


                }
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
                val intent = Intent(requireActivity(),HomeActivity::class.java)
                requireActivity().finish()
                startActivity(intent)
                requireActivity().overridePendingTransition(0,0)
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
        viewModel.progressBar.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}