package com.muratguzel.instakotlin.Profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.muratguzel.foodguide.util.imageDownload
import com.muratguzel.foodguide.util.placeHolderCreate
import com.muratguzel.instakotlin.Login.model.Users
import com.muratguzel.instakotlin.Login.viewmodel.AuthViewModel
import com.muratguzel.instakotlin.databinding.FragmentProfileEditBinding


class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var getUser: Users? = null
    private lateinit var mFirestore: FirebaseFirestore
    var updateUserName = false
    var photoUri :Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirestore = Firebase.firestore
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
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        dataSubstitution()
        registerLauncher()
        binding.imgClose.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.changeProfileFoto.setOnClickListener { selectImage() }
        binding.savedCahnged.setOnClickListener { updateProfile() }
    }

    private fun updateProfile() {

        viewModel.userNameControl(binding.etUserName.text.toString())
        viewModel.userNameUsage.observe(viewLifecycleOwner) { userNameActive ->

            Log.e("ProfileEditProfileFragment","$userNameActive")
            if (userNameActive) {
                updateUserName = false
            } else{
                updateUserName = true
            }
            viewModel.updateProfile(
                getUser!!,
                binding.etName.text.toString(),
                binding.etUserName.text.toString(),
                binding.etBiografi.text.toString(),
                binding.etWebSite.text.toString(),
                updateUserName,
                photoUri,
                requireActivity()
            )
        }
    }


    private fun dataSubstitution() {
        viewModel.userGetData()
        viewModel.userGetData.observe(viewLifecycleOwner) { userGetData ->
            if (userGetData != null) {
                getUser = userGetData
                binding.circleImageFragment.imageDownload(
                    userGetData.usersDetail!!.profileImage,
                    placeHolderCreate(requireContext())
                )
                binding.etName.setText(userGetData.nameAndSurName)
                binding.etUserName.setText(userGetData.userName)
                binding.etBiografi.setText(userGetData.usersDetail!!.biograpyh)
                binding.etWebSite.setText(userGetData.usersDetail!!.website)
            }
        }
    }

    private fun selectImage() {
        //Android 33++
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    Snackbar.make(
                        requireView(),
                        "permission needed for gallery",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Give Permission") {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                        }.show()
                } else {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                //go to gallery
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        } else {
            //Android 32 --
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(
                        requireView(),
                        "permission needed for gallery",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Give Permission") {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                        }.show()
                } else {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                //go to gallery
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }
    }

    private fun registerLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    // open gallery
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    Toast.makeText(requireContext(), "İzin verilmedi", Toast.LENGTH_SHORT).show()
                }
            }

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        photoUri = intentFromResult.data
                        binding.circleImageFragment.setImageURI(photoUri)
                    }

                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}