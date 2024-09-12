package com.muratguzel.instakotlin.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.muratguzel.instakotlin.Home.HomeActivity


class HomePagerAdapter(homeActivity:HomeActivity) : FragmentStateAdapter(homeActivity) {
    private var mFragmentList: ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
    fun addFragment(fragment: Fragment){
        mFragmentList.add(fragment)
    }
}
