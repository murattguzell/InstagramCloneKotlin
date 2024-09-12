package com.muratguzel.instakotlin.Login.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.muratguzel.instakotlin.Login.viewmodel.RegisterViewModel
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.FragmentPhoneCodeEnterBinding
import java.util.concurrent.TimeUnit

class PhoneCodeEnterFragment : Fragment() {
    private var _binding: FragmentPhoneCodeEnterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel
    private lateinit var auth: FirebaseAuth
    private var getPhoneNumber = ""
    private var verificationID = ""
    var getCode = ""
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var isFragmentAttached = false  // Fragment'in bağlı olup olmadığını takip etmek için

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isFragmentAttached = true  // Fragment bağlandığında true yap
    }

    override fun onDetach() {
        super.onDetach()
        isFragmentAttached = false  // Fragment ayrıldığında false yap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setUpCallBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPhoneCodeEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pbTelNoOnayla.visibility = View.INVISIBLE
        viewModel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]

        // ViewModel'den telefon numarasını gözlemleyin
        viewModel.getPhoneNumber.observe(viewLifecycleOwner) { inputData ->
            Log.d("PhoneCodeEnterFragment", "Phone number from ViewModel: $inputData")
            Toast.makeText(requireContext(), "Gelen veri: $inputData", Toast.LENGTH_SHORT).show()
            getPhoneNumber = inputData
            binding.tvUserPhoneNo.text = getPhoneNumber

            if (getPhoneNumber.isNotEmpty()) {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(getPhoneNumber) // Doğrulama yapılacak telefon numarası
                    .setTimeout(60L, TimeUnit.SECONDS) // Zaman aşımı süresi ve birimi
                    .setActivity(requireActivity()) // Activity (geri çağırma bağlama için)
                    .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
                Log.d(
                    "PhoneCodeEnterFragment",
                    "Verification started for phone number: $getPhoneNumber"
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Lütfen geçerli bir telefon numarası girin.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnPhoneCodeNext.setOnClickListener {
            val enteredCode = binding.etVerificationCode.text.toString()
            Log.d("PhoneCodeEnterFragment", "Entered code: $enteredCode")
            Log.d("PhoneCodeEnterFragment", "Stored code: $getCode")

            if (enteredCode.isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(verificationID, enteredCode)
                verifyPhoneNumberWithCode(credential)
            } else {
                Toast.makeText(requireContext(), "Lütfen kodu girin.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpCallBack() {
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                binding.pbTelNoOnayla.visibility = View.INVISIBLE
                Log.d("PhoneCodeEnterFragment", "onVerificationCompleted: $credential")
                // Otomatik tamamlama veya anında doğrulama
                if (!credential.smsCode.isNullOrEmpty()) {
                    getCode = credential.smsCode!!
                    Log.d(
                        "PhoneCodeEnterFragment",
                        "SMS Code received in onVerificationCompleted: $getCode"
                    )
                    // İsterseniz burada hemen doğrulama yapabilirsiniz
                } else {
                    Log.d(
                        "PhoneCodeEnterFragment",
                        "No SMS code received in onVerificationCompleted"
                    )
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (isFragmentAttached) {
                    binding.pbTelNoOnayla.visibility = View.INVISIBLE
                    Log.e("PhoneCodeEnterFragment", "Verification failed: ${e.localizedMessage}")
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(
                        "PhoneCodeEnterFragment",
                        "Fragment is not attached, cannot show error message."
                    )
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                if (isFragmentAttached) {
                    Log.d("PhoneCodeEnterFragment", "Code sent, verification ID: $verificationId")
                    verificationID = verificationId
                    forceResendingToken = token
                    Toast.makeText(requireContext(), "Kod gönderildi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verifyPhoneNumberWithCode(credential: PhoneAuthCredential) {
        // signInWithCredential metodunu kaldırıyoruz
        // Bunun yerine, sadece doğrulama başarılı olduktan sonra işlem yapabilirsiniz.
        if (isFragmentAttached) {
            Log.d(
                "PhoneCodeEnterFragment",
                "Kod doğrulandı! Kayıt yapılmadan işlem gerçekleştirildi."
            )
            Toast.makeText(requireContext(), "Kod doğrulandı!", Toast.LENGTH_SHORT).show()

            // Kullanıcı kaydedilmeden işlem yapmak istediğiniz kod buraya gelebilir
            // Örneğin, kullanıcıyı yeni bir ekrana yönlendirebilirsiniz
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.loginContainer, RegisterFragment())
            transaction.addToBackStack("registerFragment")
            transaction.commit()
        }
    }

}