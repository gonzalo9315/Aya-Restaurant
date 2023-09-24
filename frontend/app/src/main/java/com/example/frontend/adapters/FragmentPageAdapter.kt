package com.example.frontend.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.frontend.fragments.HomeFragment
import com.example.frontend.fragments.MenuFragment
import com.example.frontend.fragments.TrackingFragment

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment()
            1 -> return MenuFragment()
            else -> return TrackingFragment()
        }
    }
}