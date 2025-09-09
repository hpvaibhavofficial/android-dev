package com.example.activity_chapter_1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DetailsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var headingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Get references
        recyclerView = findViewById(R.id.recyclerViewCompanies)
        headingText = findViewById(R.id.headingText)

        // ✅ FIX: Consistent key (CATEGORY_NAME)
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Unknown"
        headingText.text = "Top 10 Companies in $categoryName"

        // Generate 10 companies for this category
        val companies = getCompaniesForCategory(categoryName)

        // Setup RecyclerView as Grid (2 columns)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = CompanyAdapter(companies)
    }

    // Function to generate 10 companies for each category
    private fun getCompaniesForCategory(category: String): List<Company> {
        return when (category) {
            "Android" -> listOf(
                Company(R.drawable.google, "Google", "Creators of Android"),
                Company(R.drawable.google, "Samsung", "Top Android phone maker"),
                Company(R.drawable.google, "OnePlus", "Flagship killer brand"),
                Company(R.drawable.google, "Xiaomi", "Affordable smartphones"),
                Company(R.drawable.google, "Oppo", "Known for camera phones"),
                Company(R.drawable.google, "Vivo", "Popular in Asia"),
                Company(R.drawable.google, "Realme", "Budget-friendly"),
                Company(R.drawable.google, "Motorola", "Classic brand"),
                Company(R.drawable.google, "Sony", "Xperia series"),
                Company(R.drawable.google, "Huawei", "Global Android OEM")
            )

            "iOS" -> listOf(
                Company(R.drawable.apple, "Apple", "Makers of iPhone"),
                Company(R.drawable.apple, "Foxconn", "Apple’s manufacturing partner"),
                Company(R.drawable.apple, "IBM", "iOS enterprise solutions"),
                Company(R.drawable.apple, "Cisco", "Enterprise networking for iOS"),
                Company(R.drawable.apple, "SAP", "Business apps for iOS"),
                Company(R.drawable.apple, "Adobe", "Creative apps for iOS"),
                Company(R.drawable.apple, "Microsoft", "Office & apps on iOS"),
                Company(R.drawable.apple, "Intel", "Apple silicon partners"),
                Company(R.drawable.apple, "Qualcomm", "Chip provider"),
                Company(R.drawable.apple, "Oracle", "Database apps on iOS")
            )

            "Electrical" -> listOf(
//                Company(R.drawable.ge, "General Electric", "Global leader in electricals"),
//                Company(R.drawable.siemens, "Siemens", "Top electrical & electronics company"),
//                Company(R.drawable.schneider, "Schneider Electric", "Energy management solutions"),
//                Company(R.drawable.abb, "ABB", "Automation & power leader"),
//                Company(R.drawable.honeywell, "Honeywell", "Industrial automation"),
//                Company(R.drawable.philips, "Philips", "Consumer electricals & lighting"),
//                Company(R.drawable.toshiba, "Toshiba", "Power systems & devices"),
//                Company(R.drawable.panasonic, "Panasonic", "Electronics & electrical goods"),
//                Company(R.drawable.hitachi, "Hitachi", "Industrial electrical solutions"),
//                Company(R.drawable.mitsubishi, "Mitsubishi Electric", "Automation & power systems")
            )

            "React" -> listOf(
//                Company(R.drawable.facebook, "Meta (Facebook)", "Creators of ReactJS"),
//                Company(R.drawable.netflix, "Netflix", "Uses React for UI"),
//                Company(R.drawable.airbnb, "Airbnb", "React-based frontends"),
//                Company(R.drawable.instagram, "Instagram", "Built on React"),
//                Company(R.drawable.whatsapp, "WhatsApp", "Uses React Native"),
//                Company(R.drawable.discord, "Discord", "React for chat system"),
//                Company(R.drawable.uber, "Uber", "Uses React Native"),
//                Company(R.drawable.microsoft, "Microsoft", "Uses React in products"),
//                Company(R.drawable.shopify, "Shopify", "React-based dashboards"),
//                Company(R.drawable.atlassian, "Atlassian", "React in Jira & Confluence")
            )

            else -> List(10) { i ->
                Company(R.drawable.img, "Company $i", "Info about company $i")
            }
        }
    }
}
