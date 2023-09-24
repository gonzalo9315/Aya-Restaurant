package com.example.frontend.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.frontend.R
import com.example.frontend.models.User
import com.example.frontend.objects.AuthDbClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUpdateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_profile_update, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")

        val layoutEmail = view.findViewById<TextInputLayout>(R.id.registerInputEmail)
        val inputEmail = layoutEmail.editText

        val layoutPass = view.findViewById<TextInputLayout>(R.id.registerInputPass)
        val inputPass= layoutPass.editText

        val layoutName= view.findViewById<TextInputLayout>(R.id.registerInputName)
        val inputName= layoutName.editText

        val layoutAddress = view.findViewById<TextInputLayout>(R.id.registerInputAddress)
        val inputAddress = layoutAddress.editText

        val layoutTlf = view.findViewById<TextInputLayout>(R.id.registerInputTlf)
        val inputTlf= layoutTlf.editText

        val updateButton: Button = view.findViewById(R.id.updateButton)

        val call = AuthDbClient.service.getMyInfo("Bearer $token")
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val data: User? = response.body()
                if (response.isSuccessful) {
                    inputEmail?.setText(data?.email)
                    inputName?.setText(data?.name)
                    inputAddress?.setText(data?.address)
                    inputTlf?.setText(data?.phone)
                } else {
                    Log.d("ProfileUpdate", "error")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("ProfileUpdate", "Error")
            }
        })

        updateButton.setOnClickListener{
            var email = inputEmail?.text.toString()
            var pass = inputPass?.text.toString()
            var name = inputName?.text.toString()
            var address = inputAddress?.text.toString()
            var tlf = inputTlf?.text.toString()

            val user = User(address, email, name, pass, tlf)

            val call = AuthDbClient.service.updateInfo("Bearer $token", user)
            call.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.d("ee", "$response")
                    if (response.isSuccessful) {
                        navigateToProfileFragment()
                        showSnackbar(view, "Has Actualizado correctamente tu cuenta")
                    } else {
                        showSnackbar(view, "Has introducido datos erróneos")
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    showSnackbar(view, "Fallo en el servidor")
                }
            })
        }

        return view
    }

    fun navigateToProfileFragment() {
        val fragment = ProfileFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    fun showSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(R.color.red.toInt())
        snackbar.show()
    }
}