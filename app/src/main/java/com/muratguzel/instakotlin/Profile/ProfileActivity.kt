package com.muratguzel.instakotlin.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muratguzel.foodguide.util.imageDownload
import com.muratguzel.foodguide.util.placeHolderCreate
import com.muratguzel.instakotlin.Login.viewmodel.AuthViewModel
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityProfileBinding
import com.muratguzel.instakotlin.utils.BottomNavigationHelper


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"
    val viewModel : AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpToolBar()
        setTextData()
        setUpBottomNavigationView()
        handleBackPress()
        binding.tvEditProfile.setOnClickListener {
            fragmentNavigation()
        }
    }


    private fun fragmentNavigation() {
        binding.profileRoot.visibility = View.GONE
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.profileContainer, ProfileEditFragment())
        transaction.addToBackStack("ProfileEditFragment")
        transaction.commit()
    }

    private fun setUpBottomNavigationView() {
        BottomNavigationHelper.setUpNavigation(this, binding.bottomNavigationView)
        var menu = binding.bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    private fun setUpToolBar() {
        binding.imgProfileSettings.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }


    private fun setTextData(){
        viewModel.userGetData()
        viewModel.userGetData.observe(this){userGetData->
            if (userGetData!=null){
                binding.tvUserNameToolBar.text = userGetData.userName
                binding.tvUserName.text = userGetData.nameAndSurName
                binding.tvPostNumber.text = userGetData.usersDetail!!.post
                binding.tvFollowerNumber.text = userGetData.usersDetail!!.follower
                binding.tvFollowUpNumber.text = userGetData.usersDetail!!.following
                binding.tvBiografi.text = userGetData.usersDetail!!.biograpyh
                binding.tvWebsite.text = userGetData.usersDetail!!.website
                val imgUrl:String = userGetData.usersDetail!!.profileImage!!
                Log.e("ProfileAcivity","$imgUrl")
                binding.circleImage.imageDownload(imgUrl,
                    placeHolderCreate(this))

                if (userGetData.usersDetail!!.biograpyh!= null){
                    binding.tvBiografi.visibility = View.VISIBLE
                    binding.tvBiografi.text = userGetData.usersDetail!!.biograpyh
                }
                if (userGetData.usersDetail!!.website!= null){
                    binding.tvWebsite.visibility = View.VISIBLE
                    binding.tvWebsite.text = userGetData.usersDetail!!.website
                }
            }
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.profileRoot.visibility = View.VISIBLE
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                    overridePendingTransition(0,0)
                }
            }
        })
    }
}
