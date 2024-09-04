package com.muratguzel.instakotlin.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.FragmentHomeBinding
import com.muratguzel.instakotlin.databinding.FragmentPhoneCodeEnterBinding

class PhoneCodeEnterFragment : Fragment() {
    private var _binding: FragmentPhoneCodeEnterBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewmodel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPhoneCodeEnterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
        viewmodel.inputData.observe(viewLifecycleOwner) { inputData ->
            Toast.makeText(requireContext(), "Gelen veri: $inputData", Toast.LENGTH_SHORT).show()
            binding.phoneCodeEnter.text = inputData
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}