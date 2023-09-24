package com.example.frontend.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.frontend.R
import com.example.frontend.models.Dish
import com.example.frontend.models.DishAmount
import com.example.frontend.models.DishCart
import com.example.frontend.models.Order
import com.example.frontend.objects.OrderDbClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_detail, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val role = sharedPreferences.getInt("role", 0)
        val id = arguments?.getInt("id")

        val title: TextView = view.findViewById(R.id.title)
        title.text = "Pedido: $id"
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val created = view.findViewById<RadioButton>(R.id.created)
        val cooking = view.findViewById<RadioButton>(R.id.cooking)
        val delivery = view.findViewById<RadioButton>(R.id.delivery)
        val delivered = view.findViewById<RadioButton>(R.id.delivered)
        val updateButton: Button = view.findViewById(R.id.updateButton)

        if (role === 1) {
            radioGroup.visibility = View.VISIBLE
            updateButton.visibility = View.VISIBLE
            var order: Order? = null

            var call = OrderDbClient.service.getOrderByID("Bearer $token", id)
            call.enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.isSuccessful) {
                        val data: Order? = response.body()
                        order = data // this
                        when (data?.state) {
                            "created" -> {
                                created.isChecked = true
                                created.setButtonDrawable(R.drawable.radio_button_style)
                            }
                            "cooking" -> {
                                cooking.isChecked = true
                            }
                            "delivery" -> {
                                delivery.isChecked = true
                            }
                            else -> {
                                delivered.isChecked = true
                            }
                        }
                    } else {
                        Log.d("OrderDetailFragment", "$response")
                        showSnackbar(view, "Fallo en el servidor", R.color.red)
                    }
                }
                override fun onFailure(call: Call<Order>, t: Throwable) {
                    Log.d("OrderDetailFragment", "$t")
                    showSnackbar(view, "Fallo en el servidor", R.color.red)
                }
            })

            updateButton.setOnClickListener{

                val selectedRadioButtonId = radioGroup.checkedRadioButtonId
                val selectedRadioButton = view.findViewById<RadioButton>(selectedRadioButtonId)
                val selectedOption = selectedRadioButton.text.toString()
                order?.state = selectedOption

                var call = OrderDbClient.service.updateOrder("Bearer $token", order , id)//here
                call.enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful) {
                            showSnackbar(view, "Pedido Actualizado", R.color.green)
                        } else {
                            showSnackbar(view, "Fallo en el servidor", R.color.red)
                        }
                    }
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        showSnackbar(view, "Fallo en el servidor", R.color.red)
                    }
                })

                navigateToOrdersFragment()
            }
        }

        var call = OrderDbClient.service.getAllItemsOfOrder("Bearer $token", id)
        call.enqueue(object : Callback<List<DishCart>> {
            override fun onResponse(call: Call<List<DishCart>>, response: Response<List<DishCart>>) {
                if (response.isSuccessful) {
                    val data: List<DishCart>? = response.body()
                    showItems(view, data)
                } else {
                    showSnackbar(view, "Fallo en el servidor", R.color.red)
                }
            }
            override fun onFailure(call: Call<List<DishCart>>, t: Throwable) {
                showSnackbar(view, "Fallo en el servidor", R.color.red)
            }
        })


        return view
    }

    fun showItems(view: View, data: List<DishCart>?) {
        val tableLayout: TableLayout = view.findViewById(R.id.tableLayout)
        val newRow = TableRow(requireContext())
        tableLayout.addView(newRow)
        var total = 0

        if (data != null) {
            for (row in data) {
                total += row.price.toInt()*row.amount.toInt()
                val tableRow = TableRow(view.context)
                val cells = listOf(
                    row.name,
                    row.amount,
                    row.price + "€"
                )
                for (cell in cells) {
                    val textView = TextView(view.context)
                    textView.text = cell
                    textView.gravity = Gravity.CENTER
                    tableRow.addView(textView)
                }

                tableLayout.addView(tableRow)
            }
            val totalRow = TableRow(requireContext())
            val textView1 = TextView(view.context)
            val textView2 = TextView(view.context)
            val textView3 = TextView(view.context)
            textView3.text = "Total: " + total + "€"
            textView3.gravity = Gravity.CENTER
            totalRow.addView(textView1)
            totalRow.addView(textView2)
            totalRow.addView(textView3)
            tableLayout.addView(totalRow)
        }
    }

    fun navigateToOrdersFragment() {
        val fragment = OrdersFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    fun showSnackbar(view: View, message: String, backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }
}