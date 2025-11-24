package com.example.basic_demo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

public class MainActivity : AppCompatActivity() {

    // App list with icons and package names
    private val apps = listOf(
        AppItem("WhatsApp", "com.whatsapp", R.drawable.wp),
        AppItem("Instagram", "com.instagram.android", R.drawable.insta),
        AppItem("Chrome", "com.android.chrome", R.drawable.ch),
        AppItem("Call", "phone", R.drawable.fb), // special case for Phone
        AppItem("Gmail", "com.google.android.gm", R.drawable.mail),
        AppItem("Maps", "com.google.android.apps.maps", R.drawable.loc)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridView: GridView = findViewById(R.id.gridView)
        gridView.adapter = AppAdapter(this, apps)

        // Handle app clicks
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            launchApp(apps[position].packageName)
        }
    }

    // Function to launch apps
    private fun launchApp(packageName: String) {
        try {
            when (packageName) {
                "phone" -> { // Open dialer
                    val intent = Intent(Intent.ACTION_DIAL)
                    startActivity(intent)
                }
                else -> {
                    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                    if (launchIntent != null) {
                        startActivity(launchIntent)
                    } else {
                        // Open Play Store if app not installed
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                        startActivity(intent)
                        Toast.makeText(this, "App not installed. Redirecting to Play Store.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot open app: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

// Data class for app items
data class AppItem(val name: String, val packageName: String, val iconRes: Int)

// Adapter for GridView
class AppAdapter(private val context: Context, private val apps: List<AppItem>) : BaseAdapter() {
    override fun getCount(): Int = apps.size
    override fun getItem(position: Int): Any = apps[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: View.inflate(context, R.layout.grid_item, null)

        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val appName: TextView = view.findViewById(R.id.appName)

        val app = apps[position]
        appIcon.setImageResource(app.iconRes)
        appName.text = app.name

        return view
    }
}
