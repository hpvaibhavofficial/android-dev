package com.example.helpinghands.ui.customer.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.helpinghands.R
import com.example.helpinghands.data.model.Order
import com.example.helpinghands.databinding.FragmentOrderDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var order: Order

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ⭐ Fetch order via Safe Args
        order = OrderDetailsFragmentArgs.fromBundle(requireArguments()).order

        loadUI()
        setupButtons()
    }

    // -----------------------------------------------------------
    // LOAD ALL ORDER DETAILS INTO UI
    // -----------------------------------------------------------
    private fun loadUI() {

        // ⭐ Load Image via Glide
        Glide.with(requireContext())
            .load(order.imageUrl)
            .placeholder(R.drawable.ic_service_placeholder)
            .error(R.drawable.ic_service_placeholder)
            .into(binding.odImg)

        // Basic Info
        binding.odServiceName.text = order.serviceName
        binding.odProviderName.text = "Provider: ${order.providerName}"
        binding.odAmount.text = "₹${order.amount}"
        binding.odPaymentMethod.text = "Payment: ${order.paymentMethod}"

        // Status (Capitalized)
        val statusFormatted = order.status.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        binding.odStatus.text = statusFormatted

        // ⭐ Optional: Color coding for status chip/label
        when (order.status.lowercase()) {
            "pending" -> binding.odStatus.setTextColor(resources.getColor(R.color.chip_pending, null))
            "in progress" -> binding.odStatus.setTextColor(resources.getColor(R.color.chip_primary, null))
            "completed" -> binding.odStatus.setTextColor(resources.getColor(R.color.chip_success, null))
            "cancelled" -> binding.odStatus.setTextColor(resources.getColor(R.color.chip_danger, null))
        }

        // Date Formatting
        val ts = order.bookingTime
        binding.odBookingDate.text =
            ts?.let {
                val fmt = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                fmt.format(it.toDate())
            } ?: "-"

        // Customer Info
        binding.odCustomerName.text = order.customerName
        binding.odCustomerPhone.text = order.customerPhone
        binding.odCustomerAddress.text = order.customerAddress

        // Show Cancel Button Only if Pending
        binding.odBtnCancel.visibility =
            if (order.status.equals("pending", ignoreCase = true))
                View.VISIBLE
            else
                View.GONE
    }

    // -----------------------------------------------------------
    // BUTTON ACTIONS
    // -----------------------------------------------------------
    private fun setupButtons() {

        binding.odBtnRebook.setOnClickListener {
            // TODO: Navigate to booking screen
            // findNavController().navigate(...)
        }

        binding.odBtnCancel.setOnClickListener {
            // TODO: Add cancel logic here
            // (Update in Firestore -> status = cancelled)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
