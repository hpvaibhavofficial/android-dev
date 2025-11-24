package com.example.helpinghands.ui.customer.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghands.R
import com.example.helpinghands.data.model.Order
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.*

class OrdersAdapter(
    private var list: List<Order>,
    private val onOrderClick: (Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    fun updateData(newList: List<Order>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = list[position]
        holder.bind(order)
        holder.card.setOnClickListener {
            onOrderClick(order)
        }
    }

    override fun getItemCount(): Int = list.size

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val card: CardView = itemView.findViewById(R.id.order_card)
        private val img: ImageView = itemView.findViewById(R.id.order_img)
        private val title: TextView = itemView.findViewById(R.id.order_title)
        private val subtitle: TextView = itemView.findViewById(R.id.order_subtitle)
        private val amount: TextView = itemView.findViewById(R.id.order_amount)
        private val date: TextView = itemView.findViewById(R.id.order_date)
        private val statusChip: Chip = itemView.findViewById(R.id.order_status_chip)

        fun bind(order: Order) {

            // Title
            title.text = order.serviceName

            // Subtitle
            subtitle.text = "ID: ${order.id} • Provider: ${order.providerName}"

            // Amount
            amount.text = "₹${order.amount}"

            // Date
            val ts = order.bookingTime
            date.text = if (ts != null) {
                val fmt = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                fmt.format(ts.toDate())
            } else {
                "-"
            }

            // Status chip
            val statusText = order.status
            statusChip.text = statusText.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

            when (statusText.lowercase(Locale.getDefault())) {
                "pending" -> statusChip.setChipBackgroundColorResource(R.color.chip_pending)
                "completed" -> statusChip.setChipBackgroundColorResource(R.color.chip_success)
                "in progress" -> statusChip.setChipBackgroundColorResource(R.color.chip_primary)
                "cancelled" -> statusChip.setChipBackgroundColorResource(R.color.chip_danger)
                else -> statusChip.setChipBackgroundColorResource(R.color.chip_neutral)
            }

            // Image loading using Glide
            Glide.with(itemView.context)
                .load(order.imageUrl)
                .placeholder(R.drawable.ic_service_placeholder)
                .error(R.drawable.ic_service_placeholder)
                .into(img)
        }
    }
}

















//package com.example.helpinghands.ui.customer.orders
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.cardview.widget.CardView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.helpinghands.R
//import com.example.helpinghands.data.model.Order
//import com.google.android.material.chip.Chip
//import java.text.SimpleDateFormat
//import java.util.*
//
//class OrdersAdapter(
//    private var list: List<Order>,
//    private val onOrderClick: (Order) -> Unit
//) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {
//
//    fun updateData(newList: List<Order>) {
//        list = newList
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_order, parent, false)
//        return OrderViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
//        val order = list[position]
//        holder.bind(order)
//        holder.card.setOnClickListener {
//            onOrderClick(order)
//        }
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val card: CardView = itemView.findViewById(R.id.order_card)
//        private val img: ImageView = itemView.findViewById(R.id.order_img)
//        private val title: TextView = itemView.findViewById(R.id.order_title)
//        private val subtitle: TextView = itemView.findViewById(R.id.order_subtitle)
//        private val amount: TextView = itemView.findViewById(R.id.order_amount)
//        private val date: TextView = itemView.findViewById(R.id.order_date)
//        private val statusChip: Chip = itemView.findViewById(R.id.order_status_chip)
//
//        fun bind(order: Order) {
//            // Title
//            title.text = order.serviceName ?: ""
//
//            // Subtitle → ID + Provider
//            subtitle.text = "ID: ${order.id ?: ""} • Provider: ${order.providerName ?: ""}"
//
//            // Amount (safe-format)
//            amount.text = "₹${order.amount ?: 0.0}"
//
//            // Date formatting (bookingTime is Firebase Timestamp? we expect it to be nullable)
//            try {
//                val ts = order.bookingTime
//                if (ts != null) {
//                    val dateObj = ts.toDate()
//                    val fmt = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//                    date.text = fmt.format(dateObj)
//                } else {
//                    date.text = "-"
//                }
//            } catch (e: Exception) {
//                date.text = "-"
//            }
//
//            // Status chip text (capitalized)
//            val statusText = order.status ?: ""
//            statusChip.text = statusText.replaceFirstChar {
//                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
//            }
//
//            // Chip background color (optional)
//            when (statusText.lowercase(Locale.getDefault())) {
//                "pending" -> statusChip.setChipBackgroundColorResource(R.color.chip_pending)
//                "completed" -> statusChip.setChipBackgroundColorResource(R.color.chip_success)
//                "in progress" -> statusChip.setChipBackgroundColorResource(R.color.chip_primary)
//                "cancelled" -> statusChip.setChipBackgroundColorResource(R.color.chip_danger)
//                else -> statusChip.setChipBackgroundColorResource(R.color.chip_neutral)
//            }
//
//            // Image placeholder — replace with Glide/Picasso if you want to load imageUrl
//            Glide.with(itemView.context)
//                .load(order.imageUrl)          // Firestore image URL
//                .placeholder(R.drawable.ic_service_placeholder)
//                .error(R.drawable.ic_service_placeholder)
//                .into(img)
//        }
//    }
//}
//
//
//
//
//
////package com.example.helpinghands.ui.customer.orders
////
////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////import android.widget.ImageView
////import android.widget.TextView
////import androidx.cardview.widget.CardView
////import androidx.recyclerview.widget.RecyclerView
////import com.example.helpinghands.R
////import com.example.helpinghands.data.model.Order
////import com.google.android.material.chip.Chip
////import java.text.SimpleDateFormat
////import java.util.*
////
////class OrdersAdapter(
////    private var list: List<T>,
////    private val onOrderClick: (Order) -> Unit
////) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {
////
////    fun updateData(newList: List<Order>) {
////        list = newList
////        notifyDataSetChanged()
////    }
////
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
////        val view = LayoutInflater.from(parent.context)
////            .inflate(R.layout.item_order, parent, false)
////        return OrderViewHolder(view)
////    }
////
////    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
////        val order = list[position]
////        holder.bind(order)
////        holder.card.setOnClickListener {
////            onOrderClick(order)
////        }
////    }
////
////    override fun getItemCount(): Int = list.size
////
////    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////
////        val card: CardView = itemView.findViewById(R.id.order_card)
////        private val img: ImageView = itemView.findViewById(R.id.order_img)
////        private val title: TextView = itemView.findViewById(R.id.order_title)
////        private val subtitle: TextView = itemView.findViewById(R.id.order_subtitle)
////        private val amount: TextView = itemView.findViewById(R.id.order_amount)
////        private val date: TextView = itemView.findViewById(R.id.order_date)
////        private val statusChip: Chip = itemView.findViewById(R.id.order_status_chip)
////
////        fun bind(order: Order) {
////            // Title
////            title.text = order.serviceName
////
////            // Subtitle → ID + Provider
////            subtitle.text = "ID: ${order.id} • Provider: ${order.providerName}"
////
////            // Amount
////            amount.text = "₹${order.amount}"
////
////            // Date formatting
////            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
////            date.text = order.bookingTime?.toDate()?.let { sdf.format(it) } ?: "—"
////
////            // Status chip
////            statusChip.text = order.status.replaceFirstChar {
////                if (it.isLowerCase()) it.titlecase() else it.toString()
////            }
////
////            // Chip background color (optional, if you added the colors earlier)
////            when (order.status.lowercase(Locale.ROOT)) {
////                "pending" -> statusChip.setChipBackgroundColorResource(R.color.chip_pending)
////                "completed" -> statusChip.setChipBackgroundColorResource(R.color.chip_success)
////                "in progress" -> statusChip.setChipBackgroundColorResource(R.color.chip_primary)
////                "cancelled" -> statusChip.setChipBackgroundColorResource(R.color.chip_danger)
////                else -> statusChip.setChipBackgroundColorResource(R.color.chip_neutral)
////            }
////
////            // Image placeholder (Glide later)
////            img.setImageResource(R.drawable.ic_service_placeholder)
////        }
////    }
////}
