package com.example.frontend.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frontend.databinding.ItemDishBinding
import com.example.frontend.interfaces.OnItemClickListener
import com.example.frontend.models.Dish

class DishesAdapter(
    private val dishes: List<Dish>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<DishesAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemDishBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(dish: Dish) {
            binding.nameDish.text = dish.name
            Glide.with(binding.root.context)
                .load("http://10.0.2.2:5001/images/dishes/" + dish.photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                .into(binding.imageDish)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemDishBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(dishes[position])
        val item = dishes[position]
        // Bind the item data to the ViewHolder

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    fun getItem(position: Int): Dish {
        return dishes[position]
    }
}