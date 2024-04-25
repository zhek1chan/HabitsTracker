package com.example.habitstracker.domain.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habitstracker.ui.fragments.ListFragment

class FragmentsAdapter(
    parentFragment: Fragment
) :
    FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ListFragment.newInstance(false)
        } else {
            ListFragment.newInstance(true)
        }
    }

}