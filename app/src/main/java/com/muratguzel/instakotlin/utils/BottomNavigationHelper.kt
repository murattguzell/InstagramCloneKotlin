package com.muratguzel.instakotlin.utils

import android.content.Context
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.muratguzel.instakotlin.Home.HomeActivity
import com.muratguzel.instakotlin.News.NewsActivity
import com.muratguzel.instakotlin.Profile.ProfileActivity
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.Search.SearchActivity
import com.muratguzel.instakotlin.Share.ShareActivity


     object BottomNavigationHelper{

        fun setUpNavigation(context: Context, bottomNavigationView: BottomNavigationView) {

            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.ic_home -> {
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }

                    R.id.ic_search -> {
                        val intent = Intent(context, SearchActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }

                    R.id.ic_share -> {
                        val intent = Intent(context, ShareActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }

                    R.id.ic_news -> {
                        val intent = Intent(context, NewsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true
                    }

                    R.id.ic_profile -> {
                        val intent = Intent(context, ProfileActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                        true // Return true to indicate that the item selection has been handled
                    }

                    else -> false
                }
            }
        }
    }
