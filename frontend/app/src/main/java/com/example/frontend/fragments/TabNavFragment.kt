package com.example.frontend.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.frontend.R
import com.example.frontend.adapters.FragmentPageAdapter
import com.google.android.material.tabs.TabLayout

class TabNavFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab_nav, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val role = sharedPreferences.getInt("role", 10)
//        val selectedTabIndex = arguments?.getInt("selectedTabIndex", 1) ?: 0

        if(role === 1){
            val fragment = OrdersFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
        }
        else if(role === 2){
            val fragment = AdminDishesFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
        }

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager2 = view.findViewById(R.id.viewPager2)

        adapter = FragmentPageAdapter(requireActivity().supportFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.ic_home))
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.ic_menu))
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.ic_tracking))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener((object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        }))

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        return view
    }

//    override fun onResume() {
//        super.onResume()
//        viewPager2.setCurrentItem(lastSelectedTab, false) // Restore the last selected tab on fragment resume
//    }
//
//    override fun onPause() {
//        super.onPause()
//        sharedPreferences.edit().putInt("lastSelectedTab", lastSelectedTab).apply() // Store the last selected tab on fragment pause
//    }
}