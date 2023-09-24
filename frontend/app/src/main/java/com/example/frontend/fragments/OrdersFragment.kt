package com.example.frontend.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.frontend.R
import com.example.frontend.models.Order
import com.example.frontend.models.User
import com.example.frontend.objects.OrderDbClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val role = sharedPreferences.getInt("role", 0)

        if (role == 0) {
            var call = OrderDbClient.service.getMyOrders("Bearer $token")
            call.enqueue(object : Callback<List<Order>> {
                override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                    if (response.isSuccessful) {
                        val data: List<Order>? = response.body()
                        showOrder(view, data)
                    } else {
                        Log.d("OrderFragment", "$response")
                    }
                }
                override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                    Log.d("OrdersFragment", "$t")
                }
            })
        } else {
            var call = OrderDbClient.service.getAllNewOrders("Bearer $token")
            call.enqueue(object : Callback<List<Order>> {
                override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                    if (response.isSuccessful) {
                        val data: List<Order>? = response.body()
                        data?.let { orders ->
                            showOrder(view, orders)
                        }
                    } else {
                        Log.d("OrderFragment", "$response")
                        showSnackbar(view, "Fallo en el servidor", R.color.red)
                    }
                }
                override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                    Log.d("OrderFragment", "$t")
                    showSnackbar(view, "Fallo en el servidor", R.color.red)
                }
            })
        }


        return view
    }

    fun showOrder(view: View, data: List<Order>?) {
        val tableLayout: TableLayout = view.findViewById(R.id.tableLayout)
        val newRow = TableRow(requireContext())
        tableLayout.addView(newRow)

        if (data != null) {
            for (row in data) {
                val tableRow = TableRow(view.context)
                tableRow.setOnClickListener {
                    navigateToOrderUpdateFragment(row.id)
                }
                val cells = listOf(
                    row.id.toString(),
                    row.createdAt
                )
                for (cell in cells) {
                    val textView = TextView(view.context)
                    textView.text = cell
                    tableRow.addView(textView)
                }

                tableLayout.addView(tableRow)
            }
        }
    }


    fun navigateToOrderUpdateFragment(id: Int) {
        val fragment = OrderDetailFragment()
        val bundle = Bundle()
        bundle.putInt("id", id)
        fragment.arguments = bundle
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    fun showSnackbar(view: View, message: String, backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }
}