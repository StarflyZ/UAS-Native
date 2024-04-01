package com.jaya.cerbung_160421041_160421132_160821028

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyAdapter(val activity: AppCompatActivity, val fragments:ArrayList<Fragment>):FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}