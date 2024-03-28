package com.example.habitstracker.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.habitstracker.PageSelector
import com.example.habitstracker.R
import com.example.habitstracker.databinding.FragmentHabitsBinding
import com.example.habitstracker.fragments.FragmentsAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HabitsFragment : Fragment(), PageSelector {
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var binding: FragmentHabitsBinding
    private lateinit var activity: Activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity()
        val adapter = FragmentsAdapter(this)
        binding.viewPager.adapter = adapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.good)
                1 -> tab.text = getString(R.string.bad)
            }
        }
        tabMediator.attach()

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val currentPosition = tab.position
                    binding.viewPager.currentItem = currentPosition
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }
        )

    }

    override fun navigateTo(page: Int) {
        binding.viewPager.currentItem = page
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}