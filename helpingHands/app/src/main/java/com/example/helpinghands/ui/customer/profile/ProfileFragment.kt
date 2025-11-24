package com.example.helpinghands.ui.customer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.helpinghands.R
import com.example.helpinghands.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProfile()
        setupClicks()
    }

    // ----------------------------------------------------
    // LOAD USER PROFILE FROM FIRESTORE
    // ----------------------------------------------------
    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { doc ->

                val name = doc.getString("name") ?: "User"
                val email = doc.getString("email") ?: ""
                val phone = doc.getString("phone") ?: "--"
                val address = doc.getString("address") ?: "--"
                val imageUrl = doc.getString("imageUrl")

                // Set UI
                binding.profileName.text = name
                binding.profileEmail.text = email
                binding.profilePhone.text = "Phone: $phone"
                binding.profileAddress.text = "Address: $address"

                // Load profile image
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(binding.profileImg)
            }
    }

    // ----------------------------------------------------
    // BUTTON + CARD CLICK HANDLERS
    // ----------------------------------------------------
    private fun setupClicks() {

        // Edit Profile
        binding.profileEdit.setOnClickListener {
            // TODO: Navigate to EditProfileFragment
        }

        // Logout
        binding.profileLogout.setOnClickListener {
            auth.signOut()
            requireActivity().finish() // Restart app, goes back to Splash/Login
        }

        // QUICK ACTIONS
        // My Orders
        binding.profileQuickGrid.getChildAt(0).setOnClickListener {
            // TODO: Navigate to OrdersFragment
        }

        // Addresses
        binding.profileQuickGrid.getChildAt(1).setOnClickListener {
            // TODO: Navigate to Address Management Screen
        }

        // Wallet
        binding.profileQuickGrid.getChildAt(2).setOnClickListener {
            // TODO: Navigate to Wallet Screen (optional)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
