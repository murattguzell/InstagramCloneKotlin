package com.muratguzel.instakotlin.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.FragmentCameraBinding
import com.muratguzel.instakotlin.databinding.FragmentHomeBinding
import com.muratguzel.instakotlin.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {
    private var _binding: FragmentMessagesBinding? = null

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
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}