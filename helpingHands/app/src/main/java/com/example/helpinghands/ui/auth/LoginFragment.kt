package com.example.helpinghands.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.helpinghands.R
import com.example.helpinghands.data.model.User
import com.example.helpinghands.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    private var hasNavigated = false // Prevent double-navigation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Go to SignUp
        binding.tvSignUp.setOnClickListener {
            if (!hasNavigated) {
                hasNavigated = true
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }

        // Login button click
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email and Password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.signIn(email, password)
        }

        // -------------------------------
        // Loading State Observer
        // -------------------------------
        authViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
        }

        // -------------------------------
        // Auth Result Observer
        // -------------------------------
        authViewModel.authState.observe(viewLifecycleOwner) { result ->

            result.onSuccess { user ->
                if (!hasNavigated) {
                    hasNavigated = true
                    Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
                    navigateToDashboard(user)
                }
            }

            result.onFailure { error ->
                // Ignore initial splash failure "Not Authenticated"
                if (error.message != "Not Authenticated" && !hasNavigated) {
                    Toast.makeText(
                        requireContext(),
                        error.message ?: "Login failed.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun navigateToDashboard(user: User) {
        if (user.isAdmin) {
            findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment)
        } else {
            findNavController().navigate(R.id.action_loginFragment_to_customerDashboardFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
