package com.example.frontend.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frontend.databinding.ItemCategoryBinding
import com.example.frontend.interfaces.OnItemClickListener
import com.example.frontend.models.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val listener: OnItemClickListener
    ): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(category: Category) {
            binding.nameCategory.text = category.name
            Glide.with(binding.root.context)
                .load("http://10.0.2.2:5001/images/categories/" + category.image)
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching if needed
                .into(binding.imageCategory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(categories[position])

        val item = categories[position]
        // Bind the item data to the ViewHolder

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    fun getItem(position: Int): Category {
        return categories[position]
    }
}