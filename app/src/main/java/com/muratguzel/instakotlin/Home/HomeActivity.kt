package com.muratguzel.instakotlin.Home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityHomeBinding
import com.muratguzel.instakotlin.utils.BottomNavigationHelper
import com.muratguzel.instakotlin.utils.HomePagerAdapter

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpBottomNavigationView()
        setUpViewPager()
    }
    private fun setUpBottomNavigationView(){
        BottomNavigationHelper.setUpNavigation(this,binding.bottomNavigationView)
        var menu = binding.bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    fun setUpViewPager(){
        var homePagerAdapter = HomePagerAdapter(this)
        homePagerAdapter.addFragment(CameraFragment())
        homePagerAdapter.addFragment(HomeFragment())
        homePagerAdapter.addFragment(MessagesFragment())
        binding.homeViewPager.adapter = homePagerAdapter
        binding.homeViewPager.setCurrentItem(1,false)
    }
}