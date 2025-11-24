package com.example.helpinghands

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlumbingApp : Application() {
    // This class initializes Hilt's code generation for dependency injection.
    // Ensure you reference this class in your AndroidManifest.xml:
    // android:name=".PlumbingApp"
}