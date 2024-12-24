package com.abdullah.lostfound.ui.adapters

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abdullah.lostfound.R
import com.abdullah.lostfound.databinding.ItemLostBinding
import com.abdullah.lostfound.ui.dataclasses.Lost
import com.abdullah.lostfound.ui.viewholder.LostViewHolder
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlin.math.truncate

class LostAdapter(val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //if (viewType == 0) {
            val binding =
                ItemLostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LostViewHolder(binding)
//        }else{
//            val binding =
//                ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return OrderViewHolder(binding)
//        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (items.get(position) is Lost) return 0
        return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is LostViewHolder) {
            val Lost = items.get(position) as Lost
            holder.binding.title.text = Lost.title
            holder.binding.desc.text = Lost.description
            holder.binding.contact.text = Lost.contact
            holder.binding.name.text = Lost.name


            Glide.with(holder.itemView.context)
                .load(Lost.image)
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(holder.binding.productImage)

            holder.itemView.setOnClickListener {
//                holder.itemView.context.startActivity(
//                    Intent(
//                        holder.itemView.context,
//                        HandCraftDetailsActivity::class.java
//                    ).putExtra("data", Gson().toJson(HandCraft))
//                )
//            }


            }

//        else if (holder is OrderViewHolder) {
//            val order = items.get(position) as Order
//            holder.binding.productTitle.text = order.item?.title
//            holder.binding.productPrice.text = "$${order.item?.price!!*order.quantity}"
//            holder.binding.productQuantity.text = "Quantity: ${order.quantity}"
//
//            holder.binding.status.text =order.status
//
//            holder.itemView.setOnClickListener {
//                holder.itemView.context.startActivity(
//                    Intent(
//                        holder.itemView.context,
//                        OrderDetailsActivity::class.java
//                    ).putExtra("data", Gson().toJson(order))
//                )
//            }
//
//
//        }

        }
    }}