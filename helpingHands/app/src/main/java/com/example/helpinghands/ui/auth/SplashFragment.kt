package com.example.helpinghands.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.helpinghands.R
import com.example.helpinghands.data.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val authViewModel: AuthViewModel by viewModels()
    private var hasNavigated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Small delay for clean Firebase initialization & animations
        Handler(Looper.getMainLooper()).postDelayed({
            authViewModel.checkCurrentUserRole()
        }, 800)

        // Observe authentication result
        authViewModel.authState.observe(viewLifecycleOwner) { result ->

            if (hasNavigated) return@observe

            result.onSuccess { user ->
                hasNavigated = true
                navigateToDashboard(user)
            }

            result.onFailure { error ->
                if (!hasNavigated) {
                    hasNavigated = true
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }
    }

    private fun navigateToDashboard(user: User) {
        if (user.isAdmin) {
            findNavController().navigate(R.id.action_splashFragment_to_adminDashboardFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_customerDashboardFragment)
        }
    }
}
