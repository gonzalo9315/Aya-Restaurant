package com.example.frontend.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frontend.R
import com.example.frontend.models.Order
import com.example.frontend.objects.OrderDbClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tracking, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")

        val image: ImageView = view.findViewById(R.id.trackingImage)
        val text: TextView = view.findViewById(R.id.text)

        if(token == "") {
            Glide.with(view.context)
                .load(R.drawable.photo404)
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                .into(image)
            text.text = "¡No has hecho ningun pedido todavía!"
            text.setTextColor(ContextCompat.getColor(view.context, R.color.red))
        } else{
            var call = OrderDbClient.service.getMyNewOrder("Bearer $token")
            call.enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.isSuccessful) {
                        val data: Order? = response.body()
                        when (data?.state) {
                            "created" -> {
                                Glide.with(view.context)
                                    .load(R.drawable.created)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                                    .into(image)
                                text.text = "¡Tu pedido ha sido creado con exito!"
                            }
                            "cooking" -> {
                                Glide.with(view.context)
                                    .load(R.drawable.cooking)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                                    .into(image)
                                text.text = "¡Tu pedido esta siendo cocinado ahora mismo!"
                            }
                            "delivery" -> {
                                Glide.with(view.context)
                                    .load(R.drawable.delivery)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                                    .into(image)
                                text.text = "¡Tu pedido esta en camino hacia tu domicilio!"
                            }
                            "delivered" -> {
                                Glide.with(view.context)
                                    .load(R.drawable.photo404)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                                    .into(image)
                                text.text = "¡No has hecho ningun pedido todavía!"
                                text.setTextColor(ContextCompat.getColor(view.context, R.color.red))
                            }
                        }
                    } else {
                        Glide.with(view.context)
                            .load(R.drawable.photo404)
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                            .into(image)
                        text.text = "¡No has hecho ningun pedido todavía!"
                        text.setTextColor(ContextCompat.getColor(view.context, R.color.red))
                    }
                }
                override fun onFailure(call: Call<Order>, t: Throwable) {
                    showSnackbar(view, "Fallo en el servidor", R.color.red)
                }
            })
        }

        return view
    }

    fun showSnackbar(view: View, message: String, backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }
}