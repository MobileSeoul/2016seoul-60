package com.example.dudco.ovs_seoulappcompetition

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

/**
 * Created by dudco on 2016. 10. 20..
 */
class MyPagerAdapter(fm:FragmentManager) : FragmentStatePagerAdapter(fm){
    private var mFragments = ArrayList<Fragment>()

    private val titles = arrayOf("메인으로", "카테고리", "지도", "프로필")
    fun addFragment(fragment: Fragment){
        mFragments.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

}