package com.example.helpinghands.ui.admin

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.helpinghands.ui.admin.products.ProductManagementFragment
import com.example.helpinghands.ui.admin.requests.RequestListFragment
import com.example.helpinghands.ui.admin.services.ServiceManagementFragment

/**
 * Adapter for the ViewPager in the AdminDashboardFragment.
 * It manages the three main management tabs.
 */
class AdminPagerAdapter(fragmentActivity: AdminDashboardFragment) : FragmentStateAdapter(fragmentActivity) {

    // Define the titles for the tabs
    val tabTitles = listOf("Requests", "Products", "Services")

    override fun getItemCount(): Int = tabTitles.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RequestListFragment()
            1 -> ProductManagementFragment()
            2 -> ServiceManagementFragment()
            else -> RequestListFragment()
        }
    }
}