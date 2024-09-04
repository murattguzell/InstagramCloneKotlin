package com.muratguzel.instakotlin.Profile


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityProfileSettingBinding
import com.muratguzel.instakotlin.utils.BottomNavigationHelper

class ProfileSettingActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"
    private lateinit var binding: ActivityProfileSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpBottomNavigationView()
        fragmentNavigation()
        handleBackPress()
    }

    private fun fragmentNavigation() {

        binding.tvEditProfileSetting.setOnClickListener {
            binding.profileSettingsRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer, ProfileEditFragment())
            transaction.addToBackStack("ProfileEditFragment")
            transaction.commit()
        }
        binding.tvSignOut.setOnClickListener {
            binding.profileSettingsRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer, SignOutFragment())
            transaction.addToBackStack("SignOutFragment")
            transaction.commit()
        }
    }

    private fun setUpBottomNavigationView() {
        BottomNavigationHelper.setUpNavigation(this, binding.bottomNavigationView)
        var menu = binding.bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }


    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.profileSettingsRoot.visibility = View.VISIBLE
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                    overridePendingTransition(0, 0)
                }
            }
        })
    }
}