package com.example.frontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frontend.R
import com.example.frontend.adapters.CategoryAdapter
import com.example.frontend.databinding.FragmentMenuBinding
import com.example.frontend.interfaces.OnItemClickListener
import com.example.frontend.models.Category
import com.example.frontend.objects.CategoryDbClient
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycler.setHasFixedSize(true)

        val call = CategoryDbClient.service.getCategories()
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        val adapter = CategoryAdapter(data, this@MenuFragment)
                        binding.recycler.adapter = adapter
                        Log.d("Category", "Data: $data")
                    }
                } else {
                    Log.d("Category", "Error")
                }
            }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("Category", "Error")
            }
        })

        return view
    }

    fun navigateToDishesFragment(id: Int) {
        val fragment = DishesFragment()
        val bundle = Bundle()
        bundle.putInt("id", id)
        fragment.arguments = bundle
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_container, fragment).addToBackStack(null).commit()
    }

    override fun onItemClick(position: Int) {
        val adapter = binding.recycler.adapter as CategoryAdapter
        val category = adapter.getItem(position)
        val categoryId = category.id
        navigateToDishesFragment(categoryId)
    }
}