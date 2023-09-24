package com.example.frontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frontend.R
import com.example.frontend.adapters.DishesAdapter
import com.example.frontend.databinding.FragmentDishesBinding
import com.example.frontend.interfaces.OnItemClickListener
import com.example.frontend.models.Dish
import com.example.frontend.objects.DishesDbClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DishesFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentDishesBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentDishesBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.recyclerDishes.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerDishes.setHasFixedSize(true)

        val id = arguments?.getInt("id")
        val call = DishesDbClient.service.getDishesByCategory(id)
        call.enqueue(object : Callback<List<Dish>> {
            override fun onResponse(call: Call<List<Dish>>, response: Response<List<Dish>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        val adapter = DishesAdapter(data, this@DishesFragment)
                        binding.recyclerDishes.adapter = adapter
                        Log.d("Dishes", "Data: $data")
                    }
                } else {
                    Log.d("Dishes", "Error")
                }
            }
            override fun onFailure(call: Call<List<Dish>>, t: Throwable) {
                Log.d("Dishes", "Error")
            }
        })

        return view
    }

    fun navigateToDetailFragment(id: Int) {
        val fragment = DishDetailFragment()
        val bundle = Bundle()
        bundle.putInt("id", id)
        fragment.arguments = bundle
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commit()
    }


    override fun onItemClick(position: Int) {
        val adapter = binding.recyclerDishes.adapter as DishesAdapter
        val dish = adapter.getItem(position)
        val dishId = dish.id
        navigateToDetailFragment(dishId)
    }


}