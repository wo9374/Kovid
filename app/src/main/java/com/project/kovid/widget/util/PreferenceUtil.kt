package com.project.kovid.widget.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val dataInitialed: SharedPreferences = context.getSharedPreferences("data_initialed", Context.MODE_PRIVATE)

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return dataInitialed.getBoolean(key, defValue)
    }

    fun setBoolean(key: String, bool: Boolean) {
        dataInitialed.edit().putBoolean(key, bool).apply()
    }

    fun getString(key: String, defValue: String): String {
        return dataInitialed.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        dataInitialed.edit().putString(key, str).apply()
    }
}