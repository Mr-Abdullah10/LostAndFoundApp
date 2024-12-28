package com.abdullah.lostfound.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdullah.lostfound.R
import com.abdullah.lostfound.databinding.ItemLostBinding
import com.abdullah.lostfound.ui.dataclasses.Lost
import com.abdullah.lostfound.ui.main.lost.LostDetailsActivity
import com.abdullah.lostfound.ui.viewholder.LostViewHolder
import com.bumptech.glide.Glide
import com.google.gson.Gson

class LostAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Inflate the layout for LostViewHolder
        val binding = ItemLostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is Lost) return 0
        return 2 // Default or other view types
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LostViewHolder) {
            val lostItem = items[position] as Lost

            // Bind data to views
            holder.binding.categoryinput.text = lostItem.category
            holder.binding.desc.text = lostItem.description
            holder.binding.contact.text = lostItem.contact
            holder.binding.name.text = lostItem.name




            // Load image
            Glide.with(holder.itemView.context)
                .load(lostItem.image)
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(holder.binding.productImage)

            // Set OnClickListener
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, LostDetailsActivity::class.java).apply {
                    putExtra("data", Gson().toJson(lostItem)) // Serialized Lost object
                    putExtra("userId", lostItem.userId)
                    putExtra("postDate", lostItem.postDate)
                    putExtra("categoryinput", lostItem.category)
                    putExtra("descriptioninput", lostItem.description)
                    putExtra("addressinput", lostItem.address)
                    putExtra("nameinput", lostItem.name)
                    putExtra("email", lostItem.email)
                    putExtra("contactinput", lostItem.contact)
                    putExtra("status", lostItem.status)
                    putExtra("imageView2", lostItem.image)
                    putExtra("spinnerPostType", if (lostItem.isLost) "Lost" else "Found")
                    putExtra("isLost", lostItem.isLost)
                    putExtra("isFound", lostItem.isFound)
                    putExtra("isFromLostFragment", lostItem.isLost )
                    //putExtra("isFromCompleteFragment", lostItem.isFound )

                }
                holder.itemView.context.startActivity(intent)
            }
        }


    }
}
