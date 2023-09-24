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
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R
import com.example.frontend.models.DishCart
import com.example.frontend.models.Order
import com.example.frontend.models.OrderItem
import com.example.frontend.objects.OrderDbClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingCartFragment : Fragment() {
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val token = sharedPreferences.getString("token", "")
        val storedJson = sharedPreferences.getString("items", null)

        val linearEmpty: LinearLayout = view.findViewById(R.id.empty)
        val linearFull: LinearLayout = view.findViewById(R.id.full)
        val buyButton: Button = view.findViewById(R.id.buyButton)
        val cleanButton: Button = view.findViewById(R.id.cleanButton)

        if (storedJson != null) {
            linearEmpty.visibility = View.INVISIBLE
            linearFull.visibility = View.VISIBLE
            val storedObjects = gson.fromJson(storedJson, Array<DishCart>::class.java)
            showItems(view, storedObjects)

            buyButton.setOnClickListener {
                editor.remove("items")
                editor.apply()
                navigateToTabNavFragment()
                showSnackbar(view, "¡Has realizado un Pedido!", R.color.green)
            }

            /*buyButton.setOnClickListener {

                val layoutDireccion = view.findViewById<TextInputLayout>(R.id.InputDireccion)
                val inputDireccion = layoutDireccion.editText
                val valueDireccion = inputDireccion?.text.toString()
                val order = Order(valueDireccion, "created")

                val createOrderCall = OrderDbClient.service.createOrder("Bearer $token", order)
                createOrderCall.enqueue(object : Callback<Order> {
                    override fun onResponse(call: Call<Order>, response: Response<Order>) {
                        if (response.isSuccessful) {
                            val createdOrder: Order? = response.body()
                            val orderId = createdOrder?.id
                            if (orderId != null) {
                                addItemsToOrder(storedObjects, orderId)
                            } else {
                                Log.d("ShoppingCartFragment", "$response")
                                showSnackbar(view, "Fallo en el servidor", R.color.red)
                            }
                        } else {
                            Log.d("ShoppingCartFragment", "$response")
                            showSnackbar(view, "Fallo en el servidor", R.color.red)
                        }
                    }

                    override fun onFailure(call: Call<Order>, t: Throwable) {
                        Log.d("ShoppingCartFragment", "$t")
                        showSnackbar(view, "Fallo en el servidor", R.color.red)
                    }
                })
            }*/

            cleanButton.setOnClickListener{
                val fragment = TabNavFragment()
                val transaction = parentFragmentManager.beginTransaction()
                editor.remove("items")
                editor.apply()
                transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
            }
        } else {
            linearEmpty.visibility = View.VISIBLE
            linearFull.visibility = View.INVISIBLE
        }

        return view
    }

    private fun addItemsToOrder(items: Array<DishCart>, orderId: Int) {
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")

        addItemsToOrderRecursive(items, orderId, "Bearer $token", 0)
    }

    private fun addItemsToOrderRecursive(items: Array<DishCart>, orderId: Int, token: String, index: Int) {
        if (index >= items.size) {
            // Todas las llamadas se han completado
            navigateToTabNavFragment()
            showSnackbar(requireView(), "Pedido Creado", R.color.green)
            return
        }

        val item = items[index]
        val orderItem = OrderItem(item.amount.toInt(), item.id, orderId)
        val addItemToOrderCall = OrderDbClient.service.addItemToOrder(token, orderItem)
        addItemToOrderCall.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    addItemsToOrderRecursive(items, orderId, token, index + 1)
                } else {
                    showSnackbar(requireView(), "Fallo en el servidor", R.color.red)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                showSnackbar(requireView(), "Fallo en el servidor", R.color.red)
            }
        })
    }

    private fun showItems(view: View, data: Array<DishCart>) {
        val tableLayout: TableLayout = view.findViewById(R.id.tableLayout)
        val newRow = TableRow(requireContext())
        tableLayout.addView(newRow)
        var total = 0

        for (row in data) {
            total += row.price.toInt() * row.amount.toInt()
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
        textView3.text = "Total: $total€"
        textView3.gravity = Gravity.CENTER
        totalRow.addView(textView1)
        totalRow.addView(textView2)
        totalRow.addView(textView3)
        tableLayout.addView(totalRow)
    }

    private fun navigateToTabNavFragment() {
        val fragment = TabNavFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    private fun showSnackbar(view: View, message: String, backgroundColor: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }
}
