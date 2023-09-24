package com.example.frontend.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.frontend.R
import com.example.frontend.fragments.DishesFragment
import com.example.frontend.fragments.LoginFragment
import com.example.frontend.fragments.MenuFragment
import com.example.frontend.fragments.ProfileFragment
import com.example.frontend.fragments.RegisterFragment
import com.example.frontend.fragments.ShoppingCartFragment
import com.example.frontend.fragments.TabNavFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_container, TabNavFragment())
//            .addToBackStack(null)
            .commit()

        val actionBar = supportActionBar
//        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setHomeButtonEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the click event for the home/up button (back arrow)
                // You can perform any desired action here, such as navigating back or closing the activity
                onBackPressed()
                return true
            }
            R.id.shoppingCart -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_container, ShoppingCartFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.profile -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_container, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            // Handle other menu item clicks if needed
            // case R.id.menu_item_id -> { ... }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_container)

        if (currentFragment is ShoppingCartFragment ||
            currentFragment is ProfileFragment ||
            currentFragment is MenuFragment ||
            currentFragment is LoginFragment ||
            currentFragment is RegisterFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_container, TabNavFragment())
                .commitAllowingStateLoss()
        } else if (currentFragment is DishesFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_container, MenuFragment())
                .commit()
        } else {
            // Navigate back to the previous fragment
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        }
    }


}