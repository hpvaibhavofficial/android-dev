package com.example.helpinghands.ui.customer.orders

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghands.R
import com.example.helpinghands.data.model.Order
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class OrdersFragment : Fragment() {

    private lateinit var rvOrders: RecyclerView
    private lateinit var searchInput: TextInputEditText
    private lateinit var emptyView: View
    private lateinit var fabSort: FloatingActionButton

    private lateinit var adapter: OrdersAdapter
    private val db = FirebaseFirestore.getInstance()

    private var firestoreListener: ListenerRegistration? = null

    private var allOrders: MutableList<Order> = mutableListOf()
    private var currentStatusFilter: String? = null   // null = ALL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvOrders = view.findViewById(R.id.orders_rv)
        searchInput = view.findViewById(R.id.orders_search_input)
        emptyView = view.findViewById(R.id.orders_empty)
        fabSort = view.findViewById(R.id.orders_fab_filter)

        setupRecyclerView()
        setupSearch()
        setupChips(view)
        setupSorting()
        loadOrders()
    }

    // -----------------------------------------------------------
    // RECYCLER VIEW + ADAPTER
    // -----------------------------------------------------------
    private fun setupRecyclerView() {

        adapter = OrdersAdapter(listOf()) { order ->

            // ⭐ Safe Args Navigation to Order Details Screen
            val action = OrdersFragmentDirections
                .actionOrdersFragmentToOrderDetailsFragment(order)

            findNavController().navigate(action)
        }

        rvOrders.layoutManager = LinearLayoutManager(requireContext())
        rvOrders.adapter = adapter
    }

    // -----------------------------------------------------------
    // SEARCH
    // -----------------------------------------------------------
    private fun setupSearch() {
        searchInput.addTextChangedListener { applyFilters() }
    }

    // -----------------------------------------------------------
    // FILTER CHIPS
    // -----------------------------------------------------------
    private fun setupChips(view: View) {
        val chipAll: Chip = view.findViewById(R.id.chips_all)
        val chipPending: Chip = view.findViewById(R.id.chips_pending)
        val chipInProgress: Chip = view.findViewById(R.id.chips_inprogress)
        val chipCompleted: Chip = view.findViewById(R.id.chips_completed)
        val chipCancelled: Chip = view.findViewById(R.id.chips_cancelled)

        chipAll.setOnClickListener { currentStatusFilter = null; applyFilters() }
        chipPending.setOnClickListener { currentStatusFilter = "pending"; applyFilters() }
        chipInProgress.setOnClickListener { currentStatusFilter = "in progress"; applyFilters() }
        chipCompleted.setOnClickListener { currentStatusFilter = "completed"; applyFilters() }
        chipCancelled.setOnClickListener { currentStatusFilter = "cancelled"; applyFilters() }
    }

    // -----------------------------------------------------------
    // SORTING
    // -----------------------------------------------------------
    private fun setupSorting() {
        fabSort.setOnClickListener { showSortDialog() }
    }

    private fun showSortDialog() {
        val options = arrayOf(
            "Newest first",
            "Oldest first",
            "Price: Low → High",
            "Price: High → Low"
        )

        AlertDialog.Builder(requireContext())
            .setTitle("Sort Orders")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> allOrders.sortByDescending { it.bookingTime?.toDate()?.time ?: 0L }
                    1 -> allOrders.sortBy { it.bookingTime?.toDate()?.time ?: 0L }
                    2 -> allOrders.sortBy { it.amount }
                    3 -> allOrders.sortByDescending { it.amount }
                }
                applyFilters()
            }
            .show()
    }

    // -----------------------------------------------------------
    // LOAD ORDERS FROM FIRESTORE
    // -----------------------------------------------------------
    private fun loadOrders() {
        firestoreListener?.remove()

        firestoreListener = db.collection("orders")
            .orderBy("bookingTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                allOrders.clear()

                snapshot?.documents?.forEach { doc ->
                    val order = doc.toObject(Order::class.java)
                    if (order != null) {
                        allOrders.add(order.copy(id = doc.id))
                    }
                }

                applyFilters()
            }
    }

    // -----------------------------------------------------------
    // APPLY SEARCH + STATUS FILTER
    // -----------------------------------------------------------
    private fun applyFilters() {
        val queryText = searchInput.text.toString().trim().lowercase()

        val filtered = allOrders.filter { order ->

            val matchesStatus =
                currentStatusFilter?.let { status ->
                    order.status.equals(status, ignoreCase = true)
                } ?: true

            val matchesSearch =
                order.id.lowercase().contains(queryText) ||
                        order.serviceName.lowercase().contains(queryText) ||
                        order.providerName.lowercase().contains(queryText)

            matchesStatus && matchesSearch
        }

        adapter.updateData(filtered)

        emptyView.isVisible = filtered.isEmpty()
        rvOrders.isVisible = filtered.isNotEmpty()
    }

    override fun onStop() {
        super.onStop()
        firestoreListener?.remove()
    }
}
