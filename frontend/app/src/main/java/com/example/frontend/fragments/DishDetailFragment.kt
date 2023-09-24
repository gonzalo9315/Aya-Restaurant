package com.example.frontend.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frontend.R
import com.example.frontend.models.Dish
import com.example.frontend.models.DishCart
import com.example.frontend.objects.DishesDbClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DishDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dish_detail, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val image: ImageView = view.findViewById(R.id.image)
        val name: TextView = view.findViewById(R.id.name)
        val description: TextView = view.findViewById(R.id.description)
        val ingredients: TextView = view.findViewById(R.id.ingredients)
        val price: TextView = view.findViewById(R.id.price)
        val layoutAmount = view.findViewById<TextInputLayout>(R.id.amountInput)
        val inputAmount = layoutAmount.editText

        val addButton: Button = view.findViewById(R.id.addButton)

        val id = arguments?.getInt("id")
        val call = DishesDbClient.service.getDishByID(id)
        call.enqueue(object : Callback<Dish> {
            override fun onResponse(call: Call<Dish>, response: Response<Dish>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Glide.with(view.context)
                            .load("http://10.0.2.2:5001/images/dishes/" + data.photo)
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                            .into(image)
                        name.text = data.name
                        description.text = data.description
                        ingredients.text = data.ingredients
                        price.text = data.price.toString()
                    }
                } else {
                    Log.d("DetailDishes", "Not found")
                }
            }
            override fun onFailure(call: Call<Dish>, t: Throwable) {
                Log.d("DetailDishes", "Error request")
            }
        })

        addButton.setOnClickListener{
            val valueAmount = inputAmount?.text.toString()
            val storedJson = sharedPreferences.getString("items", null)
            val gson = Gson()

            if (storedJson != null) {
                val existingArray = gson.fromJson(storedJson, Array<DishCart>::class.java).toMutableList()
                val newDish = DishCart(id, name.text.toString(), valueAmount, price.text.toString())
                existingArray.add(newDish)
                val updatedJson = gson.toJson(existingArray)
                editor.putString("items", updatedJson).apply()
            } else {
                val newDish = DishCart(id, name.text.toString(), valueAmount, price.text.toString())
                val newArray = arrayOf(newDish)
                val updatedJson = gson.toJson(newArray)
                editor.putString("items", updatedJson).apply()
            }

            navigateToMenuFragment()
            showSnackbar(view, "¡Has añadido un plato al carrito!", R.color.green)
        }


        return view
    }

    fun navigateToMenuFragment() {
        val fragment = MenuFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    fun showSnackbar(view: View, message: String, backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }
}