package com.solodilov.coffeeshopapp.presentation.cafe_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solodilov.coffeeshopapp.R
import com.solodilov.coffeeshopapp.databinding.ItemCafeBinding

class CafeListAdapter(
    private val onClick: (CafeUi) -> Unit,
) : ListAdapter<CafeUi, CafeViewHolder>(CafeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder =
        CafeViewHolder(
            binding = ItemCafeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick = onClick,
        )

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CafeViewHolder(
    private val binding: ItemCafeBinding,
    private val onClick: (CafeUi) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cafe: CafeUi) {
        binding.cafeName.text = cafe.name
        cafe.distance?.let { dist ->
            val distStr: String = if (dist < 1000) {
                itemView.context.getString(R.string.cafe_distance_m, dist)
            } else {
                itemView.context.getString(R.string.cafe_distance_km, dist / 1000)
            }
            binding.distance.isVisible = true
            binding.distance.text = distStr
        } ?: { binding.distance.isVisible = false }
        itemView.setOnClickListener { onClick(cafe) }
    }
}

private class CafeDiffCallback : DiffUtil.ItemCallback<CafeUi>() {

    override fun areItemsTheSame(oldItem: CafeUi, newItem: CafeUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CafeUi, newItem: CafeUi): Boolean {
        return oldItem == newItem
    }
}