package com.muratguzel.instakotlin.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muratguzel.instakotlin.databinding.FragmentProfileEditBinding
import com.muratguzel.instakotlin.utils.imageDownload
import com.muratguzel.instakotlin.utils.placeHolderCreate


class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgClose.setOnClickListener {requireActivity().onBackPressedDispatcher.onBackPressed()}
        binding.circleProfileImage.imageDownload(null,
            placeHolderCreate(requireContext()) 
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}