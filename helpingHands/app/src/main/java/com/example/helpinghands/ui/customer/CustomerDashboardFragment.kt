package com.example.helpinghands.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.helpinghands.R
import com.example.helpinghands.databinding.FragmentCustomerDashboardBinding // CRITICAL: ViewBinding Import
import dagger.hilt.android.AndroidEntryPoint

/**
 * Host Fragment for the Customer's main UI.
 * This fragment contains the BottomNavigationView and a nested NavHostFragment
 * to manage the four main tabs: Home, Orders, Categories, and Profile.
 */
@AndroidEntryPoint
class CustomerDashboardFragment : Fragment() {

    private var _binding: FragmentCustomerDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
        _binding = FragmentCustomerDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Get the NavController for the NESTED graph
        // We use childFragmentManager because the NavHost is inside this fragment's layout
        val navHostFragment = childFragmentManager.findFragmentById(R.id.customer_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        // 2. Link the BottomNavigationView to the NavController
        // This makes the tabs functional
        binding.bottomNav.setupWithNavController(navController)

        // Note: You may want to hide the main Activity's toolbar here if it's visible.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference
    }
}