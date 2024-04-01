package com.jaya.cerbung_160421041_160421132_160821028

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize default dark mode preference
        saveDarkModePreference(false)
    }

    fun saveDarkModePreference(isDarkMode: Boolean) {
        val sharedPreferences = getSharedPreferences("dark_mode_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_dark_mode", isDarkMode)
        editor.apply()
    }

    fun getDarkModePreference(): Boolean {
        val sharedPreferences = getSharedPreferences("dark_mode_pref", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_dark_mode", false)
    }
}