package com.example.habitstracker.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habitstracker.fragments.ui.GoodListFragment
import com.example.habitstracker.fragments.ui.BadListFragment

class FragmentsAdapter(
    parentFragment: Fragment
) :
    FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            GoodListFragment.newInstance()
        } else {
            BadListFragment.newInstance()
        }
    }

}