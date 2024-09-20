package com.muratguzel.instakotlin.Profile


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.muratguzel.instakotlin.Login.view.RegisterActivity
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

            alertDialog().show()
        }
    }

    fun alertDialog(): AlertDialog {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Instagram'dan Çıkış Yap")
            .setMessage("Emin Misiniz?")
            .setPositiveButton("Çıkış Yap") { dialog, which ->
                Firebase.auth.signOut()
                val intent = Intent(this, RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                finish()

            }
            .setNegativeButton("İptal") { dialog, which ->
            }.create()
        return alertDialog
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