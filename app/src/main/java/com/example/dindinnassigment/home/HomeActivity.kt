package com.example.dindinnassigment.home

import android.animation.ArgbEvaluator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.example.dindinnassigment.R


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putInt(getString(R.string.cart_count), 0)
        editor.apply()
        val homeFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, homeFragment)
        transaction.addToBackStack("home")
        transaction.commit()
    }

    override fun onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 2)
            getSupportFragmentManager().popBackStack();
        else
            this.finishAffinity();
    }

}