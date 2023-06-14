package com.herpestes.myshoppal.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.herpestes.myshoppal.R
import com.herpestes.myshoppal.databinding.ActivityMainBinding
import com.herpestes.myshoppal.utils.Constants

class MainActivity : AppCompatActivity() {
    class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val sharedPref = getSharedPreferences(Constants.MY_SHOP_PAL_PREFERENCES, Context.MODE_PRIVATE)
            val userName = sharedPref.getString(Constants.LOGGED_IN_USERNAME,"Hello")

            binding.userName.text = userName.toString()
        }
    }
}