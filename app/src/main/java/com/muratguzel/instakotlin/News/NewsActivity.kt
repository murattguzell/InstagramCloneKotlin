package com.muratguzel.instakotlin.News

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityHomeBinding
import com.muratguzel.instakotlin.databinding.ActivityNewsBinding
import com.muratguzel.instakotlin.utils.BottomNavigationHelper

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val ACTIVITY_NO = 3
    private val TAG = "NewsActivity"

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

        BottomNavigationHelper.setUpNavigation(this,binding.bottomNavigationView)
        var menu = binding.bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)

    }
}