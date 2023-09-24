package com.example.frontend.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val token = sharedPreferences.getString("token", "")
//        editor.clear()
//        editor.apply()
        val loginButton: Button = view.findViewById(R.id.loginButton)
        val registerButton: Button = view.findViewById(R.id.registerButton)
        val updateButton: Button = view.findViewById(R.id.updateButton)
        val ordersButton: Button = view.findViewById(R.id.ordersButton)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)

        if (token == "") {
            loginButton.visibility = View.VISIBLE
            registerButton.visibility = View.VISIBLE
            updateButton.visibility = View.INVISIBLE
            ordersButton.visibility = View.INVISIBLE
            logoutButton.visibility = View.INVISIBLE
        } else {
            updateButton.visibility = View.VISIBLE
            ordersButton.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
            loginButton.visibility = View.INVISIBLE
            registerButton.visibility = View.INVISIBLE
        }

        loginButton.setOnClickListener{ navigateToLoginFragment() }
        registerButton.setOnClickListener{ navigateToRegisterFragment() }
        updateButton.setOnClickListener{ navigateToUpdateFragment() }
        ordersButton.setOnClickListener{ navigateToOrdersFragment() }

        logoutButton.setOnClickListener{
            editor.remove("token")
            editor.remove("role")
            editor.clear()
            editor.apply()
            val fragment = TabNavFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
        }

        return view
    }

    fun navigateToLoginFragment() {
        val fragment = LoginFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    fun navigateToRegisterFragment() {
        val fragment = RegisterFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    fun navigateToUpdateFragment() {
        val fragment = ProfileUpdateFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    fun navigateToOrdersFragment() {
        val fragment = OrdersFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }
}