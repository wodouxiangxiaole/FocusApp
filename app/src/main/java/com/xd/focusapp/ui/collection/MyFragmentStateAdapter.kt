package com.xd.focusapp.ui.collection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentStateAdapter(activity: FragmentActivity, inputList:ArrayList<Fragment>): FragmentStateAdapter(activity) {
    lateinit var list:ArrayList<Fragment>
    init {
        list = inputList
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}