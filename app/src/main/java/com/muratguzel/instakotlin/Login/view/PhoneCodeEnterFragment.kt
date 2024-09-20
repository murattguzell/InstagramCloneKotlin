package com.muratguzel.instakotlin.Login.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.muratguzel.instakotlin.Login.viewmodel.AuthViewModel
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.FragmentPhoneCodeEnterBinding
import com.muratguzel.instakotlin.utils.FragmentUtil

class PhoneCodeEnterFragment : Fragment() {
    private var _binding: FragmentPhoneCodeEnterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel



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
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        observeLiveData()


    }
    private fun observeLiveData(){
        // ViewModel'den telefon numarasını gözlemleyin
        viewModel.getPhoneNumber.observe(viewLifecycleOwner) { phoneNumber ->
            Log.d("PhoneCodeEnterFragment", "Phone number from ViewModel: $phoneNumber")
            Toast.makeText(requireContext(), "Gelen veri: $phoneNumber", Toast.LENGTH_SHORT).show()

            binding.tvUserPhoneNo.text = phoneNumber
            viewModel.phoneCodeSent(phoneNumber, requireActivity())

        }
        viewModel.verifyPhoneNumberWithCodeStates.observe(viewLifecycleOwner) { result ->
            if (result) {
                FragmentUtil.replaceFragment(
                    requireActivity().supportFragmentManager,
                    R.id.loginContainer,
                    RegisterFragment(),
                    true,
                    "RegisterFragment"
                )

            }
            viewModel.phoneCodeProgreesBar.observe(viewLifecycleOwner){loading->
                if(loading){
                    binding.pbTelNoOnayla.visibility = View.VISIBLE
                }else{
                    binding.pbTelNoOnayla.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.verificationID.observe(viewLifecycleOwner) { verificationId ->
            verificationId?.let {
                binding.btnPhoneCodeNext.setOnClickListener {
                    val enteredCode = binding.etVerificationCode.text.toString()
                    if (enteredCode.isNotEmpty()) {
                        val credential =
                            PhoneAuthProvider.getCredential(verificationId, enteredCode)
                        viewModel.verifyPhoneNumberWithCode(credential)
                    } else {
                        Toast.makeText(requireContext(), "Lütfen kodu girin.", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}