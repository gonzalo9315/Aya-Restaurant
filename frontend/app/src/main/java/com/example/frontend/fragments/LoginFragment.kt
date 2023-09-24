package com.example.frontend.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R
import com.example.frontend.models.LoginResponse
import com.example.frontend.models.UserLogin
import com.example.frontend.objects.AuthDbClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable the back button in the ActionBar
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val editor = sharedPreferences.edit()

        val submit: Button = view.findViewById(R.id.loginButton)

        submit.setOnClickListener{

            val layoutEmail = view.findViewById<TextInputLayout>(R.id.loginInputEmail)
            val inputEmail = layoutEmail.editText
            val valueEmail = inputEmail?.text.toString()

            val layoutPass = view.findViewById<TextInputLayout>(R.id.loginInputPass)
            val inputPass= layoutPass.editText
            val valuePass = inputPass?.text.toString()
            val user = UserLogin(valueEmail, valuePass)

            val call = AuthDbClient.service.login(user)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            editor.putString("token", loginResponse.token)
                            editor.putInt("role", loginResponse.role)
                            editor.apply()
                            navigateToTab()
                            showSnackbar(view, "Iniciaste sesión", R.color.green)
                        }
                    } else {
                        showSnackbar(view, "Credenciales incorrectas", R.color.red)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showSnackbar(view, "Fallo en el servidor", R.color.green)
                }
            })

        }

        val registerLink: TextView = view.findViewById(R.id.goRegister)
        registerLink.setOnClickListener{ navigatetoRegister() }

        return view
    }

    fun navigatetoRegister() {
        val fragment = RegisterFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commit()
    }

    fun navigateToTab() {
        val fragment = TabNavFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commit()
    }

    fun showSnackbar(view: View, message: String, backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }
}