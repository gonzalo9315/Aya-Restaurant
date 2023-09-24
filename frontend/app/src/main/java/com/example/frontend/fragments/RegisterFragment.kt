package com.example.frontend.fragments

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
import com.example.frontend.models.User
import com.example.frontend.models.UserLogin
import com.example.frontend.objects.AuthDbClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val submit: Button = view.findViewById(R.id.registerButton)

        submit.setOnClickListener{

            val layoutEmail = view.findViewById<TextInputLayout>(R.id.registerInputEmail)
            val inputEmail = layoutEmail.editText
            val valueEmail = inputEmail?.text.toString()

            val layoutPass = view.findViewById<TextInputLayout>(R.id.registerInputPass)
            val inputPass= layoutPass.editText
            val valuePass = inputPass?.text.toString()

            val layoutName= view.findViewById<TextInputLayout>(R.id.registerInputName)
            val inputName= layoutName.editText
            val valueName = inputName?.text.toString()

            val layoutAddress = view.findViewById<TextInputLayout>(R.id.registerInputAddress)
            val inputAddress = layoutAddress.editText
            val valueAddress = inputAddress?.text.toString()

            val layoutTlf = view.findViewById<TextInputLayout>(R.id.registerInputTlf)
            val inputTlf= layoutTlf.editText
            val valueTlf = inputTlf?.text.toString()

            val user: User = User(valueAddress, valueEmail, valueName, valuePass, valueTlf)

            val call = AuthDbClient.service.register(user)
            call.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        navigateToLogin()
                        showSnackbar(view, "Te has registrado correctamente", R.color.green)
                    } else {
                        showSnackbar(view, "Has introducido algo incorrecto", R.color.red)
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    showSnackbar(view, "Fallo en el servidor", R.color.red)
                }
            })
        }

        val loginLink: TextView = view.findViewById(R.id.goLogin)
        loginLink.setOnClickListener{ navigateToLogin() }

        return view
    }

    fun navigateToLogin() {
        val fragment = LoginFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commit()
    }

    fun showSnackbar(view: View, message: String, backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }
}