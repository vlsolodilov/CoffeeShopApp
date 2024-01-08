package com.solodilov.coffeeshopapp.presentation.order_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.solodilov.coffeeshopapp.R
import com.solodilov.coffeeshopapp.databinding.ItemMenuBinding
import com.solodilov.coffeeshopapp.databinding.ItemOrderBinding
import com.solodilov.coffeeshopapp.domain.entity.Coffee

class OrderDetailsAdapter(
    private val onDecreaseQuantityClick: (Coffee) -> Unit,
    private val onIncreaseQuantityClick: (Coffee) -> Unit,
) : ListAdapter<Coffee, OrderViewHolder>(CoffeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder =
        OrderViewHolder(
            binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onDecreaseClick = onDecreaseQuantityClick,
            onIncreaseClick = onIncreaseQuantityClick,
        )

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            for (payload in payloads) {
                if (payload is Int) {
                    holder.updateQuantity(payload)
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}

class OrderViewHolder(
    private val binding: ItemOrderBinding,
    private val onDecreaseClick: (Coffee) -> Unit,
    private val onIncreaseClick: (Coffee) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(coffee: Coffee) {
        binding.orderItemName.text = coffee.name
        binding.orderItemPrice.text = itemView.context.getString(R.string.coffee_price, coffee.price)
        binding.decreaseCount.setOnClickListener { onDecreaseClick(coffee) }
        binding.increaseCount.setOnClickListener { onIncreaseClick(coffee) }
        binding.coffeeCount.text = coffee.quantity.toString()
    }

    fun updateQuantity(newQuantity: Int) {
        binding.coffeeCount.text = newQuantity.toString()
    }
}

private class CoffeeDiffCallback : DiffUtil.ItemCallback<Coffee>() {

    override fun areItemsTheSame(oldItem: Coffee, newItem: Coffee): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Coffee, newItem: Coffee): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Coffee, newItem: Coffee): Any? {
        return if (oldItem.quantity != newItem.quantity) newItem.quantity else null
    }
}